package org.example.tools.tests.utils;

import org.example.tools.pageobject.CheckoutCartPage;
import org.example.tools.pageobject.HomePage;
import org.example.tools.pageobject.ProductPage;
import org.example.tools.pageobject.entity.Cart;
import org.example.tools.pageobject.entity.UiCartElement;
import org.example.tools.pageobject.entity.UiProduct;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class CheckoutTestHelper {

    private final WebDriver driver;
    private final HomePage homePage;
    private final ProductPage productPage;
    private final CheckoutCartPage checkoutCartPage;
    private final Random random = new Random();

    public CheckoutTestHelper(WebDriver driver) {
        this.driver = driver;
        this.homePage = new HomePage(driver);
        this.productPage = new ProductPage(driver);
        this.checkoutCartPage = new CheckoutCartPage(driver);
    }

    public UiCartElement addRandomProductToCart(int maxQuantity) {
        homePage.open();
        homePage.waitUntilPageIsLoaded();

        homePage.openRandomProduct();
        productPage.waitUntilPageIsLoaded();

        int quantity = random.nextInt(maxQuantity) + 1;
        productPage.setButtonIncreaseQuantity(quantity);

        String name = normalizeProductName(productPage.getName());
        String price = productPage.getPrice().replace("$", "").trim();

        productPage.clickAddToCart();
        // TODO: add delay to wait for result
        productPage.waitForAddToCartToast();

        UiProduct uiProduct = UiProduct.withPrice(name, price);
        double unitPrice = Double.parseDouble(price);
        double subtotal = roundToTwoDecimals(unitPrice * quantity);
        return new UiCartElement(uiProduct, quantity, subtotal);
    }

    public Cart buildCartWithRandomProducts(int maxProducts, int maxQuantityPerProduct) {
        checkoutCartPage.open();
        checkoutCartPage.clearCartIfNeeded();

        Cart cart = new Cart();
        int numberOfProductsToAdd = random.nextInt(maxProducts) + 2;

        for (int i = 0; i < numberOfProductsToAdd; i++) {
            UiCartElement element = addRandomProductToCart(maxQuantityPerProduct);

            addOrMergeCartElement(cart, element);
            homePage.open();
            homePage.waitUntilPageIsLoaded();
        }

        return cart;
    }

    public double getExpectedCartTotal(Cart cart) {
        List<UiCartElement> items = cart.getItems();
        double expectedCartTotal  = 0;
        for (UiCartElement item : items) {
            double itemSubtotal = item.getSubtotal();
            expectedCartTotal  = expectedCartTotal  + itemSubtotal;
        }
        return expectedCartTotal ;
    }

    private void addOrMergeCartElement(Cart cart, UiCartElement newElement) {
        List<UiCartElement> items = cart.getItems();

        int existingIndex = -1;
        for (int i = 0; i < items.size(); i++) {
            UiCartElement existing = items.get(i);
            if (existing.getProduct().getName().equals(newElement.getProduct().getName())) {
                existingIndex = i;
                break;
            }
        }

        if (existingIndex == -1) {
            cart.add(newElement);
        } else {
            UiCartElement existing = items.get(existingIndex);
            int mergedQty = existing.getQuantity() + newElement.getQuantity();

            double unitPrice = Double.parseDouble(existing.getProduct().getPrice().replace("$", "").trim());
            double mergedSubtotal = roundToTwoDecimals(unitPrice * mergedQty);

            UiCartElement mergedElement = new UiCartElement(existing.getProduct(), mergedQty, mergedSubtotal);

            items.set(existingIndex, mergedElement);
        }
    }

    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static String normalizeProductName(String name) {
        return name == null ? null : name.replace('\u00A0', ' ').trim();
    }

    public CheckoutCartPage openCart() {
        checkoutCartPage.open();
        return checkoutCartPage;
    }

    // TODO: use Lists as parameters
    public void assertCartSubtotalsMatch(Cart expectedCart, List<UiCartElement> actualUiProducts) {
        System.out.println("assertCartSubtotalsMatch: expectedCart.getItems()=" + expectedCart.getItems() + ", actualUiProducts=" + actualUiProducts);
        for (UiCartElement expectedProduct : expectedCart.getItems()) {

            int actualProductIndex = actualUiProducts.indexOf(expectedProduct);
            if (actualProductIndex == -1) {
                throw new AssertionError("Product not found in actual cart: " + expectedProduct.getProduct().getName());
            }

//            UiCartElement actualProduct = actualUiProducts.stream()
//                    .filter(expectedProduct::equals)
//                    .findAny()
//                    .orElseThrow(() -> new AssertionError(
//                            "Product not found in actual cart: " + expectedProduct.getProduct().getName()
//                    ));

            UiCartElement actualProduct = actualUiProducts.get(actualProductIndex);
            double expectedItemPrice = Double.parseDouble(
                    expectedProduct.getProduct().getPrice().replace("$", "").trim()
            );
            double expectedSubtotal = expectedItemPrice * expectedProduct.getQuantity();
            double delta = 0.01;
            // check how to round value

            assertEquals(expectedSubtotal, actualProduct.getSubtotal(), delta,
                    "Subtotal price does not match for product: " + expectedProduct.getProduct().getName());
        }
    }

    public void addRandomProductAndAssertDeletion(CheckoutCartPage checkoutCartPage, int maxQuantity) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        int productsToAdd = 4;

        for (int i = 0; i < productsToAdd; i++) {
            homePage.open();
            homePage.waitUntilPageIsLoaded();
            homePage.openRandomProduct();
            productPage.waitUntilPageIsLoaded();

            int quantity = random.nextInt(maxQuantity) + 1;
            productPage.setButtonIncreaseQuantity(quantity);
            productPage.clickAddToCart();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast-container")));

            if (i < productsToAdd - 1) {
                driver.navigate().back();
                homePage.waitUntilPageIsLoaded();
            }
        }

        checkoutCartPage.open();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn-success[data-test='proceed-1']")));

        List<UiCartElement> productsBeforeDeletion = checkoutCartPage.getUIProductsInCart();
        if (productsBeforeDeletion.size() < 2) {
            throw new AssertionError("Expected at least 2 products in cart to test deletion, but got: " + productsBeforeDeletion.size());
        }

        UiCartElement randomProduct = productsBeforeDeletion.get(random.nextInt(productsBeforeDeletion.size()));
        String name = randomProduct.getProduct().getName();

        checkoutCartPage.deleteCartProduct(randomProduct);

        int expectedCountAfterDelete = productsBeforeDeletion.size() - 1;
        wait.until(driver -> driver.findElements(By.cssSelector(".table tbody tr")).size() == expectedCountAfterDelete);

        List<UiCartElement> productsAfterDeletion = checkoutCartPage.getUIProductsInCart();
        boolean stillPresent = productsAfterDeletion.stream()
                .anyMatch(p -> p.getProduct().getName().equals(name));

        if (stillPresent) {
            throw new AssertionError("Product " + name + " was not deleted from the cart");
        }
    }
}

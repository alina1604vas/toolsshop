package org.example.tools.tests.utils;

import org.example.tools.pageobject.CheckoutCartPage;
import org.example.tools.pageobject.HomePage;
import org.example.tools.pageobject.ProductPage;
import org.example.tools.pageobject.entity.Cart;
import org.example.tools.pageobject.entity.UiCartElement;
import org.example.tools.pageobject.entity.UiProduct;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

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

        String name = productPage.getName();
        String price = productPage.getPrice().replace("$", "").trim();

        productPage.clickAddToCart();

        UiProduct uiProduct = UiProduct.withPrice(name, price);
        return new UiCartElement(uiProduct, quantity);
    }

    public Cart buildCartWithRandomProducts(int maxProducts, int maxQuantityPerProduct) {
        checkoutCartPage.open();
        checkoutCartPage.clearCart();
        Cart cart = new Cart();
        int numberOfProductsToAdd = random.nextInt(maxProducts) + 1;

        for (int i = 0; i < numberOfProductsToAdd; i++) {
            UiCartElement element = addRandomProductToCart(maxQuantityPerProduct);
            addOrMergeCartElement(cart, element);

            driver.navigate().back();
            homePage.waitUntilPageIsLoaded();
        }

        return cart;
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

            UiCartElement mergedElement =
                    new UiCartElement(existing.getProduct(), mergedQty);

            items.set(existingIndex, mergedElement);
        }
    }

    public CheckoutCartPage openCart() {
        checkoutCartPage.open();
        return checkoutCartPage;
    }

    public void assertCartSubtotalsMatch(Cart expectedCart, List<UiCartElement> actualUiProducts) {
        for (UiCartElement expectedProduct : expectedCart.getItems()) {
            UiCartElement actualProduct = actualUiProducts.stream()
                    .filter(expectedProduct::equals)
                    .findAny()
                    .orElseThrow(() -> new AssertionError(
                            "Product not found in actual cart: " + expectedProduct.getProduct().getName()
                    ));

            double expectedItemPrice = Double.parseDouble(
                    expectedProduct.getProduct().getPrice().replace("$", "").trim()
            );
            double expectedSubtotal = expectedItemPrice * expectedProduct.getQuantity();
            double delta = 0.01;

            assertEquals(expectedSubtotal, actualProduct.getSubtotal(), delta,
                    "Subtotal price does not match for product: " +
                            expectedProduct.getProduct().getName());
        }
    }

    public void addRandomProductAndAssertDeletion(CheckoutCartPage checkoutCartPage, int maxQuantity) {
        homePage.open();
        homePage.waitUntilPageIsLoaded();
        homePage.openRandomProduct();
        productPage.waitUntilPageIsLoaded();

        int quantity = random.nextInt(maxQuantity) + 1;
        productPage.setButtonIncreaseQuantity(quantity);
        productPage.clickAddToCart();

        checkoutCartPage.open();

        List<UiCartElement> productsBeforeDeletion = checkoutCartPage.getUIProductsInCart();
        UiCartElement randomProduct = productsBeforeDeletion.get(random.nextInt(productsBeforeDeletion.size()));
        String name = randomProduct.getProduct().getName();

        checkoutCartPage.deleteCartProduct(randomProduct);

        List<UiCartElement> productsAfterDeletion = checkoutCartPage.getUIProductsInCart();
        boolean stillPresent = productsAfterDeletion.stream()
                .anyMatch(p -> p.getProduct().getName().equals(name));

        if (stillPresent) {
            throw new AssertionError("Product " + name + " was not deleted from the cart");
        }
    }
}

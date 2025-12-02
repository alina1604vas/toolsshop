package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.CheckoutCartPage;
import org.example.tools.pageobject.HomePage;
import org.example.tools.pageobject.ProductPage;
import org.example.tools.pageobject.entity.Cart;
import org.example.tools.pageobject.entity.UiCartElement;
import org.example.tools.pageobject.entity.UiProduct;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@EnabledForSprint(3)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckoutCartPageTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(CheckoutCartPageTest.class);
    private CheckoutCartPage checkoutCartPage;
    private HomePage homePage;
    private ProductPage productPage;

    @BeforeAll
    public void setUp() {
        homePage = new HomePage(driver);
        productPage = new ProductPage(driver);
        checkoutCartPage = new CheckoutCartPage(driver);
    }

    @AfterAll
    public void cleanUp() {
        checkoutCartPage = null;
    }

    //додати перевірку що початкова кількість 1 в тесті

    @Test
    @Tag("sprint3")
    @DisplayName("Verify products in the cart (their presence, name, price and quantity)")
    public void testProductsInCart() {
        Cart expectedCart = new Cart();
        homePage.open();
        homePage.openRandomProduct();
        productPage.waitUntilPageIsLoaded();
        int quantityToAdd = new Random().nextInt(10) + 1; // not to take zero
        productPage.setButtonIncreaseQuantity(quantityToAdd);
        String expectedName = productPage.getName();
        String expectedPrice = productPage.getPrice().replace("$", "").trim();
        UiProduct uiProduct = UiProduct.withPrice(expectedName, expectedPrice);
        UiCartElement cartElement = new UiCartElement(uiProduct, quantityToAdd);
        productPage.clickAddToCart();
        expectedCart.add(cartElement);

        checkoutCartPage.open();
        List<UiCartElement> actualUiProducts = checkoutCartPage.getUIProductsInCart();
        List<UiCartElement> expectedUiProducts = expectedCart.getItems();

        //Verify subtotal
        for (UiCartElement expectedProduct : expectedUiProducts) {
            int expectedQuantity = expectedProduct.getQuantity();
            double expectedItemPrice = Double.parseDouble(expectedProduct.getProduct().getPrice().replace("$", "").trim());
            double expectedSubtotal = expectedQuantity * expectedItemPrice;
            UiCartElement actualProduct = actualUiProducts.stream()
                    .filter(a -> a.equals(expectedProduct))
                    .findAny()
                    .orElseThrow(() -> new AssertionError(
                            "Product not found in actual cart: " + expectedProduct.getProduct().getName()
                    ));
            double actualSubtotal = actualProduct.getSubtotal();
            assertEquals(expectedSubtotal, actualSubtotal,
                    "Subtotal price does not match for product: "
                            + expectedProduct.getProduct().getName());
        }
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Verify if there are missed products in a cart")
    public void testMissedProducts() {
        homePage.open();
        Cart expectedCart = new Cart();

        int numberOfProductsToAdd = new Random().nextInt(5) + 1;
        for (int i = 0; i < numberOfProductsToAdd; i++) {
            homePage.openRandomProduct();
            productPage.waitUntilPageIsLoaded();
            int quantity = new Random().nextInt(5) + 1;
            productPage.setButtonIncreaseQuantity(quantity);
            productPage.clickAddToCart();
            String expectedName = productPage.getName();
            String expectedPrice = productPage.getPrice().replace("$", "").trim();
            UiProduct uiProduct = UiProduct.withPrice(expectedName, expectedPrice);
            UiCartElement cartElement = new UiCartElement(uiProduct, quantity);
            expectedCart.add(cartElement);

            driver.navigate().back();
            homePage.waitUntilPageIsLoaded();
        }
        checkoutCartPage.open();
        List<UiCartElement> actualUiProducts = checkoutCartPage.getUIProductsInCart();
        List<UiCartElement> expectedUiProducts = expectedCart.getItems();
        List<UiCartElement> missedProducts = new ArrayList<>();
        for (UiCartElement expectedCartElement : expectedUiProducts) {
            if (!actualUiProducts.contains(expectedCartElement)) {
                missedProducts.add(expectedCartElement);
            }
        }
        assertTrue(missedProducts.isEmpty(), "There are missed products: " + missedProducts);
    }


    @Test
    @Tag("sprint3")
    @DisplayName("Verify total price per cart (for several different products)")
    public void testTotalCartPrice() {
        checkoutCartPage.clearCart();
        homePage.open();
        double expectedCartTotal = 0.0;
        int numberOfProductsToAdd = new Random().nextInt(5) + 1;
        for (int i = 0; i < numberOfProductsToAdd; i++) {
            UiProduct product = homePage.openRandomProduct();
            productPage.waitUntilPageIsLoaded();
            double itemPrice = Double.parseDouble(product.getPrice().replace("$", "").trim());
            int quantity = new Random().nextInt(5) + 1;
            productPage.setButtonIncreaseQuantity(quantity);
            productPage.clickAddToCart();
            expectedCartTotal = expectedCartTotal + (itemPrice * quantity);

            homePage.open();
            homePage.waitUntilPageIsLoaded();
        }

        checkoutCartPage.open();
        double actualCartTotal = checkoutCartPage.getCartTotal();
        assertEquals(expectedCartTotal, actualCartTotal, 0.01);

    }

    @Test
    @Tag("sprint3")
    @DisplayName("Verify that after product is deleted, list of products in the cart is updated")
    public void testProductDeletion() {
        homePage.open();
        homePage.waitUntilPageIsLoaded();
        homePage.openRandomProduct();
        productPage.waitUntilPageIsLoaded();
        int quantity = new Random().nextInt(5) + 1;
        productPage.setButtonIncreaseQuantity(quantity);
        productPage.clickAddToCart();
        checkoutCartPage.open();

        List<UiCartElement> productsBeforeDeletion = checkoutCartPage.getUIProductsInCart();
        int randomIndex = new Random().nextInt(productsBeforeDeletion.size());
        UiCartElement randomProduct = productsBeforeDeletion.get(randomIndex);
        String name = randomProduct.getProduct().getName();
        checkoutCartPage.deleteCartProduct(randomProduct);
        List<UiCartElement> productsAfterDeletion = checkoutCartPage.getUIProductsInCart();
        assertFalse(
                productsAfterDeletion.stream()
                        .anyMatch(p -> p.getProduct().getName().equals(name)),
                "Product " + name + " was not deleted from the cart"
        );

    }

//    @Test
//    @Tag("sprint3")
//    @DisplayName("Verify that after clicking proceed to checkout button, user is redirected to Address and Name page")
//    public void testRedirectionToNameAndAddressPage() {
//        homePage.open();
//        homePage.waitUntilPageIsLoaded();
//        homePage.openRandomProduct();
//        productPage.waitUntilPageIsLoaded();
//        int quantity = new Random().nextInt(5) + 1;
//        productPage.setButtonIncreaseQuantity(quantity);
//        productPage.clickAddToCart();
//        checkoutCartPage.open();
//        checkoutCartPage.clickProceedToCheckout();
//
//    }
    //add end to end test

    }



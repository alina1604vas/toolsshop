package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.*;
import org.example.tools.pageobject.entity.Cart;
import org.example.tools.pageobject.entity.UiCartElement;
import org.example.tools.pageobject.entity.UiProduct;
import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledForSprint(3)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckoutNameAddressTest extends BaseTest {
    private HomePage homePage;
    private ProductPage productPage;
    private CheckoutCartPage checkoutCartPage;
    private CheckoutNameAddressPage nameAddressPage;
    private CheckoutPaymentPage paymentPage;

    @BeforeAll
    public void setUp() {
        homePage = new HomePage(driver);
        productPage = new ProductPage(driver);
        checkoutCartPage = new CheckoutCartPage(driver);
        Cart expectedCart = new Cart();
        paymentPage = new CheckoutPaymentPage(driver);
        nameAddressPage = new CheckoutNameAddressPage(driver);
        homePage.open();
        homePage.openRandomProduct();
        productPage.waitUntilPageIsLoaded();
        int quantityToAdd = new Random().nextInt(10) + 1;
        productPage.setButtonIncreaseQuantity(quantityToAdd);
        String expectedName = productPage.getName();
        String expectedPrice = productPage.getPrice().replace("$", "").trim();
        UiProduct uiProduct = UiProduct.withPrice(expectedName, expectedPrice);
        UiCartElement cartElement = new UiCartElement(uiProduct, quantityToAdd);
        productPage.clickAddToCart();
        expectedCart.add(cartElement);

        checkoutCartPage.open();
        checkoutCartPage.clickProceedToCheckout();
        nameAddressPage.waitUntilPageIsLoaded();
    }

    @AfterAll
    public void cleanUp() {
        homePage = null;
        productPage = null;
        checkoutCartPage = null;
        nameAddressPage = null;
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Verify that a form can be filled with valid data. User can proceed to checkout")
    public void testNameAddressFormSubmission() {
        nameAddressPage.enterFirstName();
        nameAddressPage.enterLastName();
        nameAddressPage.enterEmail();
        nameAddressPage.enterAddress();
        nameAddressPage.enterCity();
        nameAddressPage.enterState();
        nameAddressPage.enterCountry();
        nameAddressPage.enterPostCode();
        nameAddressPage.clickProceedToCheckoutButton();
        assertTrue(paymentPage.isLoaded(), "Payment page has not been loaded");
    }

}

package org.example.tools.tests;

import org.example.tools.pageobject.*;
import org.example.tools.pageobject.entity.Cart;
import org.example.tools.pageobject.entity.UiCartElement;
import org.example.tools.pageobject.entity.UiProduct;
import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckoutPaymentPageTest extends BaseTest {
    private HomePage homePage;
    private ProductPage productPage;
    private CheckoutCartPage checkoutCartPage;
    private CheckoutNameAddressPage nameAddressPage;
    private CheckoutPaymentPage paymentPage;

    @BeforeEach
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
        nameAddressPage.open();
        nameAddressPage.waitUntilPageIsLoaded();
        nameAddressPage.enterFirstName();
        nameAddressPage.enterLastName();
        nameAddressPage.enterEmail();
        nameAddressPage.enterAddress();
        nameAddressPage.enterCity();
        nameAddressPage.enterState();
        nameAddressPage.enterCountry();
        nameAddressPage.enterPostCode();
        nameAddressPage.clickProceedToCheckoutButton();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Verify payment is confirmed")
    public void testPaymentConfirmation() {
        assertTrue(paymentPage.isLoaded(), "Payment page has not been loaded");
        paymentPage.setPaymentMethodDropdown("Credit Card");
        paymentPage.enterValidAccountName();
        paymentPage.enterValidAccountNumber();
        paymentPage.confirmPayment();
        paymentPage.waitConfirmationMesg();
        assertTrue(paymentPage.isConfirmationMsgPresent(), "Payment confirmation message is NOT shown");
        assertEquals("Payment was successful", paymentPage.getPaymentConfirmationMsg(), "Confirmation payment message is NOT correct");
    }
}

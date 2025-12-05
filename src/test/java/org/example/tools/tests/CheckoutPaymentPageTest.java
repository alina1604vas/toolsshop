package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.*;
import org.junit.jupiter.api.*;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledForSprint(3)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckoutPaymentPageTest extends BaseTest {
    private HomePage homePage;
    private ProductPage productPage;
    private CheckoutCartPage checkoutCartPage;
    private CheckoutNameAddressPage nameAddressPage;
    private CheckoutPaymentPage paymentPage;

    @BeforeAll
    public void setUp() {
        homePage = new HomePage(driver);
        homePage.open();
        homePage.openRandomProduct();

        productPage = new ProductPage(driver);
        productPage.waitUntilPageIsLoaded();

        int quantityToAdd = new Random().nextInt(10) + 1;
        productPage.setButtonIncreaseQuantity(quantityToAdd);
        productPage.clickAddToCart();

        checkoutCartPage = new CheckoutCartPage(driver);
        checkoutCartPage.open();
        checkoutCartPage.clickProceedToCheckout();

        nameAddressPage = new CheckoutNameAddressPage(driver);
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

        paymentPage = new CheckoutPaymentPage(driver);
    }

    @AfterAll
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Verify payment confirmation")
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
    @Test
    @Tag("sprint3")
    @DisplayName("Verify that a user is redirected to Order Confirmation page after clicking Confirm button")
    public void testOrderConfirmation() {

    }
}

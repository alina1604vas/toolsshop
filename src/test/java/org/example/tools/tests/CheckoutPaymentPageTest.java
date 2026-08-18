package org.example.tools.tests;

import org.example.tools.pageobject.*;
import org.example.tools.tests.utils.CheckoutTestHelper;
import org.example.tools.utils.TestData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CheckoutPaymentPageTest extends BaseTest {

    private CheckoutTestHelper checkoutHelper;

    private CheckoutNameAddressPage nameAddressPage;
    private CheckoutPaymentPage paymentPage;
    private OrderConfirmationPage orderConfirmationPage;

    @BeforeEach
    void setUpEach() {
        checkoutHelper = new CheckoutTestHelper(driver);
        driver.manage().deleteAllCookies();

        checkoutHelper.addRandomProductToCart(10);

        CheckoutCartPage checkoutCartPage = checkoutHelper.openCart();
        checkoutCartPage.clickProceedToCheckout();

        CustomerLogin customerLogin = new CustomerLogin(driver);
        SuccessCustomerLogin successCustomerLogin;
        if (customerLogin.isCustomerLoginLoaded()) {
            successCustomerLogin = customerLogin.logIn(
                    TestData.realUserCreds.email(),
                    TestData.realUserCreds.password()
            );
            successCustomerLogin.isSuccessCustomerLoginLoaded();
        } else {
            successCustomerLogin = new SuccessCustomerLogin(driver);
        }
        successCustomerLogin.clickProceedToCheckout();

        nameAddressPage = new CheckoutNameAddressPage(driver);
        nameAddressPage.waitUntilPageIsLoaded();
        nameAddressPage.fillAddressViaPostcodeLookup();
        nameAddressPage.clickProceedToCheckoutButton();

        paymentPage = new CheckoutPaymentPage(driver);
        orderConfirmationPage = new OrderConfirmationPage(driver);

        assertTrue(paymentPage.isLoaded(), "Payment page has not been loaded");
    }
    @ParameterizedTest(name = "Verify payment with method: {0}")
    @ValueSource(strings = {
            "Bank Transfer",
            "Cash on Delivery",
            "Credit Card",
            "Buy Now Pay Later",
            "Gift Card"
    })
    @Tag("sprint3")
    @DisplayName("Verify successful payment and order confirmation")
    void testPaymentConfirmation(String paymentMethod) {
        paymentPage.setPaymentMethodDropdown(paymentMethod);
        paymentPage.fillPaymentDetails(paymentMethod);
        paymentPage.confirmPayment();
        paymentPage.waitConfirmationMesg();

        assertTrue(paymentPage.isConfirmationMsgPresent(), "Payment confirmation message is NOT shown");

        assertEquals("Payment was successful",
                paymentPage.getPaymentConfirmationMsg(),
                "Confirmation payment message is NOT correct");

        paymentPage.confirmOrder();

        boolean confirmationLoaded = orderConfirmationPage.isOrderConfirmationLoaded();
        assertTrue(confirmationLoaded,
                "Order Confirmation page is not loaded. Payment error on page: '"
                        + paymentPage.getPaymentErrorMessage() + "'");

        String actualMsg = orderConfirmationPage.getOrderConfirmationMsg();
        assertTrue(actualMsg.contains("Thanks for your order! Your invoice number is"),
                "Order Confirmation message is incorrect");
    }
}

package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledForSprint(4)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CheckoutPaymentPageTest extends BaseTest {

    private static HomePage homePage;
    private static ProductPage productPage;
    private static CheckoutCartPage checkoutCartPage;
    private static CheckoutNameAddressPage nameAddressPage;
    private CheckoutPaymentPage paymentPage;
    private OrderConfirmationPage orderConfirmationPage;

    @BeforeAll
    static void setUpAll() {
        homePage = new HomePage(driver);
        homePage.open();
    }

    @BeforeEach
    void setUpEach() {
        driver.manage().deleteAllCookies();
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
        orderConfirmationPage = new OrderConfirmationPage(driver);

        assertTrue(paymentPage.isLoaded(), "Payment page has not been loaded");
    }

    @AfterAll
    static void tearDownAll() {
        if (driver != null) {
            driver.quit();
        }
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
        paymentPage.enterValidAccountName();
        paymentPage.enterValidAccountNumber();
        paymentPage.confirmPayment();
        paymentPage.waitConfirmationMesg();

        assertTrue(paymentPage.isConfirmationMsgPresent(), "Payment confirmation message is NOT shown");

        assertEquals("Payment was successful",
                paymentPage.getPaymentConfirmationMsg(),
                "Confirmation payment message is NOT correct");

        paymentPage.confirmOrder();

        assertTrue(orderConfirmationPage.isOrderConfirmationLoaded(), "Order Confirmation page is not loaded");

        String actualMsg = orderConfirmationPage.getOrderConfirmationMsg();
        assertTrue(actualMsg.contains("Thanks for your order! Your invoice number is"), "OrderConfirmation message is incorrect");
    }
}

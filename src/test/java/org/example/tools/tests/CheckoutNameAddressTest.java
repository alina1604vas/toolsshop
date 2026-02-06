package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.*;
import org.example.tools.tests.utils.CheckoutTestHelper;
import org.example.tools.utils.TestData;
import org.example.tools.pageobject.entity.Cart;
import org.example.tools.pageobject.entity.UiCartElement;
import org.example.tools.pageobject.entity.UiProduct;
import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledForSprint(3)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CheckoutNameAddressTest extends BaseTest {
    private CheckoutTestHelper checkoutHelper;
    private CheckoutNameAddressPage nameAddressPage;
    private CheckoutPaymentPage paymentPage;
    private CustomerLogin customerLogin;
    private SuccessCustomerLogin successCustomerLogin;

    @BeforeEach
    public void setUp() {
        checkoutHelper = new CheckoutTestHelper(driver);

        paymentPage = new CheckoutPaymentPage(driver);
        nameAddressPage = new CheckoutNameAddressPage(driver);

        checkoutHelper.addRandomProductToCart(10);     // max qty 10 (same as before)
        checkoutHelper.openCart().clickProceedToCheckout();

        customerLogin = new CustomerLogin(driver);
        customerLogin.openCustomerLogin();
        customerLogin.isCustomerLoginLoaded();

        successCustomerLogin = customerLogin.logIn(
                TestData.realUserCreds.email(),
                TestData.realUserCreds.password()
        );
        successCustomerLogin.isSuccessCustomerLoginLoaded();
        successCustomerLogin.clickProceedToCheckout();

        nameAddressPage.waitUntilPageIsLoaded();
    }
    @Test
    @Tag("sprint3")
    @DisplayName("Verify when a form is filled with valid data, customer can proceed to checkout")
    public void testNameAddressFormSubmission() {
        nameAddressPage.enterAddress();
        nameAddressPage.enterCity();
        nameAddressPage.enterState();
        nameAddressPage.enterCountry();
        nameAddressPage.enterPostCode();
        nameAddressPage.clickProceedToCheckoutButton();

        assertTrue(paymentPage.isLoaded(), "Payment page has not been loaded");
    }

}

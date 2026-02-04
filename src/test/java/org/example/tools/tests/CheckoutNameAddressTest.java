package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.*;
import org.example.tools.utils.TestData;
import org.example.tools.pageobject.entity.Cart;
import org.example.tools.pageobject.entity.UiCartElement;
import org.example.tools.pageobject.entity.UiProduct;
import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledForSprint(4)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CheckoutNameAddressTest extends BaseTest {

    private HomePage homePage;
    private ProductPage productPage;
    private CheckoutCartPage checkoutCartPage;
    private LoginPage loginPage;
    private AccountPage accountPage;
    private CheckoutNameAddressPage nameAddressPage;
    private CheckoutPaymentPage paymentPage;
    private CustomerLogin customerLogin;
    private SuccessCustomerLogin successCustomerLogin;

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
        customerLogin = new CustomerLogin(driver);
        customerLogin.openCustomerLogin();
        customerLogin.isCustomerLoginLoaded();
        successCustomerLogin = customerLogin.logIn(TestData.realUserCreds.email(), TestData.realUserCreds.password());
        successCustomerLogin.isSuccessCustomerLoginLoaded();
        successCustomerLogin.clickProceedToCheckout();
        // checkoutCartPage.open();
        //checkoutCartPage.clickProceedToCheckout();
        nameAddressPage.waitUntilPageIsLoaded();
    }

    //this test may be applicable for guest - thing how to differentiate Guest flow VS Logged user flow
    @Test
    @Tag("sprint3")
    @DisplayName("Verify when a form is filled with valid data, customer can proceed to checkout")
    public void testNameAddressFormSubmission() {
//        nameAddressPage.enterFirstName();
//        nameAddressPage.enterLastName();
//        nameAddressPage.enterEmail();
        nameAddressPage.enterAddress();
        nameAddressPage.enterCity();
        nameAddressPage.enterState();
        nameAddressPage.enterCountry();
        nameAddressPage.enterPostCode();
        nameAddressPage.clickProceedToCheckoutButton();
        assertTrue(paymentPage.isLoaded(), "Payment page has not been loaded");
    }
//    @Test
//    @Tag("sprint3")
//    @DisplayName("Verify that validation error messages are present for empty fields")
//    public void testEmptyFieldValidation() {
//        //nameAddressPage.waitUntilPageIsLoaded();
//        nameAddressPage.triggerValidation("first_name");
//        nameAddressPage.waitError("first_name");
//        String actualFirstNameError = nameAddressPage.getErrorText("first_name");
//        assertEquals("First name is required.", actualFirstNameError, "First Name error message for empty field does not match");
//    }

}

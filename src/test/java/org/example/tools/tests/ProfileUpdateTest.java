package org.example.tools.tests;

import org.example.tools.SystemConfig;
import org.example.tools.pageobject.AccountPage;
import org.example.tools.pageobject.LoginPage;
import org.example.tools.pageobject.ProfilePage;
import org.example.tools.pageobject.RegistrationPage;
import org.example.tools.utils.Customer;
import org.example.tools.utils.TestData;
import org.example.tools.utils.UserFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ProfileUpdateTest extends BaseTest {

    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private Customer customer;
    private UserFactory userFactory;
    private ProfilePage profilePage;

    @BeforeEach
    void setUp() {
        driver.get(SystemConfig.getBaseUrl());
        driver.manage().deleteAllCookies();
        ((JavascriptExecutor) driver).executeScript(
                "window.localStorage.clear(); window.sessionStorage.clear();"
        );
        driver.navigate().refresh();

        registrationPage = new RegistrationPage(driver);
        userFactory = new UserFactory();
        registrationPage.open();
        String selectedCountry = registrationPage.chooseRandomCountry(registrationPage.getAvailableCountries());
        customer = userFactory.createCustomer(selectedCountry);
        registrationPage.registerUser(customer);
        assertTrue(registrationPage.isRegistrationSuccessful(), "Customer was not registered");
        loginPage = new LoginPage(driver);
        loginPage.openLogin();
        assertTrue(loginPage.isLoginPageOpened());
        AccountPage accountPage = loginPage.logIn(customer.getEmail(), customer.getPassword());
        assertTrue(accountPage.isPageLoaded(), "Login failed — account page not loaded");
        profilePage = new ProfilePage(driver).openProfilePage();
        profilePage.waitUntilPageIsLoaded();

    }

    @AfterEach
    void tearDown() {
        loginPage = null;
        registrationPage = null;
        profilePage = null;
    }

    @Test
    @DisplayName("Profile Info can be successfully updated")
    public void profile_CanBeUpdated() {
        //remove hardcoded country
        Customer updatedCustomer = userFactory.createCustomer("Ukraine");
        profilePage.updateProfile(updatedCustomer)
                .clickUpdateProfileBtn();
        assertEquals(updatedCustomer.getFirstName(), profilePage.getFirstName());
        assertEquals(updatedCustomer.getLastName(), profilePage.getLastName());
        assertEquals(updatedCustomer.getPhone(), profilePage.getPhone());
        assertEquals(updatedCustomer.getBillingAddress().getStreetAddress(), profilePage.getStreet());
        assertEquals(updatedCustomer.getBillingAddress().getPostCode(), profilePage.getPostCode());
        assertEquals(updatedCustomer.getBillingAddress().getCity(), profilePage.getCity());
        assertEquals(updatedCustomer.getBillingAddress().getState(), profilePage.getState());
        assertEquals(updatedCustomer.getBillingAddress().getCountry(), profilePage.getCountry());
        assertEquals("Your profile is successfully updated!", profilePage.getSuccessMsg());
    }

    @Test
    @DisplayName("Password can be successfully changed and a user can log in with a new password")
    public void changePassword_shouldAllowLoginWithNewPassword() {
        String newPassword = TestData.validPassword();
        profilePage.changePassword(customer.getPassword(), newPassword);
        assertEquals("Your password is successfully updated!", profilePage.getSuccessMsg());
        assertTrue(loginPage.isLoginPageOpened());
        AccountPage accountPage = loginPage.logIn(customer.getEmail(), newPassword);
        assertTrue(accountPage.isPageLoaded(), "Login failed — account page not loaded");
    }
}

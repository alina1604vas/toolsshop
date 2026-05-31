package org.example.tools.tests;

import org.example.tools.pageobject.AccountPage;
import org.example.tools.pageobject.LoginPage;
import org.example.tools.pageobject.ProfilePage;
import org.example.tools.utils.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.example.tools.tests.BaseTest.driver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ProfileDisplayTest {
    LoginPage loginPage;
    ProfilePage profilePage;
    Credentials creds = new Credentials();

    @BeforeEach
    void setUp() {
        loginPage = new LoginPage(driver);
        loginPage.openLogin();
        AccountPage accountPage = loginPage.logIn(creds.email(), creds.password());
        assertTrue(accountPage.isPageLoaded(), "Login failed — account page not loaded");
        profilePage = new ProfilePage(driver).openProfilePage();
        profilePage.waitUntilPageIsLoaded();
    }

    @Test
    @DisplayName("Profile displays current user info")
    public void profile_displaysCurrentUserInfo() {
        assertEquals(creds.firstName(), profilePage.getFirstName());
        assertEquals(creds.lastName(), profilePage.getLastName());
        assertEquals(creds.email(), profilePage.getEmail());
        assertEquals(creds.street(), profilePage.getStreet());
        assertEquals(creds.city(), profilePage.getCity());
        assertEquals(creds.country(), profilePage.getCountry());
    }
}

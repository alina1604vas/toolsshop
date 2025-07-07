package org.example.mail;
import org.example.mail.pageobject.LoginPage;
import org.example.mail.pageobject.RegistrationPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTest extends BaseTest {
    private RegistrationPage registrationPage;

    @BeforeEach
    public void setUpRegistrationPage() {
        registrationPage = new RegistrationPage(driver);
    }

    @AfterEach
    public void cleaUp() {
        registrationPage = null;
    }

    @Test
    @DisplayName("User registration with valid data")
    public void testUserRegistation() {
        registrationPage
                .setFirstName("Alina")
                .setLastName("User")
                .setBirthDate("16041992")
                .setStreet("Zelena")
                .setPostCode("21306")
                .setCity("Vinnytsia")
                .setState("Vinnytsia")
                .setCountry("Ukraine")
                .setPhone("1234567890")
                .setEmail("alina1604vas@gmail.com")
                .setPassword("Dwelon1234!")
                .clickRegisterButton();
    }

    @Test
    @DisplayName("User should be redirected to Login page")
    public void testRedirectionToLogin() {
        LoginPage loginPage = new LoginPage(driver);
        String expectedUrl = loginPage.getUrl();
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl,actualUrl);
    }
}

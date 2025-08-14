package org.example.mail;

import org.example.mail.pageobject.LoginPage;
import org.example.mail.pageobject.RegistrationPage;
import org.junit.jupiter.api.*;

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
    @Tag("sprint4")
    @DisplayName("User registration with valid data")
    public void testUserRegistration() {
        registrationPage
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setBirthDate(birthdayString)
                .setStreet(faker.address().streetName())
                .setPostCode(faker.address().zipCode())
                .setCity(faker.address().cityName())
                .setState(faker.address().cityName())
                .setCountry(faker.address().country())
                .setPhone(faker.phoneNumber().phoneNumber())
                .setEmail(faker.internet().emailAddress())
                .setPassword(faker.internet().password(8, 10, true, true))
                .clickRegisterButton();
    }

    @Test
    @Tag("sprint4")
    @DisplayName("User should be redirected to Login page")
    public void testRedirectionToLogin() {
        LoginPage loginPage = new LoginPage(driver);
        String expectedUrl = loginPage.getUrl();
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl,actualUrl);
    }

}

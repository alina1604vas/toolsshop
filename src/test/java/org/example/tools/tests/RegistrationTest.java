package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.LoginPage;
import org.example.tools.pageobject.RegistrationPage;
import org.junit.jupiter.api.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnabledForSprint(4)
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
    @EnabledForSprint(4)
    @DisplayName("User registration with valid data")
    public void testUserRegistration() {
        Date birthdayDate = faker.date().birthday(18, 65);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String birthdayString = formatter.format(birthdayDate);

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

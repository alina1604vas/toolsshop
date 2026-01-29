package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.LoginPage;
import org.example.tools.pageobject.RegistrationPage;
import org.example.tools.utils.Customer;
import org.example.tools.utils.TestData;
import org.example.tools.utils.UserFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@EnabledForSprint(4)
public class RegistrationTest extends BaseTest {

    private RegistrationPage registrationPage;

    @BeforeEach
    public void setUpRegistrationPage() {
        registrationPage = new RegistrationPage();
    }

    @AfterEach
    public void cleanUp() {
        registrationPage = null;
    }

    @Test
    @Tag("sprint4")
    @DisplayName("Customer registration with valid data")
    public void testUserRegistration() {
        registrationPage.open();
        String selectedCountry = registrationPage.chooseRandomCountry(
                registrationPage.getAvailableCountries());
        Customer randomUser = UserFactory.randomUser(selectedCountry);
        registrationPage.registerUser(randomUser);
        assertTrue(registrationPage.isRegistrationSuccessful(), "Customer was not registered");
        TestData.lastRegisteredUser = randomUser;
    }
// add param tests
}

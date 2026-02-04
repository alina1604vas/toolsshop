package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.RegistrationPage;
import org.example.tools.utils.TestData;
import org.example.tools.utils.User;
import org.example.tools.utils.UserFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@EnabledForSprint(4)
public class RegistrationTest extends BaseTest {

    private RegistrationPage registrationPage;
    private UserFactory userFactory;

    @BeforeEach
    public void setUpRegistrationPage() {
        registrationPage = new RegistrationPage(driver);
        userFactory = new UserFactory();
    }

    @AfterEach
    public void cleanUp() {
        registrationPage = null;
        userFactory = null;
    }

    @Test
    @Tag("sprint4")
    @DisplayName("Customer registration with valid data")
    public void testUserRegistration() {
        registrationPage.open();
        String selectedCountry = registrationPage.chooseRandomCountry(registrationPage.getAvailableCountries());
        User randomUser = userFactory.createRegularWith(selectedCountry);
        registrationPage.registerUser(randomUser);
        assertTrue(registrationPage.isRegistrationSuccessful(), "Customer was not registered");
        TestData.lastRegisteredUser = randomUser;
    }

    @ParameterizedTest
    @CsvFileSource(
            resources = "/registration_empty_fields.csv",
            numLinesToSkip = 1
    )
    public void errorMsg_present_for_empty_fields(String key, String expectedMsg) {
        registrationPage.open();
        registrationPage.clickRegisterButton();
        assertEquals(expectedMsg, registrationPage.getValidationErrorForField(key));
    }
}

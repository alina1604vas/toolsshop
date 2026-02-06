package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.RegistrationPage;
import org.example.tools.utils.TestData;
import org.example.tools.utils.Customer;
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
        Customer customer = userFactory.createCustomer(selectedCountry);
        registrationPage.registerUser(customer);
        assertTrue(registrationPage.isRegistrationSuccessful(), "Customer was not registered");
        TestData.lastRegisteredUser = customer;
    }

    //TODO: fix password field
    @DisplayName("Validation error is shown for empty input fields")
    @ParameterizedTest(name = "Test {index}: key={0}, expected message={1}")
    @CsvFileSource(
            resources = "/registration_empty_fields_input.csv",
            numLinesToSkip = 1
    )
    @Tag("sprint4")
    public void errorMsg_present_for_empty_fields(String key, String expectedMsg) {
        registrationPage.open();
        registrationPage.clickRegisterButton();
        assertEquals(expectedMsg, registrationPage.getValidationErrorForField(key));
    }

    @DisplayName("Validation error is shown for invalid phone input")
    @ParameterizedTest(name = "Test {index}: fieldKey={0}, input = {1}, expected message ={2}")
    @CsvFileSource(
            resources = "/registration_phone_invalid_input.csv",
            numLinesToSkip = 1
    )
    @Tag("sprint4")
    public void errorMsg_present_for_invalid_phone_input(String key, String input, String expectedMsg) {
        registrationPage.open();
        registrationPage.setPhone(input);
        registrationPage.clickRegisterButton();
        registrationPage.clearInputField(key);
        assertEquals(expectedMsg, registrationPage.getValidationErrorForField(key));
    }
}

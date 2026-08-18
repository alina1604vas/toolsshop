package org.example.tools.tests;

import org.example.tools.pageobject.ForgotPasswordPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ForgotPasswordPageTest extends BaseTest {
    ForgotPasswordPage forgotPasswordPage;

    @BeforeEach
    public void forgotPasswordPageSetUp() {
        forgotPasswordPage = new ForgotPasswordPage(driver);
        forgotPasswordPage.open();
    }

    @AfterEach
    public void cleanUp() {
        forgotPasswordPage = null;
    }

    @ParameterizedTest()
    @CsvFileSource(
            resources = "/forgot_password_email_input.csv",
            numLinesToSkip = 1,
            emptyValue = "''"
    )
    @DisplayName("Verify that error message is shown when email field is left empty")
    public void errorIsShownWhenEmailIsEmpty(String email, String expectedError) {
        forgotPasswordPage.clickSubmitBtn();
        String actualError = forgotPasswordPage.getEmailErrorMsg();
        assertEquals(expectedError, actualError, "Incorrect error for email input field on Forgot Password page");
    }
}



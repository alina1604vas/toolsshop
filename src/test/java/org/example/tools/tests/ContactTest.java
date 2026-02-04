package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.ContactPage;
import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@EnabledForSprint(4)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactTest extends BaseTest {

    private ContactPage contactPage;

    @BeforeAll
    public void setContactPage() {
        contactPage = new ContactPage(driver).open();
    }

    @AfterEach
    public void refresh() {
        contactPage.refresh();
    }

    @AfterAll
    public void cleanUp() {
        contactPage = null;
    }

    //add param tests
    @Test
    @Tag("sprint1")
    @DisplayName("Contact form can be submitted successfully")
    public void testSuccessfulContactFormSubmission() {
        contactPage
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setEmailAddress(faker.internet().emailAddress())
                .setRandomSubject()
                .setMessage(faker.lorem().characters(50))
                .submitContactForm();

        String expectedSuccessMessage = "Thanks for your message! We will contact you shortly.";
        String actualSuccessMessage = contactPage.getSuccessMessage();

        assertEquals(expectedSuccessMessage, actualSuccessMessage);
    }
    //add param tests
    @Test
    @Tag("sprint1")
    @DisplayName("Message field length should be more than 50")
    public void testMessageFieldLengthErrorMessage() {
        contactPage.setMessage(faker.lorem().characters(49));
        String expectedErrorMessage = "Message must be minimal 50 characters";
        contactPage.submitContactForm();

        boolean isErrorMessagePresent = contactPage.isMessageLengthErrorContainerPresent();
        assertTrue(isErrorMessagePresent, "Subject length error message is absent");

        String actualErrorMessage = contactPage.getMessageLengthError();
        assertEquals(expectedErrorMessage, actualErrorMessage, "Message length error message is incorrect");
    }
    //add param tests
    @Test
    @Tag("sprint1")
    @DisplayName("Subject should be required")
    public void testSubjectIsRequiredErrorMessage() {
        String expectedErrorMessage = "Subject is required";
        contactPage.submitContactForm();

        boolean isErrorMessagePresent = contactPage.isEmptySubjectErrorContainerPresent();
        assertTrue(isErrorMessagePresent, "Subject is required error message is absent");

        String actualErrorMessage = contactPage.getEmptySubjectError();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }
    //add param tests
    @Test
    @Tag("sprint1")
    @DisplayName("Message should be required")
    public void testMessageIsRequiredErrorMessage() {
        String expectedMessage = "Message is required";
        contactPage.submitContactForm();

        boolean isMessageErrorPresent = contactPage.isEmptyMessageErrorContainerPresent();
        assertTrue(isMessageErrorPresent, "Message is required error is absent");

        String actualMessage = contactPage.getEmptyMessageError();
        assertEquals(expectedMessage, actualMessage);
    }
    //add param tests
    @Test
    @Tag("sprint1")
    @DisplayName("Error is displayed for invalid email format")
    public void testEmailFormatValidation() {
        String expectedMessage = "Email format is invalid";

        String validEmail = faker.internet().emailAddress();
        Random random = new Random();

        int caseType = random.nextInt(3);
        String invalidEmail = switch (caseType) {
            case 0 -> validEmail.replace("@", "");
            case 1 -> validEmail.replace(".", "");
            case 2 -> "@" + faker.lorem().word() + ".com";
            default -> "plainaddress";
        };

        contactPage.setEmailAddress(invalidEmail);
        contactPage.submitContactForm();

        boolean isErrorMessagePresent = contactPage.isEmailErrorContainerPresent();
        assertTrue(isErrorMessagePresent, "Error message is absent = " + invalidEmail);

        String actualMessage = contactPage.getInvalidEmailErrorMessage();
        assertEquals(expectedMessage, actualMessage, "Expected and actual error messages do not coincide");
    }

}

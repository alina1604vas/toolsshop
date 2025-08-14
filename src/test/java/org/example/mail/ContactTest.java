package org.example.mail;

import org.example.mail.pageobject.ContactPage;
import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContactTest extends BaseTest {

    private static ContactPage contactPage;

    @BeforeAll
    public static void setContactPage() {
        contactPage = new ContactPage(driver).open();
    }

    @AfterEach
    public void refresh() {
        contactPage.refresh();
    }

    @AfterAll
    public static void cleanUp() {
        contactPage = null;
    }

    @Test
    @Tag("sprint1")
    @DisplayName("Contact form can be submitted successfully")
    public void testSuccessfulContactFormSubmission() {
        contactPage
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setEmailAddress(faker.internet().emailAddress())
                .setSubject()
                .setMessage(faker.lorem().characters(50))
                .submitContactForm();
        String expectedSuccessMessage = "Thanks for your message! We will contact you shortly.";
        String actualSuccessMessage = contactPage.getSuccessMessage();
        assertEquals(expectedSuccessMessage, actualSuccessMessage);
    }

    @Test
    @Tag("sprint1")
    @DisplayName("Subject length should be more than 50")
    public void testSubjectLength() {
        contactPage.setMessage(faker.lorem().characters(49));
        String expectedErrorMessage = "Message must be minimal 50 characters";
        contactPage.submitContactForm();
        String actualErrorMessage = contactPage.getMessageLengthError();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    @Tag("sprint1")
    @DisplayName("Subject should be required")
    public void testSubjectIsRequired() {
        String expectedErrorMessage = "Subject is required";
        contactPage.submitContactForm();
        String actualErrorMessage = contactPage.getEmptySubjectError();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    @Tag("sprint1")
    @DisplayName("Message should be required")
    public void testMessageIsRequired() {
        String expectedMessage = "Message is required";
        contactPage.submitContactForm();
        String actualMessage = contactPage.getEmptyMessageError();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @Tag("sprint1")
    @DisplayName("Error is displayed for invalid email format")
    public void testEmailFormatValidation() {
        String expectedMessage = "Email format is invalid";

        String validEmail = faker.internet().emailAddress();
        Random random = new Random();

        int caseType = random.nextInt(5);
        String invalidEmail = switch (caseType) {
            case 0 -> validEmail.replace("@", "");
            case 1 -> validEmail.replace(".", "");
            case 2 -> validEmail + " ";
            case 3 -> "@" + faker.lorem().word() + ".com";
            case 4 -> faker.lorem().characters(300) + "@test.com";
            default -> "plainaddress";
        };

        contactPage.setEmailAddress(invalidEmail);
        contactPage.submitContactForm();
        String actualMessage = contactPage.getInvalidEmailErrorMessage();
        assertEquals(expectedMessage, actualMessage);
    }

}

package org.example.mail;

import org.example.mail.pageobject.Header;
import org.junit.jupiter.api.*;

import static org.example.mail.BaseTest.driver;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HeaderTest {
    private static Header header;

    @BeforeAll
    public static void setUpHeader() {
        header = new Header(driver);
    }

    @AfterAll
    public static void cleanUp() {
        header = null;
    }

    @Test
    @DisplayName("Home is visible in the header")
    public void testIfHomeIsVisible() {
        assertTrue(header.isHomeVisible(), "Home should be visible in the header");
    }

    @Test
    @DisplayName("Categories is visible in the header")
    public void testIfCategoriesIsVisible() {
        assertTrue(header.isCategoriesVisible(), "Categories should be visible in the header");
    }

    @Test
    @DisplayName("Contact is visible in the header")
    public void testIfContactIsVisible() {
        assertTrue(header.isContactVisible(), "Contact should be visible in the header");
    }

}

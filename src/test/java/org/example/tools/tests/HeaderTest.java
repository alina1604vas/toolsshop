package org.example.tools.tests;

import org.example.tools.pageobject.Header;
import org.example.tools.pageobject.HomePage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HeaderTest extends BaseTest {

    private Header header;
    private HomePage homePage;

    @BeforeEach
    public void setUpHeader() {
        homePage = new HomePage(driver);
        homePage.open();
        homePage.waitUntilPageIsLoaded();
        header = new Header(driver);
    }

    @AfterEach
    public void cleanUp() {
        header = null;
        homePage = null;
    }

    @Test
    @Tag("smoke")
    @DisplayName("Home is visible in the header")
    public void testIfHomeIsVisible() {
        assertTrue(header.isHomeVisible(), "Home should be visible in the header");
    }

    @Test
    @Tag("sprint1")
    @DisplayName("Categories is visible in the header")
    public void testIfCategoriesIsVisible() {
        assertTrue(header.isCategoriesVisible(), "Categories should be visible in the header");
    }

    @Test
    @Tag("sprint1")
    @DisplayName("Contact is visible in the header")
    public void testIfContactIsVisible() {
        assertTrue(header.isContactVisible(), "Contact should be visible in the header");
    }

}

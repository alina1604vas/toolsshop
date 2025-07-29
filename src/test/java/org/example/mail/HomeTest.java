package org.example.mail;

import org.example.mail.entity.ProductCard;
import org.example.mail.pageobject.HomePage;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HomeTest extends BaseTest {

    private static HomePage homePage;

    @BeforeAll
    public static void setUpHomePage() {
        homePage = new HomePage(driver);
    }

    @AfterAll
    public static void cleanUp() {
        homePage = null;
    }

    @Test
    @DisplayName("Check Home page title")
    public void testHomePageTitle() {
        String expectedTitle = "Practice Software Testing - Toolshop - v1.0";
        String actualTitle = driver.getTitle();
        assertEquals(expectedTitle, actualTitle, "Home page title is incorrect");
    }

    @Test
    @DisplayName("Check Home Page url")
    public void testHomePageURL() {
        String expectedUrl = "https://v1.practicesoftwaretesting.com/#/";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl, "Home Page URLs do not match");
    }

    @Test
    @DisplayName("Check number of products on Home page")
    public void testNumberOfProducts() {
        int expectedNumber = 26;
        int actualNumber = homePage.getNumberOfProducts();
        assertEquals(expectedNumber, actualNumber, "Incorrect number of products on Home page");
    }

    @Test
    @DisplayName("Check name, image and price of cards")
    public void testProductCards() {
        ArrayList<ProductCard> products = homePage.getAllProducts();
        for (ProductCard card : products) {
            String cardName = card.getName();
            String cardImage = card.getImage();
            String cardPrice = card.getPrice();
            assertNotNull(cardImage, "Image is empty");
            assertNotNull(cardName, "Name is empty");
            assertNotNull(cardPrice, "Price is empty");
        }
    }

}

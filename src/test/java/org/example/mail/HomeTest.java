package org.example.mail;

import org.example.mail.entity.ProductCard;
import org.example.mail.network.api.Endpoints;
import org.example.mail.network.entity.*;
import org.example.mail.pageobject.HomePage;
import org.example.mail.utils.network.ChromeResponseListener;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HomeTest extends BaseTest {

    private static HomePage homePage;
    private static ChromeResponseListener responseListener = new ChromeResponseListener(devTools);

    private static HomeData homeData = new HomeData();

    @BeforeAll
    public static void setUpHomePage() {
        responseListener.subscribe(
                Endpoints.GET_BRANDS,
                Brand.class,
                brands -> homeData.setBrands(brands));

        responseListener.subscribe(
                Endpoints.GET_CATEGORIES,
                Category.class,
                categories -> homeData.setCategories(categories));

        responseListener.subscribe(
                Endpoints.GET_PRODUCTS,
                ProductsPerPage.class,
                products -> homeData.setProductsPerPage(products),
                "");

        homePage = new HomePage(driver);
    }

    @AfterAll
    public static void cleanUp() {
        responseListener.unSubscribe();
        homePage = null;
    }


    //    TODO: make 2nd part dynamic
    @Test
    @Tag("sprint1")
    @DisplayName("Check Home page title")
    public void testHomePageTitle() {
        String expectedTitle = "Practice Software Testing - Toolshop - v1.0";
        String actualTitle = driver.getTitle();
        assertEquals(expectedTitle, actualTitle, "Home page title is incorrect");
    }

    //TODO: make version part dynamic
    @Test
    @Tag("sprint1")
    @DisplayName("Check Home Page url")
    public void testHomePageURL() {
        String expectedUrl = "https://v1.practicesoftwaretesting.com/#/";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl, "Home Page URLs do not match");
    }


    //TODO: remove hardcoded values
    @Test
    @Tag("sprint1")
    @DisplayName("Check number of products on Home page")
    public void testNumberOfProducts() {
        int expectedNumber = 26;
        int actualNumber = homePage.getNumberOfProducts();
        assertEquals(expectedNumber, actualNumber, "Incorrect number of products on Home page");
    }

    @Test
    @Tag("sprint1")
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

    @Test
    @Tag("sprint2")
    @DisplayName("Check if a given category name is present on Home page")
    public void testCategoriesPresentInTheFilter() {
        List<Category> categories = homeData.getCategories();

        for (Category category : categories) {
            String categoryName = category.getName();
            boolean result = homePage.isCategoryPresent(categoryName);
            assertTrue(result, String.format("%s is  not present on Home Page", categoryName));

            List<Category> subCategories = category.getSubCategories();
            if (subCategories != null && !subCategories.isEmpty()) {
                for (Category subCategory : subCategories) {
                    String subCategoryName = subCategory.getName();
                    boolean r = homePage.isCategoryPresent(subCategoryName);
                    assertTrue(r, String.format("%s is  not present on Home Page", subCategoryName));
                }
            }
        }
    }

    @Test
    @Tag("sprint2")
    @DisplayName("Check if a given brand name is present on Home page")
    public void testBrandPresentInTheFilter() {
        List<Brand> brands = homeData.getBrands();

        for (Brand brand : brands) {
            String brandName = brand.getName();
            boolean result = homePage.isBrandPresent(brandName);
            assertTrue(result, String.format("%s is  not present on Home Page", brandName));
        }
    }

    //TODO: get rid of locators the test method
    @Test
    @Tag("sprint2")
    @DisplayName("Check that search results contain the search term in product names")
    public void testSearchByValue() {
        String searchTerm = "hammer";
        homePage.searchByValue(searchTerm);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div[data-test='search_completed'] .card")));

        ArrayList<ProductCard> products = homePage.getAllProducts();
        for (ProductCard product : products) {
            boolean isNamePresent = product.getName().toLowerCase().contains(searchTerm.toLowerCase());
            assertTrue(isNamePresent, "Searched value is not present in the results");
        }
    }


    //TODO: what about pagination?
    @Test
    @Tag("sprint2")
    @DisplayName("Check sorting of products by alphabet A-Z")
    public void testSortingByAlphabetAZ() {
        homePage.sortAZ();

        ArrayList<ProductCard> products = homePage.getAllProducts();

        ArrayList<String> actualNames = new ArrayList<>();
        for (ProductCard product : products) {
            String actualName = product.getName();
            actualNames.add(actualName);
        }

        ArrayList<String> sortedNames = new ArrayList<>(actualNames);
        Collections.sort(sortedNames);

        assertEquals(sortedNames, actualNames, "Products are not sorted alphabetically A-Z");
    }

    @Test
    @Tag("sprint2")
    @DisplayName("Check sorting of products by alphabet Z-A in reverse order")
    public void testSortingByAlphabetZA() {
        homePage.sortZA();

        ArrayList<ProductCard> products = homePage.getAllProducts();

        ArrayList<String> actualNames = new ArrayList<>();
        for (ProductCard product : products) {
            String actualName = product.getName();
            actualNames.add(actualName);
        }

        ArrayList<String> sortedNames = new ArrayList<>(actualNames);
        Collections.sort(sortedNames, Collections.reverseOrder());

        assertEquals(sortedNames, actualNames, "Products are not sorted alphabetically Z-A");
    }

    @Test
    @Tag("sprint2")
    @DisplayName("Check sorting of products by price from High to Low")
    public void testSortingByPriceHighToLow() {
        homePage.sortByPriceHighToLow();

        ArrayList<ProductCard> products = homePage.getAllProducts();

        ArrayList<Double> actualPrices = new ArrayList<>();
        for (ProductCard product : products) {
            String actualPrice = product.getPrice().replaceAll("[^\\d.]", "").replace(",", "");
            actualPrices.add(Double.parseDouble(actualPrice));
        }

        ArrayList<Double> sortedPrices = new ArrayList<>(actualPrices);
        sortedPrices.sort(Collections.reverseOrder());

        assertEquals(sortedPrices, actualPrices, "Products are not sorted by price from High to Low");

    }

    @Test
    @Tag("sprint2")
    @DisplayName("Check sorting of products by price from Low to High")
    public void testSortingByPriceLowToHigh() {
        homePage.sortByPriceLowToHigh();

        ArrayList<ProductCard> products = homePage.getAllProducts();

        ArrayList<Double> actualPrices = new ArrayList<>();
        for (ProductCard product : products) {
            String actualPrice = product.getPrice().replaceAll("[^\\d.]", "").replace(",", "");
            actualPrices.add(Double.parseDouble(actualPrice));
        }

        ArrayList<Double> sortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(sortedPrices);

        assertEquals(sortedPrices, actualPrices, "Products are not sorted by price from Low to High");
    }

}

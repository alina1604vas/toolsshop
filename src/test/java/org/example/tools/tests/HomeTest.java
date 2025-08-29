package org.example.tools.tests;

import com.google.gson.reflect.TypeToken;
import org.example.tools.SystemConfig;
import org.example.tools.network.api.Endpoints;
import org.example.tools.network.entity.Brand;
import org.example.tools.network.entity.Category;
import org.example.tools.network.entity.HomeData;
import org.example.tools.network.entity.ProductsPerPage;
import org.example.tools.pageobject.HomePage;
import org.example.tools.pageobject.entity.UiProduct;
import org.example.tools.infra.EnabledForSprint;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@EnabledForSprint(3)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HomeTest extends BaseTest {

    private HomePage homePage;
    private HomeData homeData = new HomeData();

    @BeforeAll
    public void setUpHomePage() {
        responseListener.subscribe(
                Endpoints.GET_BRANDS,
                TypeToken.getParameterized(List.class, Brand.class).getType(),
                response -> homeData.setBrands((List<Brand>) response));

        responseListener.subscribe(
                Endpoints.GET_CATEGORIES,
                TypeToken.getParameterized(List.class, Category.class).getType(),
                response -> homeData.setCategories((List<Category>) response));

        responseListener.subscribe(
                Endpoints.GET_PRODUCTS,
                TypeToken.get(ProductsPerPage.class).getType(),
                response -> {
                    homeData.setProductsPerPage((ProductsPerPage) response);
                });

        homePage = new HomePage(driver).open();
    }

    @AfterAll
    public void cleanUp() {
        responseListener.unSubscribe();
        homePage = null;
    }

    //    TODO: make 2nd part dynamic - done
    @Test
    @Tag("sprint1")
    @DisplayName("Check Home page title")
    public void testHomePageTitle() {
        String expectedTitle = "Practice Software Testing - Toolshop";
        String actualTitle = homePage.getHomePageTitle();
        assertEquals(expectedTitle, actualTitle, "Home page title is incorrect");
    }

    //TODO: make version part dynamic -done
    @Test
    @Tag("sprint1")
    @DisplayName("Check Home Page url")
    public void testHomePageURL() {
        String expectedUrl = SystemConfig.getBaseUrl();
        String actualUrl = homePage.getHomePageURl();
        assertEquals(expectedUrl, actualUrl, "Home Page URLs do not match");
    }

    //TODO: remove hardcoded values
    @Test
    @Tag("sprint1")
    @DisplayName("Check number of products on Home page")
    public void testNumberOfProducts() {
        //int expectedNumber = 26;
        //int expectedNumberOfProducts = homeData.getProductsPerPage().getTotalProducts();
        //int actualNumberOfProducts = homePage.getNumberOfProducts();
        //assertEquals(expectedNumberOfProducts, actualNumberOfProducts, "Incorrect number of products on Home page");
    }

    @Test
    @Tag("sprint1")
    @DisplayName("Check name, image and price of cards")
    public void testProductCards() {
        ArrayList<UiProduct> products = homePage.getAllProducts();
        for (UiProduct card : products) {
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
            String brandName = brand.getBrandName();
            boolean result = homePage.isBrandPresent(brandName);
            assertTrue(result, String.format("%s is  not present on Home Page", brandName));
        }
    }

    @ParameterizedTest
    @Tag("sprint2")
    @ValueSource(strings = {"hammer", "screwdriver", "pliers", "wrench"})
    @DisplayName("Check that search results contain the search term in product names")
    public void testSearchByValue(String searchTerm) {
        homePage.searchByValue(searchTerm);

        ArrayList<UiProduct> products = homePage.getAllProducts();
        for (UiProduct product : products) {
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

        ArrayList<UiProduct> products = homePage.getAllProducts();

        ArrayList<String> actualNames = new ArrayList<>();
        for (UiProduct product : products) {
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

        ArrayList<UiProduct> products = homePage.getAllProducts();

        ArrayList<String> actualNames = new ArrayList<>();
        for (UiProduct product : products) {
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

        ArrayList<UiProduct> products = homePage.getAllProducts();

        ArrayList<Double> actualPrices = new ArrayList<>();
        for (UiProduct product : products) {
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

        ArrayList<UiProduct> products = homePage.getAllProducts();

        ArrayList<Double> actualPrices = new ArrayList<>();
        for (UiProduct product : products) {
            String actualPrice = product.getPrice().replaceAll("[^\\d.]", "").replace(",", "");
            actualPrices.add(Double.parseDouble(actualPrice));
        }

        ArrayList<Double> sortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(sortedPrices);

        assertEquals(sortedPrices, actualPrices, "Products are not sorted by price from Low to High");
    }

}

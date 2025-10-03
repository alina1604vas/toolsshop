package org.example.tools.tests;

import com.google.gson.reflect.TypeToken;
import org.awaitility.Awaitility;
import org.example.tools.infra.EnabledForSprint;
import org.example.tools.network.api.Endpoints;
import org.example.tools.network.entity.Brand;
import org.example.tools.network.entity.Category;
import org.example.tools.network.entity.Product;
import org.example.tools.network.entity.ProductsPerPage;
import org.example.tools.pageobject.HomePage;
import org.example.tools.pageobject.ProductPage;
import org.example.tools.pageobject.entity.UiProduct;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v136.layertree.model.StickyPositionConstraint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@EnabledForSprint(3)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductPageTest extends BaseTest {

    private HomePage homePage = new HomePage(driver);
    private ProductPage productPage = new ProductPage(driver);

    private Product expectedProduct = null;
    private List<Product> expectedRelatedProducts = null;

    @BeforeAll
    public void setUpProductPage() {
        synchronized (this) {

            responseListener.addObserver(
                    Endpoints.GET_PRODUCT,
                    TypeToken.get(Product.class).getType(),
                    response -> expectedProduct = (Product) response);

            responseListener.addObserver(
                    Endpoints.GET_PRODUCT_RELATED,
                    TypeToken.getParameterized(List.class, Product.class).getType(),
                    response -> expectedRelatedProducts = (List<Product>) response);
        }

        homePage = new HomePage(driver).open();

    }

    @AfterEach
    public void reset() {
        homePage.open();
    }

    @AfterAll
    public void cleanUp() {
        responseListener.removeObserver(Endpoints.GET_PRODUCT);
        responseListener.removeObserver(Endpoints.GET_PRODUCT_RELATED);
        homePage = null;
        productPage = null;
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Check that Image, Name and Price coincide")
    public void testProductImageNamePrice() {
        UiProduct uiProduct = homePage.openRandomProduct();

        String expectedImage = uiProduct.getImage();
        String expectedName = uiProduct.getName();
        String expectedPrice = uiProduct.getPrice();

        productPage.waitUntilPageIsLoaded();

        String actualImage = productPage.getImage();
        String actualName = productPage.getName();
        String actualPrice = productPage.getPrice();

        assertEquals(expectedImage, actualImage, "Images do not coincide");
        assertEquals(expectedName, actualName, "Names do not coincide");
        assertEquals(expectedPrice, actualPrice, "Prices do not coincide");
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Check Product description")
    public void testProductDescription() {
        homePage.openRandomProduct();

        productPage.waitUntilPageIsLoaded();

        String actualDescription = productPage.getDescription();
        String expectedDescription = expectedProduct.getDescription();
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Check product category labels")
    public void testProductCategoryLabel() {
        homePage.openRandomProduct();

        productPage.waitUntilPageIsLoaded();

        String expectedCategoryLabel = expectedProduct.getCategory().getName();
        String actualCategoryLabel = productPage.getProductCategoryLabel();

        assertEquals(expectedCategoryLabel, actualCategoryLabel, "Category labels do not match");
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Check brand labels of a product")
    public void testProductBrandLabel() {
        homePage.openRandomProduct();

        productPage.waitUntilPageIsLoaded();

        String expectedBrandLabel = expectedProduct.getBrand().getBrandName();
        String actualBrandLabel = productPage.getProductBrandLabel();

        assertEquals(expectedBrandLabel, actualBrandLabel, "Brand labels do not coincide");
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Check related products")
    public void testRelatedProducts() {
        UiProduct uiProduct = homePage.openRandomProduct();

        productPage.waitUntilPageIsLoaded();

        List<UiProduct> actualRelatedProducts = productPage.getRelatedProducts();

        assertEquals(expectedRelatedProducts.size(), actualRelatedProducts.size(), "Number of related products does not match");

        for (Product expectedRelatedProduct : expectedRelatedProducts) {
            String expectedProductName = expectedRelatedProduct.getName();
            String expectedProductImage = expectedRelatedProduct.getProductImage().getFileName();

            UiProduct target = null;
            for (UiProduct actualProduct : actualRelatedProducts) {
                String actualProductName = actualProduct.getName();
                if (actualProductName.equals(expectedProductName)) {
                    target = actualProduct;
                    break;
                }
            }
            assertNotNull(target, "Related product " + expectedProductName + " is not present on a page");
            assertEquals(expectedProductName, target.getName(), "Names do not coincide");
            assertTrue(target.getImage().endsWith(expectedProductImage), "Images do not coincide");
        }

    }
    @Test
    @Tag("sprint3")
    @DisplayName("Check that a product can be added to a shopping cart")
    public void testAddToCart() {
        homePage.openRandomProduct();
        productPage.setButtonIncreaseQuantity(3);
        productPage.clickAddToCart();
        String actualMessage = productPage.confirmationAlertIsPresent();
        assertEquals("Product added to shopping cart.", actualMessage, "Shopping cart messages do not coincide");
    }
}




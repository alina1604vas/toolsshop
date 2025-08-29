package org.example.tools.tests;

import com.google.gson.reflect.TypeToken;
import org.example.tools.infra.EnabledForSprint;
import org.example.tools.network.api.Endpoints;
import org.example.tools.network.entity.Product;
import org.example.tools.pageobject.HomePage;
import org.example.tools.pageobject.ProductPage;
import org.example.tools.pageobject.entity.UiProduct;
import org.junit.jupiter.api.*;

import javax.xml.catalog.CatalogResolver;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnabledForSprint(3)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductPageTest extends BaseTest {

    private HomePage homePage = new HomePage(driver);
    private ProductPage productPage = new ProductPage(driver);

    private Product expectedProduct = null;

    @BeforeAll
    public void prepare() {
        homePage.open();
    }

    @AfterEach
    public void reset() {
        homePage.open();
    }

    @AfterAll
    public void cleanUp() {
        homePage = null;
        productPage = null;
        responseListener.unSubscribe();
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Check that Image, Name and Price coincide")
    public void testProductImageNamePrice() {
        UiProduct uiProduct = homePage.openRandomProduct();

        String expectedImage = uiProduct.getImage();
        String expectedName = uiProduct.getName();
        String expectedPrice = uiProduct.getPrice();

        String actualImage = productPage.getImage();
        String actualName = productPage.getName();
        String actualPrice = productPage.getPrice();

        assertEquals(expectedImage, actualImage, "Images do not coincide");
        assertEquals(expectedName, actualName, "Names do not coincide");
        assertEquals(expectedPrice, actualPrice, "Prices do not coincide"); //to cut of currency sign
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Check Product description")
    public void testProductDescription() {
        responseListener.subscribe(
                Endpoints.GET_PRODUCT,
                TypeToken.get(Product.class).getType(),
                response -> expectedProduct = (Product) response);

        UiProduct uiProduct = homePage.openRandomProduct();

        productPage.waitUntilPageIsLoaded();

        String actualDescription = productPage.getDescription();
        String expectedDescription = expectedProduct.getDescription();
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Check product category and brand labels")
    public void testProductCategoryLabel() {
        responseListener.subscribe(
                Endpoints.GET_PRODUCT,
                TypeToken.get(Product.class).getType(),
                response -> expectedProduct = (Product) response);

        UiProduct uiProduct = homePage.openRandomProduct();

        productPage.waitUntilPageIsLoaded();

        String expectedCategoryLabel = expectedProduct.getCategory().getName();
        String actualCategoryLabel = productPage.getProductCategoryLabel();

        assertEquals(expectedCategoryLabel, actualCategoryLabel, "Category labels do not match");
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Check brand labels of a product")
    public void testProductBrandLabel() {
        responseListener.subscribe(
                Endpoints.GET_PRODUCT,
                TypeToken.get(Product.class).getType(),
                response -> expectedProduct = (Product) response);

        UiProduct uiProduct = homePage.openRandomProduct();

        productPage.waitUntilPageIsLoaded();

        String expectedBrandLabel = expectedProduct.getBrand().getBrandName();
        String actualBrandLabel = productPage.getProductBrandLabel();

        assertEquals(expectedBrandLabel, actualBrandLabel, "Brand labels do not coincide");
    }

    @Test
    @Tag("sprint3")
    //Дописати
    @DisplayName("Check related products are present")
    public void testRelatedProductsArePresent() {
        responseListener.subscribe(
                Endpoints.GET_PRODUCT_RELATED,
                TypeToken.get(Product.class).getType(),
                response -> expectedProduct = (Product) response);

        UiProduct uiProduct = homePage.openRandomProduct();

        productPage.waitUntilPageIsLoaded();
    }
}




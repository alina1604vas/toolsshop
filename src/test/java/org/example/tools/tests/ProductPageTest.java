package org.example.tools.tests;

import com.google.gson.reflect.TypeToken;
import org.example.tools.infra.EnabledForSprint;
import org.example.tools.network.api.Endpoints;
import org.example.tools.network.entity.Product;
import org.example.tools.pageobject.HomePage;
import org.example.tools.pageobject.ProductPage;
import org.example.tools.pageobject.entity.UiProduct;
import org.junit.jupiter.api.*;

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
    @DisplayName("fggg")
    public void testProductAttributes() {
        UiProduct uiProduct = homePage.openRandomProduct();

        String expectedImage = uiProduct.getImage();
        String expectedName = uiProduct.getName();
        String expectedPrice = uiProduct.getPrice();

        String actualImage = productPage.getImage();
        String actualName = productPage.getName();
        String actualPrice = productPage.getPrice();

        assertEquals(expectedImage, actualImage, "Images do not coincide");
        assertEquals(expectedName, actualName, "Names do not coincide");
        assertEquals(expectedPrice, actualPrice, "Prices do not coincide");
    }

    @Test
    public void ggg() {
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

}

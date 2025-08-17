package org.example.mail;

import com.google.gson.reflect.TypeToken;
import org.example.mail.network.api.Endpoints;
import org.example.mail.network.entity.Product;
import org.example.mail.pageobject.HomePage;
import org.example.mail.pageobject.ProductPage;
import org.example.mail.pageobject.entity.UiProduct;
import org.example.mail.utils.network.ChromeResponseListener;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductPageTest extends BaseTest {

    private static HomePage homePage = new HomePage(driver);
    private static ProductPage productPage = new ProductPage(driver);
    private static final ChromeResponseListener responseListener = new ChromeResponseListener(devTools);

    private Product expectedProduct = null;

    @BeforeAll
    public static void prepare() {
        homePage.open();
    }

    @AfterEach
    public void reset() {
        homePage.open();
    }

    @AfterAll
    public static void cleanUp() {
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

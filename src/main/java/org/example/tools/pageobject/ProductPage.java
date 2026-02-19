package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.example.tools.pageobject.entity.UiProduct;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ProductPage {

    private final String url = SystemConfig.getBaseUrl() + "product/";
    private WebDriver driver;

    @FindBy(css = ".figure-img.img-fluid")
    private WebElement productImage;

    @FindBy(xpath = "//h1[@data-test='product-name']")
    private WebElement productName;

    @FindBy(xpath = "//p/span[@aria-label='category']")
    private WebElement productCategory;

    @FindBy(xpath = "//p/span[@aria-label='brand']")
    private WebElement productBrand;

    @FindBy(css = "span[data-test='unit-price']")
    private WebElement productPrice;

    @FindBy(id = "description")
    private WebElement productDescription;

    @FindBy(id = "btn-decrease-quantity")
    private WebElement buttonDecreaseQuantity;


    @FindBy(xpath = "//input[@data-test='quantity']")
    private WebElement productQuantity;

    @FindBy(id = "btn-add-to-cart")
    private WebElement buttonAddToCart;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void openProductPage(String id) {
        driver.get(url + id);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("description")));
    }

    public void waitUntilPageIsLoaded() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("description")));
    }

    public String getImage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(productImage));

        return productImage.getAttribute("src");
    }

    public String getName() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@data-test='product-name']")));

        return productName.getText();
    }

    public String getPrice() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span[data-test='unit-price']")));

        return productPrice.getText();
    }

    public String getDescription() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("description")));

        return productDescription.getText();
    }

    public String getProductCategoryLabel() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(productCategory));

        return productCategory.getText();
    }

    public String getProductBrandLabel() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(productBrand));

        return productBrand.getText();

    }

    public List<UiProduct> getRelatedProducts() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> relatedWebElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".container .card")));
        ArrayList<UiProduct> relatedProducts = new ArrayList<>();

        for (int i = 0; i < relatedWebElements.size(); i++) {
            WebElement card = relatedWebElements.get(i);
            String relatedProductCardTitle = card.findElement(By.cssSelector(".card-title")).getText();
            String relatedProductCardImage = card.findElement(By.cssSelector(".card-img-top")).getAttribute("src");
            relatedProducts.add(UiProduct.withImage(relatedProductCardTitle, relatedProductCardImage));
        }
        return relatedProducts;
    }

    public void setButtonIncreaseQuantity(int desiredQuantity) {
        WebElement qtyInput = driver.findElement(By.xpath("//input[@data-test='quantity']"));
        int initialQty = Integer.parseInt(qtyInput.getAttribute("value"));
        int clicksNeeded = desiredQuantity - initialQty;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        for (int i = 0; i < clicksNeeded; i++) {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("btn-increase-quantity"))
            ).click();
            int expectedQty = initialQty + i + 1;
            wait.until(d -> Integer.parseInt(qtyInput.getAttribute("value")) == expectedQty);
        }
    }

    public void clickAddToCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("btn-add-to-cart"))
        );
        button.click();
    }
    public void waitForAddToCartToast() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        By toast = By.id("toast-container");

        wait.until(ExpectedConditions.visibilityOfElementLocated(toast));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(toast));
    }

    public String getShoppingSuccessMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Product added to shopping cart')]")));
        String message = toast.getText();
        return message;
    }

    public void clickShoppingCartIcon() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement cart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='nav-link' and @href='#/checkout']")));
        cart.click();
    }

    public int getItemsQtyInCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement qtyIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lblCartCount")));
        String qtyText = qtyIcon.getText();
        return Integer.parseInt(qtyText);
    }

}

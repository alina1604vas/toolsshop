package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

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

    @FindBy(id = "btn-increase-quantity")
    private WebElement buttonIncreaseQuantity;

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
        WebDriverWait wait =  new WebDriverWait(driver,Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(productCategory));

        return productCategory.getText();
    }

    public String getProductBrandLabel() {
        WebDriverWait wait =  new WebDriverWait(driver,Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(productBrand));

        return productBrand.getText();

    }

//    public ArrayList<UiProduct> getRelatedProducts() {
//
//    }
}

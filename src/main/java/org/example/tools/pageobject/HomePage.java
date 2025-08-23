package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.example.tools.pageobject.entity.UiProduct;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomePage {

    private static final Logger log = LoggerFactory.getLogger(HomePage.class);
    private final String url = SystemConfig.getBaseUrl();
    private WebDriver driver;

    @FindBy(id = "Layer_1")
    private WebElement logo;

    @FindBy(xpath = "//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]")
    private List<WebElement> products;

    @FindBy(css = "img.card-img-top")
    private WebElement cardImage;

    @FindBy(css = "[data-test='product-name']")
    private WebElement cardTitle;

    @FindBy(css = "[data-test='product-price']")
    private WebElement cardPrice;

    @FindBy(className = "form-select")
    private WebElement sortDropdown;

    @FindBy(xpath = "//input[@data-test='search-query']")
    private WebElement searchInputField;

//    @FindBy(xpath = "//button[@type='submit']")
//    private WebElement searchButton;

    @FindBy(id = "filters")
    private WebElement filters;

    @FindBy(css = "[data-test='search-submit']")
    private WebElement buttonSearch;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public HomePage open() {
        driver.get(url);
        PageFactory.initElements(driver, this);
        return this;
    }

    public ArrayList<UiProduct> getAllProducts() {
        String productsXpath = "//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(productsXpath)));
        List<WebElement> productElements = driver.findElements(By.xpath(productsXpath));

        ArrayList<UiProduct> result = new ArrayList<>();
        for (WebElement element : productElements) {
            String image = element.findElement(By.cssSelector("img.card-img-top")).getAttribute("src");
            String name = element.findElement(By.cssSelector("[data-test='product-name']")).getText();
            String price = element.findElement(By.cssSelector("[data-test='product-price']")).getText();

            result.add(new UiProduct(name, image, price));
        }
        return result;
    }


    //TODO: get rid of products
    public int getNumberOfProducts() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]"), 0));
        return products.size();
    }

    public boolean isCategoryPresent(String categoryName) {
        WebElement f = driver.findElement(By.id("filters"));

        try {
            f.findElement(By.xpath(".//label[contains(text(), '" + categoryName + "')]"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isBrandPresent(String brandName) {
        WebElement b = driver.findElement(By.id("filters"));

        try {
            b.findElement(By.xpath(".//label[contains(text(), '" + brandName + "')]"));
            return true;
        } catch (java.util.NoSuchElementException e) {
            return false;
        }
    }

    public void searchByValue(String value) {
        searchInputField.sendKeys(value);
        buttonSearch.click();
    }

    //TODO: get rid of locators
    public void sortAZ() {
        WebElement firstProductBeforeSort = driver.findElement(
                By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]")
        );

        Select selectOption = new Select(sortDropdown);
        selectOption.selectByVisibleText("Name (A - Z)");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.stalenessOf(firstProductBeforeSort));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]")
        ));
    }

    //TODO: get rid of locators
    public void sortZA() {
        WebElement firstProductBeforeSort = driver.findElement(
                By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]")
        );

        Select selectOption = new Select(sortDropdown);
        selectOption.selectByVisibleText("Name (Z - A)");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.stalenessOf(firstProductBeforeSort));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]")
        ));
    }

    //TODO: get rid of locators
    public void sortByPriceHighToLow() {
        WebElement firstProductBeforeSort = driver.findElement(
                By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]")
        );

        Select selectOption = new Select(sortDropdown);
        selectOption.selectByVisibleText("Price (High - Low)");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.stalenessOf(firstProductBeforeSort));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]")
        ));
    }

    //TODO: get rid of locators
    public void sortByPriceLowToHigh() {
        WebElement firstProductBeforeSort = driver.findElement(
                By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]")
        );

        Select selectOption = new Select(sortDropdown);
        selectOption.selectByVisibleText("Price (Low - High)");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.stalenessOf(firstProductBeforeSort));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]")
        ));
    }

    public UiProduct openRandomProduct() {
        ArrayList<UiProduct> products = getAllProducts();

        Random random = new Random();
        int randomIndex = random.nextInt(products.size());
        UiProduct randomUIProduct = products.get(randomIndex);

        WebElement productElement = driver.findElement(By.xpath(
                "//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]" +
                        "[.//*[@data-test='product-name' and normalize-space(text())='" + randomUIProduct.getName() + "']]"
        ));
        productElement.click();

        return randomUIProduct;
    }

//    public String getRandomProductId() {
//        openRandomProduct();
//        String currentUrl = driver.getCurrentUrl();
//        String[] parts = currentUrl.split("/product/");
//        return parts[1];
//    }

}

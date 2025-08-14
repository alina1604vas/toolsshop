package org.example.mail.pageobject;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.mail.entity.ProductCard;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomePage {

    private final String url = Dotenv.load().get("BASE_URL") + "/#";
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
        driver.get(url);
        PageFactory.initElements(driver, this);
    }

    public ArrayList<ProductCard> getAllProducts() {
        List<WebElement> productElements = driver.findElements(
                By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]")
        );
        ArrayList<ProductCard> result = new ArrayList<>();

        for (WebElement element : productElements) {

            String image = element.findElement(By.cssSelector("img.card-img-top")).getAttribute("src");
            String name = element.findElement(By.cssSelector("[data-test='product-name']")).getText();
            String price = element.findElement(By.cssSelector("[data-test='product-price']")).getText();

            result.add(new ProductCard(name, image, price));
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
            f.findElement(By.xpath(
                    ".//label[contains(text(), '" + categoryName + "')]"
            ));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isBrandPresent(String brandName) {
        WebElement b = driver.findElement(By.id("filters"));

        try {
            b.findElement(By.xpath(
                    ".//label[contains(text(), '" + brandName + "')]"
            ));
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
}

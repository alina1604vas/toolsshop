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

    @FindBy(className = "form-select")
    private WebElement sortDropdown;

    @FindBy(xpath = "//input[@data-test='search-query']")
    private WebElement searchInputField;

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
    public void waitUntilPageIsLoaded() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // increase timeout
        wait.until(driver -> {
            // wait for container to exist
            WebElement container = driver.findElement(By.cssSelector(".col-md-9 .container"));
            // wait for at least one product inside container
            List<WebElement> products = container.findElements(By.xpath(".//a[contains(@class,'card') and starts-with(@data-test,'product-')]"));
            return products.size() > 0;
        });
    }

    public String getHomePageTitle() {
        String title = driver.getTitle().trim();
        return title.replaceFirst("\\s-\\s*v[0-9.]+$", "").trim();
    }

    public String getHomePageURl() {
        return driver.getCurrentUrl();
    }
//чи вертає цей метод всы продукти зы всых сторынок??
    public ArrayList<UiProduct> getAllProducts() {
        String productsXpath = "//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(productsXpath)));
        List<WebElement> productElements = driver.findElements(By.xpath(productsXpath));

        ArrayList<UiProduct> result = new ArrayList<>();
        for (WebElement element : productElements) {
            String image = element.findElement(By.cssSelector("img.card-img-top")).getAttribute("src");
            String name = element.findElement(By.cssSelector("[data-test='product-name']")).getText();
            String price = element.findElement(By.cssSelector("[data-test='product-price']")).getText().replaceAll("[^0-9.,]", "");

            result.add(new UiProduct(name, image, price));
        }
        return result;
    }

    public int getTotalNumberOfProducts() {
        int totalProducts = 0;
        int numberOfPages = driver.findElements(By.xpath("//ul[contains(@class,'ngx-pagination')]/li[not(contains(@class,'pagination-previous')) and not(contains(@class,'pagination-next')) and not(contains(@class, 'small-screen'))]")).size();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        for (int i = 1; i <= numberOfPages; i++) {

            List<WebElement> oldProducts = driver.findElements(By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]"));

            String pageSelector = String.format("//ul[contains(@class,'ngx-pagination')]/li[not(contains(@class,'pagination-previous')) and not(contains(@class,'pagination-next')) and not(contains(@class, 'small-screen'))][%d]", i);
            WebElement pageLink = driver.findElement(By.xpath(pageSelector));
            pageLink.click();

            if (totalProducts != 0) {
                wait.until(ExpectedConditions.stalenessOf(oldProducts.get(0)));
            }

            List<WebElement> newProducts = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]")
                    )
            );
            totalProducts = totalProducts + newProducts.size();
        }

        return totalProducts;
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div[data-test='search_completed'] .card")));
    }

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

}

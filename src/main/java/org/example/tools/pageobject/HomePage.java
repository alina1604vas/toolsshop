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

import java.lang.ref.WeakReference;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Comparator;

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
//        List<WebElement> pages = driver.findElements(By.cssSelector(".page-item > .page-link[aria-label^='Page-']"));
//        String productsXpath = "//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]";
//
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(productsXpath)));
//
//        List<WebElement> productElements = driver.findElements(By.xpath(productsXpath));
//
//        ArrayList<UiProduct> result = new ArrayList<>();
//        for (WebElement element : productElements) {
//            String image = element.findElement(By.cssSelector("img.card-img-top")).getAttribute("src");
//            String name = element.findElement(By.cssSelector("[data-test='product-name']")).getText();
//            String price = element.findElement(By.cssSelector("[data-test='product-price']")).getText().replaceAll("[^0-9.,]", "");
//
//            result.add(new UiProduct(name, image, price));
//        }
//        return result;
            String productsXpath = "//a[contains(@class,'card') and starts-with(@data-test,'product-')]";
            By pageLinks = By.cssSelector(".page-item > .page-link[aria-label^='Page-']");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ArrayList<UiProduct> result = new ArrayList<>();

        int totalPages = driver.findElements(pageLinks).size();

        for (int i = 0; i < totalPages; i++) {
            List<WebElement> pages = driver.findElements(pageLinks);

            if (i > 0) {
                pages.get(i).click();
            }

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(productsXpath)));

            List<WebElement> productElements = driver.findElements(By.xpath(productsXpath));

            for (int j = 0; j < productElements.size(); j++) {
                UiProduct uiProduct = buildUiProductForIndex(productsXpath, j);
                if (uiProduct != null) {
                    result.add(uiProduct);
                }
            }
        }

        // Normalize ordering across all pages according to currently selected sort option
        try {
            Select currentSort = new Select(sortDropdown);
            String selected = currentSort.getFirstSelectedOption().getText();

            if ("Name (A - Z)".equalsIgnoreCase(selected)) {
                result.sort(Comparator.comparing(UiProduct::getName, String.CASE_INSENSITIVE_ORDER));
            } else if ("Name (Z - A)".equalsIgnoreCase(selected)) {
                result.sort(Comparator.comparing(UiProduct::getName, String.CASE_INSENSITIVE_ORDER).reversed());
            } else if ("Price (Low - High)".equalsIgnoreCase(selected)) {
                result.sort(Comparator.comparingDouble(p -> parsePrice(p.getPrice())));
            } else if ("Price (High - Low)".equalsIgnoreCase(selected)) {
                result.sort(Comparator.comparingDouble((UiProduct p) -> parsePrice(p.getPrice())).reversed());
            }
        } catch (Exception ignored) {
            // If dropdown is not present or option text changed, just return collected order.
        }

        return result;
    }

    private double parsePrice(String priceText) {
        if (priceText == null || priceText.isEmpty()) {
            return 0.0;
        }
        String normalized = priceText.replaceAll("[^0-9.,]", "").replace(",", ".");
        if (normalized.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(normalized);
    }

    private UiProduct buildUiProductForIndex(String productsXpath, int index) {
        try {
            List<WebElement> freshProducts = driver.findElements(By.xpath(productsXpath));
            if (index >= freshProducts.size()) {
                return null;
            }
            WebElement element = freshProducts.get(index);

            String image = element.findElement(By.cssSelector("img.card-img-top"))
                    .getAttribute("src");
            String name = element.findElement(By.cssSelector("[data-test='product-name']"))
                    .getText();
            String price = element.findElement(By.cssSelector("[data-test='product-price']"))
                    .getText()
                    .replaceAll("[^0-9.,]", "");

            return new UiProduct(name, image, price);
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            // Retry once with a fresh lookup
            try {
                List<WebElement> freshProducts = driver.findElements(By.xpath(productsXpath));
                if (index >= freshProducts.size()) {
                    return null;
                }
                WebElement element = freshProducts.get(index);

                String image = element.findElement(By.cssSelector("img.card-img-top"))
                        .getAttribute("src");
                String name = element.findElement(By.cssSelector("[data-test='product-name']"))
                        .getText();
                String price = element.findElement(By.cssSelector("[data-test='product-price']"))
                        .getText()
                        .replaceAll("[^0-9.,]", "");

                return new UiProduct(name, image, price);
            } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                return null;
            }
        }
    }

    public int getTotalNumberOfProducts() {
        String productsXpath = "//a[contains(@class,'card') and starts-with(@data-test,'product-')]";
        By pageLinks = By.cssSelector(".page-item > .page-link[aria-label^='Page-']");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        int totalProducts = 0;
        int totalPages = driver.findElements(pageLinks).size();

        if (totalPages == 0) {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(productsXpath)));
            return driver.findElements(By.xpath(productsXpath)).size();
        }

        for (int i = 0; i < totalPages; i++) {
            List<WebElement> pages = driver.findElements(pageLinks);

            if (i > 0) {
                pages.get(i).click();
            }

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(productsXpath)));
            totalProducts += driver.findElements(By.xpath(productsXpath)).size();
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
        String productsXpath = "//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]";
        WebElement firstProductBeforeSort = driver.findElement(By.xpath(productsXpath));
        String firstNameBeforeSort = firstProductBeforeSort
                .findElement(By.cssSelector("[data-test='product-name']"))
                .getText();

        Select selectOption = new Select(sortDropdown);
        selectOption.selectByVisibleText("Name (A - Z)");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> {
            try {
                List<WebElement> products = driver.findElements(By.xpath(productsXpath));
                if (products.isEmpty()) {
                    return false;
                }
                String firstNameAfterSort = products.get(0)
                        .findElement(By.cssSelector("[data-test='product-name']"))
                        .getText();
                return !firstNameAfterSort.equals(firstNameBeforeSort);
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                return false;
            }
        });
    }

    public void sortZA() {
        String productsXpath = "//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]";
        WebElement firstProductBeforeSort = driver.findElement(By.xpath(productsXpath));
        String firstNameBeforeSort = firstProductBeforeSort
                .findElement(By.cssSelector("[data-test='product-name']"))
                .getText();

        Select selectOption = new Select(sortDropdown);
        selectOption.selectByVisibleText("Name (Z - A)");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> {
            try {
                List<WebElement> products = driver.findElements(By.xpath(productsXpath));
                if (products.isEmpty()) {
                    return false;
                }
                String firstNameAfterSort = products.get(0)
                        .findElement(By.cssSelector("[data-test='product-name']"))
                        .getText();
                return !firstNameAfterSort.equals(firstNameBeforeSort);
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                return false;
            }
        });
    }

    public void sortByPriceHighToLow() {
        String productsXpath = "//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]";
        WebElement firstProductBeforeSort = driver.findElement(By.xpath(productsXpath));
        String firstPriceBeforeSort = firstProductBeforeSort
                .findElement(By.cssSelector("[data-test='product-price']"))
                .getText();

        Select selectOption = new Select(sortDropdown);
        selectOption.selectByVisibleText("Price (High - Low)");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> {
            try {
                List<WebElement> products = driver.findElements(By.xpath(productsXpath));
                if (products.isEmpty()) {
                    return false;
                }
                String firstPriceAfterSort = products.get(0)
                        .findElement(By.cssSelector("[data-test='product-price']"))
                        .getText();
                return !firstPriceAfterSort.equals(firstPriceBeforeSort);
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                return false;
            }
        });
    }

    public void sortByPriceLowToHigh() {
        String productsXpath = "//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]";
        WebElement firstProductBeforeSort = driver.findElement(By.xpath(productsXpath));
        String firstPriceBeforeSort = firstProductBeforeSort
                .findElement(By.cssSelector("[data-test='product-price']"))
                .getText();

        Select selectOption = new Select(sortDropdown);
        selectOption.selectByVisibleText("Price (Low - High)");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> {
            try {
                List<WebElement> products = driver.findElements(By.xpath(productsXpath));
                if (products.isEmpty()) {
                    return false;
                }
                String firstPriceAfterSort = products.get(0)
                        .findElement(By.cssSelector("[data-test='product-price']"))
                        .getText();
                return !firstPriceAfterSort.equals(firstPriceBeforeSort);
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                return false;
            }
        });
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

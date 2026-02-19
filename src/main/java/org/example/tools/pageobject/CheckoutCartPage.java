package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.example.tools.pageobject.entity.UiCartElement;
import org.example.tools.pageobject.entity.UiProduct;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.example.tools.pageobject.entity.UiProduct.withPrice;

public class CheckoutCartPage {

    private static final Logger log = LoggerFactory.getLogger(CheckoutCartPage.class);
    private final String url = SystemConfig.getBaseUrl() + "checkout";
    private WebDriver driver;

    //TODO: maybe add wait provider
    public CheckoutCartPage(WebDriver driver) {
        this.driver = driver;
    }

    public CheckoutCartPage open() {
        driver.get(url);
        PageFactory.initElements(driver, this);
        return this;
    }

    //TODO: refactor wait flow
    public List<UiCartElement> getUIProductsInCart() {
        List<UiCartElement> uiProducts = new ArrayList<>();

        waitForCart(productsExist -> {
            if (!productsExist) {
                return; // cart empty, leave list as empty
            }

            By tableLocator = By.cssSelector(".table");
            By rowsLocator = By.cssSelector(".table tbody tr");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));
            wait.until(ExpectedConditions.visibilityOfElementLocated(rowsLocator));

            List<WebElement> rows = driver.findElements(rowsLocator);
            for (WebElement row : rows) {
                String title = normalizeProductName(
                        row.findElement(By.cssSelector(".product-title")).getText()
                );
                String price = row.findElement(By.xpath(".//span[@data-test='product-price']"))
                        .getText()
                        .replace("$", "")
                        .trim();
                int qty = Integer.parseInt(
                        row.findElement(By.xpath(".//input[@data-test='product-quantity']"))
                                .getAttribute("value"));
                double subtotal = Double.parseDouble(
                        row.findElement(By.cssSelector("span[data-test='line-price']"))
                                .getText()
                                .replace("$", "")
                                .trim());

                UiProduct uiProduct = withPrice(title, price);
                UiCartElement cartElement = new UiCartElement(uiProduct, qty, subtotal);
                uiProducts.add(cartElement); // add to outer list
            }
        });

        return uiProducts;
//        waitForCart(
//                msg -> {
//                    By tableLocator = By.cssSelector(".table");
////        By rowsLocator = By.xpath("//tr[.//span[@data-test='product-title']]");
//                    By rowsLocator = By.cssSelector(".table tbody tr");
//
//                    // TODO check if table will show to user. Wait for table if so
//                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//                    wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));
//                    wait.until(ExpectedConditions.visibilityOfElementLocated(rowsLocator));
//
//                    List<WebElement> rows = driver.findElements(rowsLocator);
//                    List<UiCartElement> uiProducts = new ArrayList<>();
//                    for (WebElement row : rows) {
//                        String title = normalizeProductName(row.findElement(By.cssSelector(".product-title")).getText());
//                        String price = row.findElement(By.xpath(".//span[@data-test='product-price']"))
//                                .getText()
//                                .replace("$", "")
//                                .trim();
//                        int qty = Integer.parseInt(
//                                row.findElement(By.xpath(".//input[@data-test='product-quantity']"))
//                                        .getAttribute("value"));
//                        double subtotal = Double.parseDouble(
//                                row.findElement(By.cssSelector("span[data-test='line-price']"))
//                                        .getText()
//                                        .replace("$", "")
//                                        .trim());
//
//                        // TODO: refactor avoid static functions
//                        UiProduct uiProduct = withPrice(title, price);
//                        UiCartElement cartElement = new UiCartElement(uiProduct, qty, subtotal);
//                        uiProducts.add(cartElement);
//                    }
//
//                }
//        );
//
//        return null;
    }

    private static String normalizeProductName(String name) {
        return name == null ? null : name.replace('\u00A0', ' ').trim();
    }

    public double getCartTotal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement totalRow = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//td[@data-test='cart-total']")));

        String totalPrice = totalRow.getText().replace("$", "").trim();
        return Double.parseDouble(totalPrice);
    }

    public void deleteCartProduct(UiCartElement element) {
        String productName = element.getProduct().getName().trim();
        WebElement deleteButton = driver.findElement(By.xpath(
                "//tr[.//span[contains(@class,'product-title') and contains(normalize-space(), '" + productName + "')]]" +
                        "//a[contains(@class,'btn-danger')]"
        ));
        deleteButton.click();
    }

    public void clearCartIfNeeded() {
        waitForCart(areProductsPresent -> { if (areProductsPresent) clearCart(); } );
    }

    public void clearCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        Actions actions = new Actions(driver);

        By deleteButtonLocator = By.xpath(
                "//tr[.//span[@data-test='product-title']]//a[contains(@class,'btn-danger')]"
        );
        By toastLocator = By.xpath("//div[@aria-label='Product deleted.']");

        List<WebElement> buttons;
        while (!(buttons = driver.findElements(deleteButtonLocator)).isEmpty()) {
            WebElement deleteButton = buttons.get(0);
            WebElement row = deleteButton.findElement(By.xpath("./ancestor::tr"));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", deleteButton);
            actions.moveToElement(deleteButton)
                    .pause(Duration.ofMillis(200))
                    .click()
                    .perform();

            wait.until(ExpectedConditions.visibilityOfElementLocated(toastLocator));
            wait.until(ExpectedConditions.stalenessOf(row));
        }
    }

    public void waitForCart(Consumer<Boolean> onProductsExist) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        By tableLocator = By.cssSelector(".table");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));
            onProductsExist.accept(true);
        } catch (TimeoutException e) {
            onProductsExist.accept(false);
        }
    }

    public void clickProceedToCheckout() {
        By proceedToCheckoutBtn = By.cssSelector("button[data-test='proceed-1']");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckoutBtn));
        button.click();
    }

}

package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.example.tools.pageobject.entity.UiCartElement;
import org.example.tools.pageobject.entity.UiProduct;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.example.tools.pageobject.entity.UiProduct.withPrice;

public class CheckoutCartPage {
    private static final Logger log = LoggerFactory.getLogger(CheckoutCartPage.class);
    private final String url = SystemConfig.getBaseUrl() + "checkout";
    private WebDriver driver;

    @FindBy(css = ".table tbody tr")
    private List<WebElement> uniqueProducts;

    public CheckoutCartPage(WebDriver driver) {
        this.driver = driver;
    }

    public CheckoutCartPage open() {
        driver.get(url);
        PageFactory.initElements(driver, this);
        return this;
    }

    public List<UiCartElement> getUIProductsInCart() {
        List<UiCartElement> uiProducts = new ArrayList<>();
        for (WebElement row : uniqueProducts) {
            String title = row.findElement(By.cssSelector(".product-title")).getText();
            String price = row.findElement(By.cssSelector("td:nth-child(4)")).getText().replace("$", "").trim();
            int qty = Integer.parseInt(row.findElement(By.cssSelector("td:nth-child(3) input.quantity")).getAttribute("value"));
            double subtotal = Double.parseDouble(row.findElement(By.cssSelector("td:nth-child(5)")).getText().replace("$", "").trim());
            UiProduct uiProduct = withPrice(title, price);
            UiCartElement cartElement = new UiCartElement(uiProduct, qty, subtotal);
            uiProducts.add(cartElement);
        }
        return uiProducts;
    }

    public double getCartTotal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement totalRow = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".table tfoot tr td:nth-child(5)")));

        String totalPrice = totalRow.getText().replace("$", "").trim();
        return Double.parseDouble(totalPrice);
    }

    //add clear Cart (проклікувати всі красні)
    public void clearCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        By deleteButton = By.cssSelector(".btn-danger");

        while (!driver.findElements(deleteButton).isEmpty()) {
            driver.findElement(deleteButton).click();
            wait.until(ExpectedConditions.stalenessOf(driver.findElement(deleteButton)));
        }
    }
//    public void emptyCart() {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        List<WebElement> deleteButtons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".btn-danger")));
//
//        for (WebElement deleteButton : deleteButtons) {
//            deleteButton.click();
//        }
//    }
}


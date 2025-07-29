package org.example.mail.pageobject;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.mail.entity.ProductCard;
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

public class HomePage {

    private final String url = Dotenv.load().get("BASE_URL");
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

    public HomePage(WebDriver driver) {
        this.driver = driver;
        driver.get(url);
        PageFactory.initElements(driver, this);
    }

    public ArrayList<ProductCard> getAllProducts() {
        ArrayList<ProductCard> result = new ArrayList<>();
        for (WebElement element : products) {

            String image = element.findElement(By.cssSelector("img.card-img-top")).getAttribute("src");
            String name = element.findElement(By.cssSelector("[data-test='product-name']")).getText();
            String price = element.findElement(By.cssSelector("[data-test='product-price']")).getText();

            result.add(new ProductCard(name, image, price));
        }

        return result;
    }

    public int getNumberOfProducts() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.xpath("//a[contains(@class, 'card') and starts-with(@data-test, 'product-')]"), 0));
        return products.size();
    }

}

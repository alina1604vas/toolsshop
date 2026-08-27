import org.example.tools.SystemConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Header {

    //    private final String url = SystemConfig.getBaseUrl();
    private final WebDriver driver;

    @FindBy(linkText = "Home")
    private WebElement homeNavigationItem;

    @FindBy(css = ".nav-item.dropdown")
    private WebElement categoriesNavigationItem;

    @FindBy(linkText = "Contact")
    private WebElement contactNavigationItem;

    @FindBy(css = "button.navbar-toggler")
    private WebElement hamburgerToggler;

    //    public Header(WebDriver driver) {
//        driver.get(url);
//        PageFactory.initElements(driver, this);
//    }
    public Header(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    //    public boolean isHomeVisible() {
//        return homeNavigationItem.isDisplayed();
//    }
    private void openHamburgerMenuIfCollapsed() {
        List<WebElement> togglers = driver.findElements(By.cssSelector("button.navbar-toggler"));
        if (togglers.isEmpty() || !togglers.get(0).isDisplayed()) {
            return; // wide viewport - nav items already shown directly, nothing to expand
        }

        WebElement toggler = togglers.get(0);
        if ("false".equals(toggler.getAttribute("aria-expanded"))) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(toggler)).click();
            wait.until(d -> "true".equals(toggler.getAttribute("aria-expanded")));
        }
    }

    public boolean isHomeVisible() {
        openHamburgerMenuIfCollapsed();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        return wait.until(ExpectedConditions.visibilityOf(homeNavigationItem)).isDisplayed();
    }

    public boolean isCategoriesVisible() {
        openHamburgerMenuIfCollapsed();
        return categoriesNavigationItem.isDisplayed();
    }

    public boolean isContactVisible() {
        openHamburgerMenuIfCollapsed();
        return contactNavigationItem.isDisplayed();
    }

}

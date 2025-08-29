package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.example.tools.utils.TestUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ContactPage {

    private final String url = SystemConfig.getBaseUrl() + "contact";
    private WebDriver driver;

    @FindBy(id = "first_name")
    private WebElement firstName;

    @FindBy(id = "last_name")
    private WebElement lastName;

    @FindBy(id = "email")
    private WebElement emailAddress;

    @FindBy(id = "subject")
    private WebElement subject;

    @FindBy(id = "message")
    private WebElement message;

    @FindBy(className = "btnSubmit")
    private WebElement buttonSend;

    public ContactPage(WebDriver driver) {
        this.driver = driver;
    }

    public ContactPage open() {
        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first_name")));

        PageFactory.initElements(driver, this);
        return this;
    }

    public void refresh() {
        driver.navigate().refresh();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(firstName));
    }

    public ContactPage setFirstName(String name) {
        firstName.sendKeys(name);
        return this;
    }

    public ContactPage setLastName(String surname) {
        lastName.sendKeys(surname);
        return this;
    }

    public ContactPage setEmailAddress(String email) {
        emailAddress.sendKeys(email);
        return this;
    }

    public ContactPage setRandomSubject() {
        subject.click();
        Select select = new Select(subject);
        List<WebElement> subjectOptions = select.getOptions();
        int randomIndex = TestUtils.getRandomInt(1, subjectOptions.size() - 1);
        select.selectByIndex(randomIndex);
        return this;
    }

    public ContactPage setMessage(String messageText) {
        message.sendKeys(messageText);
        return this;
    }

    public boolean isMessageLengthErrorContainerPresent() {
        return isElementPresent(By.xpath("//div[contains(text(), 'Message must be minimal 50 characters')]"));
    }

    public String getMessageLengthError() {
        boolean isElementPresent = isElementPresent(
                By.xpath("//div[contains(text(), 'Message must be minimal 50 characters')]")
        );

        if (isElementPresent) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[contains(text(), 'Message must be minimal 50 characters')]"))
            ).getText();
        } else {
            return "";
        }
    }

    public ContactPage submitContactForm() {
        buttonSend.click();
        return this;
    }

    public boolean isEmptySubjectErrorContainerPresent() {
        return isElementPresent(By.id("subject_alert"));
    }

    public String getEmptySubjectError() {
        boolean isElementPresent = isElementPresent(By.id("subject_alert"));
        if (isElementPresent) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subject_alert"))).getText();
        } else {
            return "";
        }
    }

    public boolean isEmptyMessageErrorContainerPresent() {
        return isElementPresent(By.id("message_alert"));
    }

    public String getEmptyMessageError() {
        boolean isElementPresent = isElementPresent(By.id("message_alert"));
        if (isElementPresent) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message_alert"))).getText();
        } else {
            return "";
        }
    }

    private boolean isElementPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    public boolean isEmailErrorContainerPresent() {
        return isElementPresent(By.cssSelector("[data-test='email-error']"));
    }

    public String getInvalidEmailErrorMessage() {
        boolean isElementPresent = isElementPresent(By.cssSelector("[data-test='email-error']"));
        if (isElementPresent) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='email-error']")));
            return errorMessage.getText();
        } else {
            return "";
        }
    }

    public String getSuccessMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']"))).getText();
    }

}
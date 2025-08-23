package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class ContactPage {

    private final String url = SystemConfig.getBaseUrl() + "/contact";
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

    @FindBy(xpath = "//div[@role='alert']")
    private WebElement successMessage;

    @FindBy(id = "subject_alert")
    private WebElement subjectEmptyError;

    @FindBy(id = "message_alert")
    private WebElement messageEmptyError;

    @FindBy(xpath = "//div[contains(text(), 'Message must be minimal 50 characters')]")
    private WebElement messageLengthError;

    @FindBy(id = "email_alert")
    private WebElement emailFormatError;

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

    public ContactPage setSubject() {
        subject.click();
        Select select = new Select(subject);
        List<WebElement> subjectOptions = select.getOptions();
        int randomIndex = new Random().nextInt(subjectOptions.size());
        select.selectByIndex(randomIndex);
        return this;
    }

    public ContactPage setMessage(String messageText) {
        message.sendKeys(messageText);
        return this;
    }

    public String getMessageLengthError() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(messageLengthError));
        return messageLengthError.getText();
    }

    public ContactPage submitContactForm() {
        buttonSend.click();
        return this;
    }

    public String getEmptySubjectError() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(subjectEmptyError));
        return subjectEmptyError.getText();
    }

    public String getEmptyMessageError() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(messageEmptyError));
        return messageEmptyError.getText();
    }

    public String getInvalidEmailErrorMessage() {
        // TODO: find element dynamically

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(emailFormatError));
        return emailFormatError.getText();
    }

    public String getSuccessMessage() {
        return successMessage.getText();
    }

}

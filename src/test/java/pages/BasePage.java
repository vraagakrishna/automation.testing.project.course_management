package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.AlertUtils;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class BasePage {

    // <editor-fold desc="Class Fields / Constants">
    protected final AppiumDriver driver;

    protected final AlertUtils alertUtils;
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.alertUtils = new AlertUtils(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Protected Methods">
    protected WebElement getElement(By by) {
        return new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(visibilityOfElementLocated(by));
    }

    protected WebElement getElementOrThrow(By by, String elementName) {
        try {
            return getElement(by);
        } catch (TimeoutException ex) {
            throw new TimeoutException(elementName + " not displayed", ex);
        }
    }

    protected void clickButton(By by) {
        this.getElement(by)
            .click();
    }

    protected void verifyIfTextDisplayed(By by, String expectedMessage) {
        WebElement element = this.getElement(by);
        String actualMessage = element.getText();

        Assert.assertTrue(
                actualMessage.contains(expectedMessage),
                "Expected message: " + expectedMessage + ", but actual message: " + actualMessage
        );
    }

    protected void verifyIfTextDisplayedAnywhere(String expectedMessage) {
        boolean messageVisible = driver.findElements(By.xpath("//*[contains(text(), '" + expectedMessage + "')]"))
                                       .size() > 0;

        Assert.assertTrue(messageVisible, "Expected message not found on login page: " + expectedMessage);
    }

    protected void enterKeys(By by, Object keys) {
        WebElement element = this.getElement(by);
        element.click();
        element.clear();
        element.sendKeys((CharSequence) keys);
    }

    protected boolean isElementVisible(By by) {
        List<WebElement> elements = driver.findElements(by);
        return !elements.isEmpty() && elements.get(0)
                                              .isDisplayed();
    }

    protected WebElement waitForDropdownToHaveOptions(By by) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(d -> {
            WebElement dropdown = d.findElement(by);

            if (!dropdown.isEnabled()) return false;

            List<WebElement> options = new Select(dropdown).getOptions();

            if (options.size() <= 1) return false;

            String firstOptionText = options.get(0)
                                            .getText();

            return !firstOptionText.contains("Loading");
        });

        return driver.findElement(by);
    }

    protected String getSnackBarText() {
        Wait<AppiumDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(5))
                .pollingEvery(Duration.ofMillis(300));

        boolean popupDisplayed = wait.until(d ->
                d.getPageSource()
                 .contains("live-region=\"1\"")
        );

        if (!popupDisplayed)
            return null;

        String source = driver.getPageSource();

        Pattern pattern = Pattern.compile("content-desc=\"([^\"]*)\"[^>]*live-region=\"1\"");
        Matcher matcher = pattern.matcher(source);

        String popupText = null;
        if (matcher.find()) {
            popupText = matcher.group(1);
        }

        return popupText;
    }
    // </editor-fold>

}

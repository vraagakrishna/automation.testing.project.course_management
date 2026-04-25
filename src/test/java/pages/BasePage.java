package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AlertUtils;

import java.time.Duration;

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

    protected WebElement getElement(By by, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(visibilityOfElementLocated(by));
    }

    protected void clickButton(By by) {
        this.getElement(by)
            .click();
    }

    protected boolean verifyIfTextDisplayedAnywhere(String expectedMessage) {
        try {
            return driver.findElements(By.xpath("//*[contains(text(), '" + expectedMessage + "')]"))
                         .size() > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    protected void enterKeys(By by, Object keys) {
        WebElement element = this.getElement(by);

        String currentText = getElementText(element);

        if (currentText != null && currentText.equals(keys.toString()))
            return;

        // Re-fetch to avoid stale after attribute read
        element = this.getElement(by);

        element.click();
        element.clear();
        element.sendKeys((CharSequence) keys);
    }

    protected String getElementText(WebElement element) {
        return null;
    }
    // </editor-fold>

}

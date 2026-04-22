package pages.web;

import common.Constants;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import pages.BasePage;
import utils.ConfigManager;

import java.util.List;

public class BasePageWeb extends BasePage {

    // <editor-fold desc="Class Fields / Constants">
    private final By body = By.tagName("body");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public BasePageWeb(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Protected Methods">
    protected void scrollToViewThenClickButton(By by) {
        WebElement element = this.getElement(by, 50);
        scrollIntoView(element);
        clickButton(element);
    }

    @Override
    protected void clickButton(By by) {
        scrollToViewThenClickButton(by);
    }

    protected void clickButton(WebElement element) {
        if (ConfigManager.getPlatformName()
                         .equalsIgnoreCase(Constants.PLATFORM_IOS)) {
            element.click();  // better for Safari alert handling
            return;
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected String getSelectedOptionInDropdown(By by) {
        return new Select(getElement(by)).getFirstSelectedOption()
                                         .getText();
    }

    protected void verifyIfTextDisplayed(By by, String expectedMessage) {
        WebElement element = this.getElement(by);
        String actualMessage = element.getText();

        Assert.assertTrue(
                actualMessage.contains(expectedMessage),
                "Expected message: " + expectedMessage + ", but actual message: " + actualMessage
        );
    }

    protected boolean isElementVisible(By by) {
        List<WebElement> elements = driver.findElements(by);
        return !elements.isEmpty() && elements.get(0)
                                              .isDisplayed();
    }

    protected boolean selectByVisibleText(By by, String visibleText) {
        WebElement element = getElement(by);
        scrollIntoView(element);
        element.click();

        List<WebElement> options = element.findElements(By.tagName("option"));

        for (WebElement option : options) {
            String text = option.getText()
                                .trim();

            if (text.equals(visibleText)) {
                option.click();

                driver.findElement(body)
                      .click();

                return true;
            }
        }

        // Close dropdown if option not found
        driver.findElement(body)
              .click();

        return false;
    }

    protected String getValidationMessage(By by) {
        WebElement element = getElement(by);
        return driver.executeScript("return arguments[0].validationMessage;", element)
                     .toString()
                     .toLowerCase();
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    protected void closeKeyboardIfOpen() {
        ((JavascriptExecutor) driver).executeScript("document.activeElement.blur()");
    }

    protected boolean verifyIfTextDisplayedInElement(WebElement element, String expectedMessage) {
        try {
            return element.findElements(
                                  By.xpath(".//*[contains(text(), '" + expectedMessage + "')]")
                          )
                          .size() > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    protected String extractBackgroundUrl(String style) {
        if (style == null || !style.contains("url(")) {
            return null;
        }

        return style
                .substring(style.indexOf("url(") + 4, style.indexOf(")", style.indexOf("url(")))
                .replace("\"", "")
                .trim();
    }

    @Override
    protected String getElementText(WebElement element) {
        String tagName = element.getTagName();

        if ("textarea".equalsIgnoreCase(tagName))
            return element.getText();
        else
            return element.getAttribute("value");
    }

    @Override
    protected void enterKeys(By by, Object keys) {
        closeKeyboardIfOpen();
        WebElement element = this.getElement(by);
        scrollIntoView(element);

        String currentText = getElementText(element);

        if (currentText != null && currentText.equals(keys.toString()))
            return;

        element.click();
        element.clear();
        element.sendKeys((CharSequence) keys);
    }
    // </editor-fold>

}

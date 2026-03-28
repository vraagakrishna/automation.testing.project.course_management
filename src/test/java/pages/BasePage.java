package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import utils.AlertUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class BasePage {

    // <editor-fold desc="Class Fields / Constants">
    protected final AppiumDriver driver;

    protected final AlertUtils alertUtils;

    private final By body = By.tagName("body");
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

    protected void getElementOrThrow(By by, String elementName) {
        try {
            getElement(by);
        } catch (TimeoutException ex) {
            throw new TimeoutException(elementName + " not displayed", ex);
        }
    }

    protected void scrollToViewThenClickButton(By by) {
        WebElement element = this.getElement(by);
        scrollIntoView(element);
        clickButton(element);
    }

    protected void clickButton(By by) {
        this.getElement(by)
            .click();
    }

    protected void clickButton(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
    }

    protected void verifyIfTextDisplayed(By by, String expectedMessage) {
        WebElement element = this.getElement(by);
        String actualMessage = element.getText();

        Assert.assertTrue(
                actualMessage.contains(expectedMessage),
                "Expected message: " + expectedMessage + ", but actual message: " + actualMessage
        );
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

    protected String getSelectedOptionInDropdown(By by) {
        return new Select(getElement(by)).getFirstSelectedOption()
                                         .getText();
    }

    protected String getSelectedOptionInDropdown(String label) {
        String source = driver.getPageSource()
                              .toLowerCase();
        label = label.toLowerCase();

        Pattern pattern = Pattern.compile("content-desc=\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(source);

        while (matcher.find()) {
            String contentDesc = matcher.group(1);

            // normalise encoded newline
            contentDesc = contentDesc.replace("&#10;", "\n");

            String[] parts = contentDesc.split("\n");

            if (parts.length == 2 && parts[0].equals(label))
                return parts[1].trim();
        }

        return null;
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

    protected void setDropdownValue(String label, String value) {
        // Click the dropdown button itself
        driver.findElement(
                      AppiumBy.androidUIAutomator(
                              "new UiSelector().className(\"android.widget.Button\").descriptionContains(\""
                                      + capitalize(label) + "\")"
                      )
              )
              .click();

        // Wait for the option buttons to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement option = wait.until(d -> {
            List<WebElement> elems = driver.findElements(
                    AppiumBy.androidUIAutomator(
                            "new UiSelector().className(\"android.widget.Button\").description(\"" + value + "\")"
                    )
            );
            return elems.isEmpty() ? null : elems.get(0);
        });

        // Click the option
        option.click();
    }

    protected String getValidationMessage(By by) {
        WebElement element = getElement(by);
        return driver.executeScript("return arguments[0].validationMessage;", element)
                     .toString();
    }

    protected Document getSourceXml() {
        try {
            String source = driver.getPageSource();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                                                            .newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(source)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse page source", e);
        }
    }

    protected Element findCheckbox(Document doc) {
        NodeList nodes = doc.getElementsByTagName("android.widget.CheckBox");

        if (nodes.getLength() > 0)
            return (Element) nodes.item(0);

        return null;
    }

    protected void validateErrorMessage(By inputField, String expectedMessage) {
        // Find the input field first
        WebElement inputElement = driver.findElement(inputField);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Look for any child element with a content-desc inside the EditText
        WebElement errorElement = wait.until(d -> {
            List<WebElement> children = inputElement.findElements(
                    AppiumBy.xpath(".//*")  // all descendants
            );
            for (WebElement e : children) {
                String desc = e.getAttribute("content-desc");
                if (desc != null && desc.contains(expectedMessage)) {
                    return e;
                }
            }
            return null;
        });

        String actualMessage = errorElement.getAttribute("content-desc");
        Assert.assertTrue(
                actualMessage.contains(expectedMessage),
                "Expected message: " + expectedMessage + ", but actual message: " + actualMessage
        );
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    protected void closeKeyboardIfOpen() {
        ((JavascriptExecutor) driver)
                .executeScript("document.activeElement.blur()");
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1)
                  .toUpperCase() + str.substring(1)
                                      .toLowerCase();
    }
    // </editor-fold>

}

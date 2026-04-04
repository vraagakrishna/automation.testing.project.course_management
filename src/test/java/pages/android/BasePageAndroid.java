package pages.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import pages.BasePage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasePageAndroid extends BasePage {

    // <editor-fold desc="Ctor">
    public BasePageAndroid(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Protected Methods">
    protected void getElementOrThrow(By by, String elementName) {
        try {
            getElement(by);
        } catch (TimeoutException ex) {
            throw new TimeoutException(elementName + " not displayed", ex);
        }
    }

    protected String getSnackBarText() {
        Wait<AppiumDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(5))
                                                          .pollingEvery(Duration.ofMillis(300));

        boolean popupDisplayed = wait.until(d -> d.getPageSource()
                                                  .contains("live-region=\"1\""));

        if (!popupDisplayed) return null;

        String source = driver.getPageSource();

        Pattern pattern = Pattern.compile("content-desc=\"([^\"]*)\"[^>]*live-region=\"1\"");
        Matcher matcher = pattern.matcher(source);

        String popupText = null;
        if (matcher.find()) {
            popupText = matcher.group(1);
        }

        return popupText;
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

            if (parts.length == 2 && parts[0].equals(label)) return parts[1].trim();
        }

        return null;
    }

    protected boolean setDropdownValue(String label, String value) {
        // Click the dropdown button itself
        driver.findElement(AppiumBy.androidUIAutomator(
                      "new UiSelector().className(\"android.widget.Button\")." + "descriptionContains(\"" + label + "\")"))
              .click();

        WebElement option = null;

        try {
            // Wait for the option buttons to appear
            option = new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> {
                List<WebElement> elems = driver.findElements(AppiumBy.androidUIAutomator(
                        "new UiSelector().className(\"android.widget.Button\").description(\"" + value + "\")"));
                return elems.isEmpty() ? null : elems.get(0);
            });
        } catch (TimeoutException ex) {
            // nothing to do
        }

        if (option == null) {
            // Close dropdown
            driver.navigate()
                  .back();

            return false;
        }

        // Click the option
        option.click();

        return true;
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

        if (nodes.getLength() > 0) return (Element) nodes.item(0);

        return null;
    }

    protected void validateErrorMessage(By inputField, String expectedMessage) {
        // Find the input field first
        WebElement inputElement = driver.findElement(inputField);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Look for any child element with a content-desc inside the EditText
        WebElement errorElement = wait.until(d -> {
            List<WebElement> children = inputElement.findElements(AppiumBy.xpath(".//*")  // all descendants
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

    protected void closeKeyboardIfOpen() {
        try {
            boolean keyboardOpen = (Boolean) driver.executeScript("mobile: isKeyboardShown");

            if (keyboardOpen) driver.executeScript("mobile: pressKey", Map.of("keycode", 4));
        } catch (Exception ignored) {
            // keyboard was not open
        }
    }

    @Override
    protected String getElementText(WebElement element) {
        return element.getAttribute("text");
    }
    // </editor-fold>

}

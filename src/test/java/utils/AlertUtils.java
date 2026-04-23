package utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.logging.Logger;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;

public class AlertUtils {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(AlertUtils.class.getName());

    private final WebDriver driver;
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public AlertUtils(WebDriver driver) {
        this.driver = driver;
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    public void verifyIfAlertMessageIsCorrect(String expectedMessage) {
        String alertMessage = this.getAlertMessage(null);
        logger.info(String.format("Alert message found: %s", alertMessage));
        ReportManager.getTest()
                     .info(String.format("Alert message found: %s", alertMessage));

        Assert.assertTrue(
                alertMessage.contains(expectedMessage.toLowerCase()),
                "Expected alert message: '" + expectedMessage + "', but actual alert message: '" + alertMessage + "'"
        );
    }

    public void verifyIfConfirmationAlertMessageIsCorrect(String expectedMessage, boolean accept) {
        String alertMessage = this.getAlertMessage(accept);
        logger.info(String.format("Alert message found: %s", alertMessage));

        Assert.assertEquals(alertMessage, expectedMessage.toLowerCase(), "Alert message does not match");
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private String getAlertMessage(Boolean confirm) {
        Alert alert = getAlert();
        String alertMessage = this.getAlertMsg(alert);

        logger.info(String.format("Dismissing alert with message: %s", alertMessage));
        if (confirm != null && confirm)
            alert.accept();
        else
            alert.dismiss();

        return alertMessage;
    }

    private Alert getAlert() {
        logger.info("Getting alert");
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(alertIsPresent());
    }

    private String getAlertMsg(Alert alert) {
        logger.info("Getting alert message");

        // wait a fraction of a second to let OS/browser fully paint the alert
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {
        }

        String alertMessage = alert.getText();

        logger.info(String.format("Alert message is: %s", alertMessage));

        return alertMessage.toLowerCase();
    }
    // </editor-fold>

}
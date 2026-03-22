package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    // <editor-fold desc="Class Fields / Constants">
    private static final String SCREENSHOT_DIR = System.getProperty("user.dir") + File.separator +
            "Reports" + File.separator + "Screenshots";
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    public static void captureAndAttach(WebDriver driver, String label) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String path = takeScreenshot(driver, label.replace(" ", "_") + "_" + timestamp);
        ReportManager.getTest()
                     .info("Screenshot: " + label)
                     .addScreenCaptureFromPath(path, label);//
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private static File getScreenshotPath(String screenshotName) {
        return new File(SCREENSHOT_DIR, screenshotName + ".png");
    }

    private static String takeScreenshot(WebDriver driver, String screenshotName) {
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File src = takesScreenshot.getScreenshotAs(OutputType.FILE);

        File destination = getScreenshotPath(screenshotName);

        try {
            if (!destination.getParentFile()
                            .exists()) {
                destination.getParentFile()
                           .mkdirs();
            }

            Files.copy(src.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Screenshots" + File.separator + screenshotName + ".png";
    }
    // </editor-fold>

}

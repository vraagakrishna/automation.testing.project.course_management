package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {

    // <editor-fold desc="Class Fields / Constants">
    private static final String REPORT_DIR = System.getProperty("user.dir") + File.separator + "Reports";

    private static ExtentReports extent;
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    public static ExtentReports extentReportSetup() {
        if (extent != null)
            return extent;

        // Create report directory
        File dir = new File(REPORT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        extent = new ExtentReports();
        ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(new File(
                REPORT_DIR + File.separator + "report_" + timestamp + ".html"
        ));
        extent.attachReporter(extentSparkReporter);

        extentSparkReporter.config()
                           .setDocumentTitle("Course Management Report");
        extentSparkReporter.config()
                           .setReportName("Course Management");

        // Make the right-hand details panel scrollable
        extentSparkReporter.config()
                           .setCss(".test-contents { overflow-y: scroll; max-height: 600px; }");

        extentSparkReporter.config()
                           .setTheme(Theme.DARK);

        // Fetch system details
        String os = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String javaVersion = System.getProperty("java.version");

        // Add them to the report
        extent.setSystemInfo("Operating System", os + " " + osVersion);
        extent.setSystemInfo("Java Version", javaVersion);
        extent.setSystemInfo("Platform Name", ConfigManager.getPlatformName());
        extent.setSystemInfo("Execution Type", ConfigManager.getExecutionType());
        extent.setSystemInfo("Browser Name", ConfigManager.getBrowserName());

        return extent;
    }

    public static ExtentReports getExtent() {
        return extent;
    }
    // </editor-fold>

}

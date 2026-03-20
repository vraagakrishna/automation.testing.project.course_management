package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

public class ExtentReportManager {

    // <editor-fold desc="Class Fields / Constants">
    private static final String REPORT_DIR = System.getProperty("user.dir") + File.separator + "Reports";

    // </editor-fold>

    // <editor-fold desc="Public Methods">
    public static ExtentReports extentReportSetup() {
        // Create report directory
        File dir = new File(REPORT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        ExtentReports extentReports = new ExtentReports();
        ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(new File(
                REPORT_DIR + File.separator + "report_" + System.currentTimeMillis() + ".html"
        ));
        extentReports.attachReporter(extentSparkReporter);

        extentSparkReporter.config().setDocumentTitle("Course Management Report");
        extentSparkReporter.config().setReportName("Course Management");

        // Make the right-hand details panel scrollable
        extentSparkReporter.config().setCss(".test-contents { overflow-y: scroll; max-height: 600px; }");

        extentSparkReporter.config().setTheme(Theme.DARK);

        // Fetch system details
        String os = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String javaVersion = System.getProperty("java.version");

        // Add them to the report
        extentReports.setSystemInfo("Operating System", os + " " + osVersion);
        extentReports.setSystemInfo("Java Version", javaVersion);
        extentReports.setSystemInfo("Platform Name", ConfigManager.getPlatformName());
        extentReports.setSystemInfo("Execution Type", ConfigManager.getExecutionType());
        extentReports.setSystemInfo("Browser Name", ConfigManager.getBrowserName());

        return extentReports;
    }
    // </editor-fold>

}

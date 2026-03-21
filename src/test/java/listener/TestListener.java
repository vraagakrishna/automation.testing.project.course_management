package listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import factory.DriverFactory;
import org.testng.*;
import pages.web.auth.LoginPageWeb;
import utils.ExtentReportManager;
import utils.ReportManager;
import utils.ScreenshotUtils;

import java.util.logging.Logger;

public class TestListener implements ITestListener, IConfigurationListener {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(TestListener.class.getName());

    private static ExtentReports extent;
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void onStart(ITestContext context) {
        extent = ExtentReportManager.extentReportSetup();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = this.getDescription(result.getMethod());
        ExtentTest extentTest = extent.createTest(testName);
        ReportManager.setTest(extentTest);

        String[] groups = result.getMethod()
                                .getGroups();
        for (String group : groups) {
            extentTest.assignCategory(group);
        }

        logger.info("========================================");
        logger.info(">> Group : " + groups.toString());
        logger.info(">> Test Name: " + testName);
        logger.info("========================================");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info(">> Status  : PASSED");
        ReportManager.getTest()
                     .log(
                             Status.PASS,
                             "Test Case '" + this.getDescription(result.getMethod()) + "' has Passed."
                     );
    }

    @Override
    public void onConfigurationFailure(ITestResult result) {
        handleFailure(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        handleFailure(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.info(">> Status  : SKIPPED");
        ReportManager.getTest()
                     .log(
                             Status.SKIP,
                             "Test Case '" + this.getDescription(result.getMethod()) + "' has Skipped."
                     );
    }

    @Override
    public void onFinish(ITestContext context) {
        ReportManager.unload();
        extent.flush();
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private String getDescription(ITestNGMethod method) {
        String description = method.getDescription();
        if (description == null || description.isEmpty())
            description = method.getMethodName();

        return description;
    }

    private void handleFailure(ITestResult result) {
        logger.info(">> Status  : FAILED");
        ExtentTest test = ReportManager.getTest();
        String testCase = this.getDescription(result.getMethod());

        // Log the actual exception/assertion message
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            test.log(Status.FAIL, "Test Case '" + testCase + "' has Failed.");
            test.log(Status.FAIL, "Reason: " + throwable.getMessage());

            // If you also want the stacktrace:
            test.log(Status.FAIL, throwable);
        }

        ScreenshotUtils.captureAndAttach(DriverFactory.getDriver(), "'" + testCase + "' failed");
    }
    // </editor-fold>

}

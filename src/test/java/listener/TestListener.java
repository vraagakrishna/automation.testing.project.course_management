package listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import factory.DriverFactory;
import org.testng.*;
import utils.ExtentReportManager;
import utils.ReportManager;
import utils.ScreenshotUtils;

public class TestListener implements ITestListener, IConfigurationListener {

    // <editor-fold desc="Class Fields / Constants">
    private static ExtentReports extent;
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void onStart(ITestContext context) {
        extent = ExtentReportManager.extentReportSetup();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(this.getDescription(result.getMethod()));
        ReportManager.setTest(extentTest);

        String[] groups = result.getMethod()
                                .getGroups();
        for (String group : groups) {
            extentTest.assignCategory(group);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
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
        ReportManager.getTest()
                     .log(
                             Status.SKIP,
                             "Test Case '" + this.getDescription(result.getMethod()) + "' has Skipped."
                     );
    }

    @Override
    public void onFinish(ITestContext context) {
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
        ExtentTest test = ReportManager.getTest();
        String testCase = this.getDescription(result.getMethod());

        // If no test exists yet (config failure), create one
        if (test == null) {
            test = extent.createTest("Configuration Failure: " + testCase);
            ReportManager.setTest(test);
        }

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

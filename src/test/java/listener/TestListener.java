package listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import factory.DriverFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import utils.ExtentReportManager;
import utils.ReportManager;
import utils.ScreenshotUtils;

public class TestListener implements ITestListener {

    // <editor-fold desc="Class Fields / Constants">
    private static ExtentReports extent;
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    @Override
    public void onStart(ITestContext context) {
        extent = ExtentReportManager.extentReportSetup();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(this.getDescription(result.getMethod()));
        ReportManager.setTest(extentTest);

        String[] groups = result.getMethod().getGroups();
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
    public void onTestFailure(ITestResult result) {
        ReportManager.getTest()
                     .log(
                             Status.FAIL,
                             "Test Case '" + this.getDescription(result.getMethod()) + "' has Failed."
                     );
        ScreenshotUtils.captureAndAttach(DriverFactory.getDriver(), result.getName());
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
    // </editor-fold>

}

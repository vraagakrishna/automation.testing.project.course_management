package listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import factory.DriverFactory;
import org.testng.*;
import utils.ExtentReportManager;
import utils.ReportManager;
import utils.ScreenshotUtils;
import utils.SoftAssertManager;

import java.util.logging.Logger;

public class TestListener implements ITestListener, IConfigurationListener, IInvokedMethodListener {

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
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        // Only apply to test methods (not @Before/@After)
        if (!method.isTestMethod()) {
            return;
        }

        try {
            // This will FAIL the test if soft asserts failed
            SoftAssertManager.getSoftAssert()
                             .assertAll();
        } catch (AssertionError softEx) {
            Throwable existing = result.getThrowable();

            if (existing != null) {
                // Keep original failure
                // Attach soft assert with its own stack
                existing.addSuppressed(softEx);

                result.setThrowable(existing);
            } else {
                // Only soft assert failed
                result.setThrowable(softEx);
            }

            result.setStatus(ITestResult.FAILURE);
        } finally {
            SoftAssertManager.remove();
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info(">> Status  : PASSED");
        ReportManager.getTest()
                     .log(
                             Status.PASS,
                             "Test Case '" + ReportManager.getDescription(result.getMethod()) + "' has Passed."
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
                             "Test Case '" + ReportManager.getDescription(result.getMethod()) + "' has Skipped."
                     );
    }

    @Override
    public void onFinish(ITestContext context) {
        ReportManager.unload();
        extent.flush();
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private void handleFailure(ITestResult result) {
        logger.info(">> Status  : FAILED");
        ExtentTest test = ReportManager.getTest();
        String testCase = ReportManager.getDescription(result.getMethod());

        // Log the actual exception/assertion message
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            test.log(Status.FAIL, "Test Case '" + testCase + "' has Failed.");

            // Hard Assertion
            logger.info("Hard Assert Failure:\n" + getStackTraceWithoutSuppressed(throwable));

            test.log(Status.FAIL, "Hard Assert Failure: ");
            test.log(Status.FAIL, "Reason: " + throwable.getMessage());
            test.log(Status.FAIL, throwable);

            Throwable[] suppressed = throwable.getSuppressed();
            if (suppressed.length > 0) {
                test.log(Status.FAIL, "Soft Assert Failures:");

                for (Throwable soft : suppressed) {
                    logger.info("Soft Assert Failure:\n" + getStackTraceWithoutSuppressed(soft));
                    test.log(Status.FAIL, "Reason: " + soft.getMessage());
                    test.log(Status.FAIL, soft);
                }
            }

        }

        ScreenshotUtils.captureAndAttach(DriverFactory.getDriver(), "'" + testCase + "' failed");
    }

    private String getStackTraceWithoutSuppressed(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t)
          .append("\n");

        for (StackTraceElement element : t.getStackTrace()) {
            sb.append("\tat ")
              .append(element)
              .append("\n");
        }

        return sb.toString();
    }
    // </editor-fold>

}

package listener;

import factory.DriverFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtils;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        ScreenshotUtils.captureAndAttach(DriverFactory.getDriver(), result.getName());
    }

}

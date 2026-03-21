package utils;

import com.aventstack.extentreports.ExtentTest;

public class ReportManager {

    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentTest getTest() {
        if (test.get() == null) {
            ExtentTest setupNode = ExtentReportManager.getExtent()
                                                      .createTest("Setup");
            test.set(setupNode);
        }
        return test.get();
    }

    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    public static void unload() {
        test.remove();
    }

}

package utils;

import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.logging.Logger;

public class ReportManager {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(ReportManager.class.getName());

    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    public static String getDescription(ITestNGMethod method) {
        String description = method.getDescription();
        if (description == null || description.isEmpty())
            description = method.getMethodName();

        return description;
    }

    public static void startTest(ITestResult result) {
        String testName = getDescription(result.getMethod());
        ExtentTest extentTest = ExtentReportManager.getExtent()
                                                   .createTest(testName);
        ReportManager.setTest(extentTest);

        String[] groups = result.getMethod()
                                .getGroups();
        for (String group : groups) {
            ReportManager.getTest()
                         .assignCategory(group);
        }

        logger.info("========================================");
        logger.info(">> Group : " + Arrays.toString(groups));
        logger.info(">> Test Name: " + testName);
        logger.info("========================================");
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    public static void unload() {
        test.remove();
    }
    // </editor-fold>

}

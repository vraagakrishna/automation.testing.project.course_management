package utils;

import org.testng.asserts.SoftAssert;

public class SoftAssertManager {

    private static final ThreadLocal<SoftAssert> softAssertThread = ThreadLocal.withInitial(SoftAssert::new);

    public static SoftAssert getSoftAssert() {
        return softAssertThread.get();
    }

    public static void remove() {
        softAssertThread.remove();
    }

}

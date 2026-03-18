package factory;

import common.Constants;
import io.appium.java_client.AppiumDriver;
import pages.android.HomePageAndroid;
import pages.interfaces.HomePage;
import pages.web.HomePageWeb;
import utils.ConfigManager;

public class PageFactory {

    public static HomePage getHomePage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new HomePageWeb(driver);

        if (platformName.equalsIgnoreCase(Constants.PLATFORM_ANDROID))
            return new HomePageAndroid(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

}

package pages.android.dashboard;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pages.android.BasePageAndroid;
import pages.interfaces.dashboard.IUserDashboardPage;
import pages.web.dashboard.UserDashboardPageWeb;

import java.util.logging.Logger;

public class UserDashboardPageAndroid extends BasePageAndroid implements IUserDashboardPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(UserDashboardPageWeb.class.getName());

    private final By viewAllCoursesBtn = By.xpath("//android.widget.Button[contains(@content-desc,'View All')]");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public UserDashboardPageAndroid(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void clickViewAllCourses() {
        logger.info("Clicking View All courses button");
        clickButton(viewAllCoursesBtn);
    }
    // </editor-fold>

}

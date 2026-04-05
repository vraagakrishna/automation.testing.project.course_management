package pages.interfaces.admin;

import models.Course;
import org.openqa.selenium.WebElement;

public interface ICourseManagementPage {

    void verifyCourseManagementPageIsDisplayed();

    void clickAddCourseBtn();

    void verifyBlankCourseFormIsDisplayed();

    void addCourse(Course course);

    WebElement validateCourseIsDisplayed(Course course);

    void validateCourseIsNotDisplayed(Course course);

    void editCourse(WebElement courseElement, Course editedCourse);

    void cancelCourse(Course course);

    void deleteCourse(WebElement courseElement);

    void cancelEditCourse(WebElement courseElement, Course course);

    void validateCourseTitleErrorMessage();

    void validateCourseDescriptionErrorMessage();

    void verifyAlertMessage(String expectedMessage);

}

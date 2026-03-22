package pages.interfaces.admin;

import models.Course;

public interface ICourseManagementPage {

    void verifyCourseManagementPageIsDisplayed();

    void clickAddCourseBtn();

    void verifyBlankCourseFormIsDisplayed();

    void addCourse(Course course);

    void cancelCourse(Course course);

    void validateCourseTitleErrorMessage(String expectedMessage);

    void validateCourseDescriptionErrorMessage(String expectedMessage);

    void verifyAlertMessage(String expectedMessage);

}

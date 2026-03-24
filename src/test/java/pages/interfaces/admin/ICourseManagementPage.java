package pages.interfaces.admin;

import models.Course;

public interface ICourseManagementPage {

    void verifyCourseManagementPageIsDisplayed();

    void clickAddCourseBtn();

    void verifyBlankCourseFormIsDisplayed();

    void addCourse(Course course);

    void cancelCourse(Course course);

    void validateCourseTitleErrorMessage();

    void validateCourseDescriptionErrorMessage();

    void verifyAlertMessage(String expectedMessage);

}

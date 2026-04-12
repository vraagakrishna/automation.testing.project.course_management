package pages.interfaces.dashboard;

import models.Course;

public interface IUserDashboardPage {

    void clickViewAllCourses();

    void validateEnrolledCourse(Course course);

}

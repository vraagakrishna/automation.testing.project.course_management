package pages.interfaces.user;

import models.Course;
import org.openqa.selenium.WebElement;

public interface ICoursePage {

    WebElement findCourse(Course course);

    void validateCourseDetails(Course course, WebElement courseCardElement, boolean shouldEnroll);

}

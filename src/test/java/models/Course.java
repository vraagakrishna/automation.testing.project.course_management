package models;

import java.math.BigDecimal;

public class Course {

    // <editor-fold desc="Class Fields / Constants">
    private String title;

    private String description;

    private String duration;

    private String level = "Beginner";

    private BigDecimal price;

    private String thumbnailUrl;

    private String meetingUrl;

    private boolean published;
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public Course() {

    }

    public Course(Course other) {
        this.title = other.title;
        this.description = other.description;
        this.duration = other.duration;
        this.level = other.level;
        this.price = other.price;
        this.thumbnailUrl = other.thumbnailUrl;
        this.meetingUrl = other.meetingUrl;
        this.published = other.published;
    }
    // </editor-fold>

    // <editor-fold desc="Getters and Setters">
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getMeetingUrl() {
        return meetingUrl;
    }

    public void setMeetingUrl(String meetingUrl) {
        this.meetingUrl = meetingUrl;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
    // </editor-fold>

    // <editor-fold desc="Overrides">
    @Override
    public String toString() {
        return "Course{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", duration='" + duration + '\'' +
                ", level='" + level + '\'' +
                ", price=" + price +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", meetingUrl='" + meetingUrl + '\'' +
                ", published=" + published +
                '}';
    }
    // </editor-fold>

}

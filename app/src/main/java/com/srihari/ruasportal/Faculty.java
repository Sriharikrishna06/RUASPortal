package com.srihari.ruasportal;

public class Faculty {

    private String Course;
    private String Faculty_Name;

    public Faculty() {
    }

    public Faculty(String course, String faculty_Name) {
        Course = course;
        Faculty_Name = faculty_Name;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getFaculty_Name() {
        return Faculty_Name;
    }

    public void setFaculty_Name(String faculty_Name) {
        Faculty_Name = faculty_Name;
    }
}

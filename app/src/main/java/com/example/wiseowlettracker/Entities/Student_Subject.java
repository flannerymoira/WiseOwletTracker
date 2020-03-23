package com.example.wiseowlettracker.Entities;

public class Student_Subject {
    private int ssy_id;
    private int student_id;
    private int subject_id;
    private String year;
    private int subject_target;

    public Student_Subject(){
    }

    public int getSsy_Id() {
        return ssy_id;
    }

    public void setSsy_Id(int ssy_id) {
        this.ssy_id = ssy_id;
    }

    public int getStudentId() {
        return student_id;
    }

    public void setStudentId(int student_id) {
        this.student_id = student_id;
    }

    public int getSubjectId() {
        return subject_id;
    }

    public void setSubjectId(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getSubjectTarget() {
        return subject_target;
    }

    public void setSubjectTarget(int subject_target) {
        this.subject_target = subject_target;
    }
}



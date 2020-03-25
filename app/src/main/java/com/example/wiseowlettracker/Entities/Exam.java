package com.example.wiseowlettracker.Entities;

public class Exam {

    private int exam_id;
    private String exam_name;
    private String year;

    public Exam(){
    }
    public int getExamId() {
        return exam_id;
    }

    public void setExamId(int exam_id) {
        this.exam_id = exam_id;
    }

    public String getExamName() {
        return exam_name;
    }

    public void setExamName(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}

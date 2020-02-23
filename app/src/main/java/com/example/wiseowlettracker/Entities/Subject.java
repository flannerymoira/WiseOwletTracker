package com.example.wiseowlettracker.Entities;

public class Subject {

    private int subject_id;
    private String subject_name;
    private String level;

    public Subject(){
    }

    public int getSubjectId() {
        return subject_id;
    }

    public void setSubjectId(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubjectName() {
        return subject_name;
    }

    public void setSubjectName(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

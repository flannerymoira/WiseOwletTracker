package com.example.wiseowlettracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static long StudentId;
    public static String StudentName;

   public DatabaseHelper(Context context) {
        super(context, "wiseOwlet2.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //insert new student into the database
    public boolean createStudent(String first_name, String surname, String email, String password, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("first_name",first_name);
        contentValues.put("surname", surname);
        contentValues.put("email",email);
        contentValues.put("password",password);
        contentValues.put("phone",phone);

        long ins = db.insert("student", null, contentValues);

        if(ins==-1)
            {return false;}
        else
            {
            StudentId  = ins;
            db.close();
            return true;
            }
    }

    //check if email is correct
    public boolean checkEmail(String email){
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor ecursor = db.rawQuery("Select email from student where email=?", new String[]{email});

        if(ecursor.getCount()>0)
            {ecursor.close();
            return false;}
        else
            {ecursor.close();
            return true;}
    }

    //check if Password is correct and get StudentId and name
    public boolean checkPassword(String Email, String password) {

        SQLiteDatabase db=this.getReadableDatabase();

        Cursor studentCursor = db.rawQuery("Select student_id, first_name from student where email=? and password=?", new String[]{Email, password});

        if (studentCursor.moveToFirst()) {
            StudentId  = studentCursor.getLong(0);
            StudentName  = studentCursor.getString(1);
            db.close();
            return true;}
        else { db.close();
            return false;
        }
    }

    public boolean createStudyTarget(int daily_target, int weekly_target) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("student_id",StudentId);
        contentValues.put("daily_target",daily_target);
        contentValues.put("weekly_target",weekly_target);

        long result = db.insert("study_target", "student_id, " +
                "daily_target, weekly_target", contentValues);

        db.close();
        if(result==-1)
            return false;
        else
            return true;

    }

    public boolean createStudentSubject(String year, int subjectId, int target) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("student_id",StudentId);
        contentValues.put("subject_id",subjectId);
        contentValues.put("year",year);
        contentValues.put("subject_target",target);

        long result = db.insert("student_subject", "student_id, " +
                "subject_id, year, subject_target", contentValues);

        db.close();
        if(result==-1)
            return false;
        else
            return true;

    }

}



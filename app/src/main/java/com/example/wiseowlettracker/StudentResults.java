package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.wiseowlettracker.Entities.Exam;
import com.example.wiseowlettracker.Entities.Study_Type;
import com.example.wiseowlettracker.Entities.Subject;

import java.util.ArrayList;

import static com.example.wiseowlettracker.DatabaseHelper.StudentId;
import static com.example.wiseowlettracker.MainActivity.DATABASE_NAME;

public class StudentResults extends AppCompatActivity {
    public static int SubjectId, ExamId;
    Spinner subList, examNameList;
    ArrayList<Subject> subjectList;
    SQLiteDatabase srDb;
    ArrayList<String> subNames, examType;
    ArrayList<Exam> examList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_results);
        subList = findViewById(R.id.subSpinner);
        examNameList = findViewById(R.id.examSpinner);

        //Access to database
        DatabaseOpenHelper srConn = new DatabaseOpenHelper(this, DATABASE_NAME, null, 1);
        srDb = srConn.getWritableDatabase();

        getStudentSubjectList();
        getExamList();

        ArrayAdapter<CharSequence> adapterSR = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subNames);
        subList.setAdapter(adapterSR);

        //method to select an option from subject list
        subList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    SubjectId = subjectList.get(position - 1).getSubjectId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> adapterEx = new ArrayAdapter(this, android.R.layout.simple_spinner_item, examType);
        examNameList.setAdapter(adapterEx);

        //method to select an option from type of study list
        examNameList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ExamId = examList.get(position -1).getExamId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //method to get array of subjects for a student from student_subject table
    private void getStudentSubjectList() {
        Subject subject;
        subjectList = new ArrayList<Subject>();
        String sid = Long.toString(StudentId);

        Cursor studentSubject = srDb.rawQuery("SELECT S.* FROM STUDENT_SUBJECT SS, SUBJECT S" +
                " WHERE SS.SUBJECT_ID = S.SUBJECT_ID AND SS.STUDENT_ID = ?", new String[]{sid});

        while (studentSubject.moveToNext()) {
            subject = new Subject();
            subject.setSubjectId(studentSubject.getInt(0));
            subject.setSubjectName(studentSubject.getString(1));
            subject.setLevel(studentSubject.getString(2));
            subjectList.add(subject);
        } // end while

        studentSubject.close();

        subNames = new ArrayList<String>();
        subNames.add("(Select Subject )");

        for (int i = 0; i < subjectList.size(); i++) {
            subNames.add(subjectList.get(i).getSubjectName());
        }
    }

    private void getExamList() {
        Exam exam;
        examList = new ArrayList<Exam>();

        Cursor examCursor = srDb.rawQuery("select * from exam ",null);

        while (examCursor.moveToNext()) {
            exam = new Exam();
            exam.setExamId(examCursor.getInt(0));
            exam.setExamName(examCursor.getString(1));
            examList.add(exam);
        } // end while

        examCursor.close();

        examType = new ArrayList<String>();
        examType.add("(Select Exam )");

        for (int a = 0; a < examList.size(); a++) {
            examType.add(examList.get(a).getExamName());
        }
    }

}

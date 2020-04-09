package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.wiseowlettracker.Entities.Study_Type;
import com.example.wiseowlettracker.Entities.Subject;

import java.util.ArrayList;

import static com.example.wiseowlettracker.DatabaseHelper.StudentId;
import static com.example.wiseowlettracker.MainActivity.DATABASE_NAME;

public class AddStudyLog extends AppCompatActivity {
    public static int SubjectId, StudyId;
    Spinner subList, typeOfStudy;
    ArrayList<String> subNames, studyType;
    ArrayList<Subject> subjectList;
    ArrayList<Study_Type> studyList;
    SQLiteDatabase ssDb;
    EditText editTime, editNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_study_log);
        subList = findViewById(R.id.subSpinner);
        typeOfStudy = findViewById(R.id.studySpinner);

        //Access to database
        DatabaseOpenHelper myConn = new DatabaseOpenHelper(this,DATABASE_NAME, null, 1);
        ssDb = myConn.getWritableDatabase();

        getStudentSubjectList();
        getStudyList();

        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subNames);
        subList.setAdapter(adapter2);

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

        ArrayAdapter<CharSequence> adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, studyType);
        typeOfStudy.setAdapter(adapter3);

        //method to select an option from type of study list
        typeOfStudy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    StudyId = studyList.get(position -1).getStudyId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //onclick listener for StopWatch
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.item2:
                startActivity(new Intent(AddStudyLog.this, StopWatch.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //method to get array of subjects for a student from student_subject table
    private void getStudentSubjectList() {
        Subject subject;
        subjectList = new ArrayList<Subject>();
        String sid = Long.toString(StudentId);

        Cursor studentSubject = ssDb.rawQuery("select s.* from student_subject ss, subject s" +
                " where ss.subject_id = s.subject_id and ss.student_id = ?", new String[]{sid});

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

    private void getStudyList() {
        Study_Type study_type;
        studyList = new ArrayList<Study_Type>();

        Cursor studyCursor = ssDb.rawQuery("SELECT * FROM STUDY_TYPE ",null);

        while (studyCursor.moveToNext()) {
            study_type = new Study_Type();
            study_type.setStudyId(studyCursor.getInt(0));
            study_type.setStudyType(studyCursor.getString(1));
            studyList.add(study_type);
        } // end while

        studyCursor.close();

        studyType = new ArrayList<String>();
        studyType.add("(Select StudyType )");

        for (int a = 0; a < studyList.size(); a++) {
            studyType.add(studyList.get(a).getStudyType());
        }
    }

    public void addStudyLog(View view) {
        DatabaseHelper slDb = new DatabaseHelper(this);
        editNote = (EditText) findViewById(R.id.editNote);
        editTime = (EditText) findViewById(R.id.editTime);
        String tempVal = editTime.getText().toString();
        int time = Integer.parseInt(tempVal);

        boolean logInserted = slDb.createStudyLog(editNote.getText().toString(), time);

        if (logInserted = true) {
            Toast.makeText(AddStudyLog.this, "Study log entry made.", Toast.LENGTH_LONG).show();
            //  setContentView(R.layout.activity_setup_subjects);
        } else {
            Toast.makeText(AddStudyLog.this, "Incorrect data.", Toast.LENGTH_LONG).show();
            //setContentView(R.layout.activity_setup_subjects);}
        }
    } // end addSubject


}


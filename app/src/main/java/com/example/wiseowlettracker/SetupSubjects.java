package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wiseowlettracker.Entities.Subject;
import java.util.ArrayList;

public class SetupSubjects extends AppCompatActivity {

    public static int subId;
    public static String sYear;
    Spinner subList, yearList;
    ArrayList<String> subNames, yearNames;
    ArrayList<Subject> subjectList;
    EditText editDailyTarget, editWeeklyTarget, editYear, editSubject, editTarget;

    SQLiteDatabase db;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_subjects);

        subList = findViewById(R.id.subSpinner);
        yearList = findViewById(R.id.yearSpinner);

        //Access to database
        DatabaseOpenHelper conn = new DatabaseOpenHelper(this, "wiseOwlet2.db", null, 1);
        db = conn.getWritableDatabase();

        getSubjectList();
        obtainLists();

        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, yearNames);
        yearList.setAdapter(adapter2);

        //method to select an option from year list
        yearList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    sYear = yearNames.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subNames);
        subList.setAdapter(adapter);

        //method to select an option from subject list
        subList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    subId = subjectList.get(position - 1).getSubjectId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    //method to get array of objects from table
    private void getSubjectList() {

        Subject subject;
        subjectList = new ArrayList<Subject>();

        Cursor ss = db.rawQuery("SELECT * FROM SUBJECT", null);

        while (ss.moveToNext()) {
            subject = new Subject();
            subject.setSubjectId(ss.getInt(0));
            subject.setSubjectName(ss.getString(1));
            subject.setLevel(ss.getString(2));
            subjectList.add(subject);
        } // end while

        ss.close();
    }

    //method to populate spinners
    private void obtainLists() {

        yearNames = new ArrayList<String>();
        yearNames.add("(Select Year)");
        yearNames.add("Year 1");
        yearNames.add("Year 2");
        yearNames.add("Year 3");
        yearNames.add("Transition Year");
        yearNames.add("Year 5");
        yearNames.add("Year 6");

        subNames = new ArrayList<String>();
        subNames.add("(Select Subject / Level)");

        for (int i = 0; i < subjectList.size(); i++) {
            subNames.add(subjectList.get(i).getSubjectName() + " / " + subjectList.get(i).getLevel());
        } // end for
    } // end obtainLists

    public void addTargets(View view) {
        DatabaseHelper myDb = new DatabaseHelper(this);
        editDailyTarget = (EditText) findViewById(R.id.editDailyTarget);
        String tempVal = editDailyTarget.getText().toString();
        int dailyTarget = Integer.parseInt(tempVal);
        editWeeklyTarget = (EditText) findViewById(R.id.editWeeklyTarget);
        tempVal = editWeeklyTarget.getText().toString();
        int weeklyTarget = Integer.parseInt(tempVal);

        boolean isInserted = myDb.createStudyTarget(dailyTarget,weeklyTarget);

        if (isInserted = true) {
            Toast.makeText(SetupSubjects.this, "Targets are set up.", Toast.LENGTH_LONG).show();}
        else
            Toast.makeText(SetupSubjects.this, "Targets were not set up.", Toast.LENGTH_LONG).show();

    } // end addTargets

    public void addSubject(View view) {
        DatabaseHelper myDb = new DatabaseHelper(this);
        editTarget = (EditText) findViewById(R.id.editTarget);
        String tempVal = editTarget.getText().toString();
        int target = Integer.parseInt(tempVal);

        boolean subInserted = myDb.createStudentSubject(sYear, subId, target);

        if (subInserted = true) {
            Toast.makeText(SetupSubjects.this, "Subject is set up.", Toast.LENGTH_LONG).show();
            //  setContentView(R.layout.activity_setup_subjects);
        } else {
            Toast.makeText(SetupSubjects.this, "Incorrect data.", Toast.LENGTH_LONG).show();
            //setContentView(R.layout.activity_setup_subjects);}
        }
    } // end addSubject

} // end setupSubjects
package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wiseowlettracker.Entities.DatePickerFragment;
import com.example.wiseowlettracker.Entities.Exam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.wiseowlettracker.MainActivity.DATABASE_NAME;

public class ReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static int ExamId;
    public static Boolean FirstDate;
    Button btnAddStartDate, btnAddEndDate;
    SQLiteDatabase repDb;
    Spinner examNameList;
    ArrayList<String> examType;
    ArrayList<Exam> examList;
    ImageButton btn_study_rep, btn_exam_rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        FirstDate = true;
        examNameList = findViewById(R.id.examSpinner);

        //Access to database
        DatabaseOpenHelper srConn = new DatabaseOpenHelper(this, DATABASE_NAME, null, 1);
        repDb = srConn.getWritableDatabase();

        getExamList();

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


        btn_study_rep = findViewById(R.id.btn_study_report);
        btn_study_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportActivity.this, StudyHistory.class));
            }
        });
    }

    public void getDate(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);

        String currentDateString = DateFormat.getDateInstance().format(c.getTime());

        if (FirstDate) {
            TextView textStart = (TextView) findViewById(R.id.btnStartDate);
            textStart.setText(currentDateString);
            FirstDate = false;
        }
        else {
            TextView textEnd = (TextView) findViewById(R.id.btnEndDate);
            textEnd.setText(currentDateString);
        }

    }

    private void getExamList() {
        Exam exam;
        examList = new ArrayList<Exam>();

        examType = new ArrayList<String>();
        examType.add("(Select Exam )");

        Cursor examsCursor = repDb.rawQuery("select * from exam ",null);

        while (examsCursor.moveToNext()) {
            exam = new Exam();
            exam.setExamId(examsCursor.getInt(0));
            exam.setExamName(examsCursor.getString(1));
            exam.setYear(examsCursor.getString(2));
            examList.add(exam);

            examType.add(exam.getExamName());
        } // end while

        examsCursor.close();
    }
}
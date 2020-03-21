package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wiseowlettracker.Entities.Subject;
import com.example.wiseowlettracker.R;

import java.util.ArrayList;

import static com.example.wiseowlettracker.AddStudyLog.SubjectId;
import static com.example.wiseowlettracker.DatabaseHelper.StudentEmail;
import static com.example.wiseowlettracker.DatabaseHelper.StudentId;
import static com.example.wiseowlettracker.DatabaseHelper.StudentPhone;

public class StudentAccount extends AppCompatActivity {
    public static int SubjectId;
    Spinner subList;
    ArrayList<String> subNames;
    ImageView imgPhone, imgEmail;
    EditText txtPhone, txtEmail, txtDailyTarget, txtWeeklyTarget;
    SQLiteDatabase saDb;
    int StudentDailyTarget, StudentWeeklyTarget;
    String tmpDailyVal, tmpWeeklyVal;
    Button btn_upd_phone, btn_upd_target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account);

        //Access to database
        DatabaseOpenHelper nextConn = new DatabaseOpenHelper(this, "wiseOwlet2.db", null, 1);
        saDb = nextConn.getWritableDatabase();

        imgPhone = findViewById(R.id.img_phone);
        imgEmail = findViewById(R.id.img_email);
        subList = findViewById(R.id.subSpinner);

        txtPhone = findViewById(R.id.txtPhone);
        txtPhone.setText(StudentPhone);

        txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setText(StudentEmail);

        getStudentDetails();

        txtDailyTarget = findViewById(R.id.editDailyTarget);
        tmpDailyVal = Integer.toString(StudentDailyTarget);
        txtDailyTarget.setText(tmpDailyVal);

        txtWeeklyTarget = findViewById(R.id.editWeeklyTarget);
        tmpWeeklyVal = Integer.toString(StudentWeeklyTarget);
        txtWeeklyTarget.setText(tmpWeeklyVal);

        getStudentSubjectList();

        ArrayAdapter<CharSequence> adapterSA = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subNames);
        subList.setAdapter(adapterSA);

        //method to select an option from subject list
        subList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
   //                 SubjectId = subjectList.get(position - 1).getSubjectId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn_upd_phone = findViewById(R.id.btnUpdatePhone);

        btn_upd_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_S = txtPhone.getText().toString();
                Boolean updatedPhone = updatePhone(phone_S);
                if (updatedPhone)
                    Toast.makeText(getApplicationContext(), "Update phone is succesful", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Incorrect Password, please retry.", Toast.LENGTH_SHORT).show();
            }});

        btn_upd_target = findViewById(R.id.btnUpdateTarget);

        btn_upd_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dailyVal = txtDailyTarget.getText().toString();
                String weeklyVal = txtWeeklyTarget.getText().toString();
                Boolean updatedTarget = updateTargets(dailyVal, weeklyVal);
                if (updatedTarget) {
                    Toast.makeText(getApplicationContext(), "Update targets succesful", Toast.LENGTH_SHORT).show();

                    StudentDailyTarget = Integer.parseInt(dailyVal);
                    StudentWeeklyTarget = Integer.parseInt(weeklyVal);
                }
                else
                    Toast.makeText(getApplicationContext(), "Incorrect Password, please retry.", Toast.LENGTH_SHORT).show();
            }});

    }


    //get targets for the Student
    public void getStudentDetails() {
        String sid = Long.toString(StudentId);

        Cursor targetCursor = saDb.rawQuery("select daily_target, weekly_target from study_target where student_id =?", new String[]{sid});

        if (targetCursor.moveToFirst()) {
            StudentDailyTarget = targetCursor.getInt(0);
            StudentWeeklyTarget  = targetCursor.getInt(1);
        }

        targetCursor.close();
    }

    //method to get array of subjects for a student from student_subject table
    private void getStudentSubjectList() {
        String sid = Long.toString(StudentId);

        subNames = new ArrayList<String>();
        subNames.add("(Subject / Target)");

        Cursor stuSubCursor = saDb.rawQuery("SELECT S.SUBJECT_NAME, SS.SUBJECT_TARGET FROM STUDENT_SUBJECT SS, SUBJECT S" +
                " WHERE SS.SUBJECT_ID = S.SUBJECT_ID AND SS.STUDENT_ID = ?", new String[]{sid});

        while (stuSubCursor.moveToNext()) {
            subNames.add(stuSubCursor.getString(0) + " / " + stuSubCursor.getString(1));
        }

        stuSubCursor.close();
    }

    //update phone on student table
    public boolean updatePhone(String phone) {
        String sid = Long.toString(StudentId);

        Cursor updPhCursor =  saDb.rawQuery("update student set phone = ?  where student_id=?", new String[]{phone, sid});

        StudentPhone = phone;
        if(updPhCursor.getCount()>0)
        {updPhCursor.close();
            return false;}
        else
        {updPhCursor.close();
            return true;}
    }

    //update targets on study_target table
    public boolean updateTargets(String daily_target, String weekly_target) {
        String temp = Long.toString(StudentId);

        Cursor updTargCursor =  saDb.rawQuery("update study_target set daily_target = ?, weekly_target = ? where student_id = ?", new String[]{daily_target, weekly_target, temp});

        if(updTargCursor.getCount()>0)
        {updTargCursor.close();
            return false;}
        else
        {updTargCursor.close();
            return true;}
    }
}

package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wiseowlettracker.Entities.Subject;
import com.example.wiseowlettracker.R;

import java.util.ArrayList;

import static com.example.wiseowlettracker.DatabaseHelper.StudentEmail;
import static com.example.wiseowlettracker.DatabaseHelper.StudentId;
import static com.example.wiseowlettracker.DatabaseHelper.StudentPhone;

public class StudentAccount extends AppCompatActivity {
    public static int SubjectId;
    Spinner subList;
    ArrayList<String> subNames;
    ImageView imgPhone, imgEmail;
    EditText txtName, txtPhone, txtEmail, txtDailyTarget, txtWeeklyTarget;
    SQLiteDatabase saDb;
    int StudentDailyTarget, StudentWeeklyTarget;
    String tempVal;

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
        tempVal = Integer.toString(StudentDailyTarget);
        txtDailyTarget.setText(tempVal);

        txtWeeklyTarget = findViewById(R.id.editWeeklyTarget);
        tempVal = Integer.toString(StudentWeeklyTarget);
        txtWeeklyTarget.setText(tempVal);

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

}

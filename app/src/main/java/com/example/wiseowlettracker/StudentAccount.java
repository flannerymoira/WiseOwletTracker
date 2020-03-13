package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wiseowlettracker.R;

import static com.example.wiseowlettracker.DatabaseHelper.StudentEmail;
import static com.example.wiseowlettracker.DatabaseHelper.StudentFullName;
import static com.example.wiseowlettracker.DatabaseHelper.StudentId;
import static com.example.wiseowlettracker.DatabaseHelper.StudentPhone;

public class StudentAccount extends AppCompatActivity {
    DatabaseHelper stdb;
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

        txtName = findViewById(R.id.txtName);
        txtName.setText(StudentFullName );

        txtPhone = findViewById(R.id.txtPhone);
        txtPhone.setText(StudentPhone);

        txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setText(StudentEmail);

        Boolean getDetails = getStudentDetails();

        txtDailyTarget = findViewById(R.id.editDailyTarget);
        tempVal = Integer.toString(StudentDailyTarget);
        txtDailyTarget.setText(tempVal);

        txtWeeklyTarget = findViewById(R.id.editWeeklyTarget);
        tempVal = Integer.toString(StudentWeeklyTarget);
        txtWeeklyTarget.setText(tempVal);
    }
    //check if Password is correct and get StudentId and name
    public boolean getStudentDetails() {
        String sid = Long.toString(StudentId);

        Cursor targetCursor = saDb.rawQuery("select daily_target, weekly_target from study_target where student_id =?", new String[]{sid});

        if (targetCursor.moveToFirst()) {
            StudentDailyTarget = targetCursor.getInt(0);
            StudentWeeklyTarget  = targetCursor.getInt(1);
            targetCursor.close();
            return true;}
        else { targetCursor.close();
            return false;
        }
    }

}

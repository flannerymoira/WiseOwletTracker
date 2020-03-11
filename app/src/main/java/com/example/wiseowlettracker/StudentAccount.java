package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wiseowlettracker.R;

import static com.example.wiseowlettracker.DatabaseHelper.StudentEmail;
import static com.example.wiseowlettracker.DatabaseHelper.StudentFullName;
import static com.example.wiseowlettracker.DatabaseHelper.StudentName;
import static com.example.wiseowlettracker.DatabaseHelper.StudentPhone;

public class StudentAccount extends AppCompatActivity {
    ImageView imgAddress, imgPhone, imgEmail;
    EditText txtName, txtAddress, txtPhone, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account);

        imgAddress = findViewById(R.id.img_address);
        imgPhone = findViewById(R.id.img_phone);
        imgEmail = findViewById(R.id.img_email);

        imgAddress.setVisibility(View.VISIBLE);
        imgPhone.setVisibility(View.VISIBLE);
        imgEmail.setVisibility(View.VISIBLE);

        txtName = findViewById(R.id.txtName);
        txtName.setText(StudentFullName );

        txtAddress = findViewById(R.id.txtAddress);
        txtAddress.setText("44 Springvale, Edmondstown Road, Rathfarnham, Dublin 16");

        txtPhone = findViewById(R.id.txtPhone);
        txtPhone.setText(StudentPhone);

        txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setText(StudentEmail );

        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.studenthistory);
        int w = mainLayout.getWidth();
        int h = mainLayout.getHeight();
     //   mainLayout.setRotation(270.0f);


    }
}

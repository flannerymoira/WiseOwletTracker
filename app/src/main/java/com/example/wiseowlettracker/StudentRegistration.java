package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StudentRegistration extends AppCompatActivity {

    EditText email, first_name, surname, password, confirm, phone;
    Button continuebtn;
    DatabaseHelper mydb = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        email = findViewById(R.id.txtEmail);
        first_name = findViewById(R.id.txtFName);
        surname = findViewById(R.id.txtLName);
        password = findViewById(R.id.txtCreatePass);
        confirm = findViewById(R.id.txtConfPass);
        phone = findViewById(R.id.txtPhone);

        //continue button
        continuebtn = findViewById(R.id.btnContReg);

        continuebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String first_name_S = first_name.getText().toString();
                String surname_S = surname.getText().toString();
                String email_S = email.getText().toString();
                String password_S = password.getText().toString();
                String confirm_S = confirm.getText().toString();
                String phone_S = phone.getText().toString();

                if (email_S.equals("") || password_S.equals("") || confirm_S.equals(""))
                    Toast.makeText(getApplicationContext(), "Email and password fields must be entered", Toast.LENGTH_SHORT).show();
                else {
                    if (password_S.equals(confirm_S)) {
                        boolean checkEmail = mydb.checkEmail(email_S);
                        if (checkEmail) {
                            boolean added = mydb.insert(first_name_S, surname_S, email_S, password_S, phone_S);
                            if (added) {
                                Toast.makeText(getApplicationContext(), "Account Registered!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(StudentRegistration.this, SetupSubjects.class));
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Account not inserted", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Account with this email address already exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "The two passwords entered do not match.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}

package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    DatabaseHelper db;
    EditText txtemail, txtpass;
    TextView txtlogin, txtno_acc;
    Button btn_login, btn_reg;
    ImageButton image_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);

        txtemail = findViewById(R.id.txtemail);
        txtpass = findViewById(R.id.txtpass);
        txtlogin = findViewById(R.id.txtlogin);
        txtno_acc = findViewById(R.id.txt_noacc);

        btn_reg = findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(Login.this, StudentRegistration.class);
                startActivity(reg);
            }
        });

//        btn_login = findViewById(R.id.btn_login);
        image_login = findViewById(R.id.imageLogin);
        image_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = txtemail.getText().toString();
                String Password = txtpass.getText().toString();

                if (Email.equals("") || Password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean checkEmail = db.checkEmail(Email);
                    if (!checkEmail) {
                        Boolean checkPassword = db.checkPassword(Email,Password);
                        if (checkPassword) {
                            Toast.makeText(getApplicationContext(), "Login succesful", Toast.LENGTH_SHORT).show();
                            db.close();
                            startActivity(new Intent(Login.this, StudentActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Incorrect Password, please retry.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Log in failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}

package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.wiseowlettracker.MainActivity.DATABASE_NAME;

// If the Student has forgotten password they can reset.
// Enter email, password and confirm password.
// Update student table and unlock account.
public class ResetPassword extends AppCompatActivity {
    EditText email, password, confirm;
    Button resetbtn;
    SQLiteDatabase passDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        //Access to database
        DatabaseOpenHelper passConn = new DatabaseOpenHelper(this, DATABASE_NAME, null, 1);
        passDb = passConn.getWritableDatabase();

        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtCreatePass);
        confirm = findViewById(R.id.txtConfPass);

        //Reset Button
        resetbtn = findViewById(R.id.btnReset);
        resetbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String Confirm  = confirm.getText().toString();

                if (Password.equals(Confirm)) {
                    Boolean updatePass = updatePassword(Password, Email);
                    if (updatePass) {
                        Toast.makeText(getApplicationContext(), "Reset password successfully.", Toast.LENGTH_SHORT).show();
                        passDb.close();
                        startActivity(new Intent(ResetPassword.this, Login.class));
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Reset Password failed.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "The two passwords entered do not match.", Toast.LENGTH_SHORT).show();
            }
         }); // end setOnClickListener
    }

    //update password on student table
    public boolean updatePassword(String password, String email) {

        Cursor passwordCursor =  passDb.rawQuery("update student set password = ?, account_locked = 0 where email = ?", new String[]{password, email});

        if(passwordCursor.getCount()>0)
        {passwordCursor.close();
            return false;}
        else
        {passwordCursor.close();
            return true;}
    }
}

package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.example.wiseowlettracker.DatabaseHelper.StudentName;

public class StudentActivity extends AppCompatActivity {

    TextView txtName, txtMins;
    ImageButton btn_log;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
 //       db = new DatabaseHelper(this);

        txtName = findViewById(R.id.txtName);
        txtName.setText(StudentName + ".");

        txtMins = findViewById(R.id.txtViewMins);
        txtMins.setText("20 minutes");

        btn_log = findViewById(R.id.btn_log);
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log = new Intent(StudentActivity.this, AddStudyLog.class);
                startActivity(log);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //onclick listener for items (
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.item1:
                startActivity(new Intent(StudentActivity.this, StudentAccount.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(StudentActivity.this, StopWatch.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

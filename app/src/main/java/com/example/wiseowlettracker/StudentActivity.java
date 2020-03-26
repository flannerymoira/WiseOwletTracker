package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.wiseowlettracker.DatabaseHelper.StudentEmail;
import static com.example.wiseowlettracker.DatabaseHelper.StudentId;
import static com.example.wiseowlettracker.DatabaseHelper.StudentName;
import static com.example.wiseowlettracker.MainActivity.DATABASE_NAME;

public class StudentActivity extends AppCompatActivity {

    TextView txtName, txtMins;
    ImageButton btn_log, btn_result, btn_rep;
    SQLiteDatabase sumDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //Access to database
        DatabaseOpenHelper firstConn = new DatabaseOpenHelper(this, DATABASE_NAME, null, 1);
        sumDb = firstConn.getReadableDatabase();

        txtName = findViewById(R.id.txtName);
        txtName.setText(StudentName + ".");

        txtMins = findViewById(R.id.txtViewMins);
        int tmpVal =  sumStudy();
        String tmpTotal = Integer.toString(tmpVal);
        txtMins.setText(tmpTotal);
        sumDb.close();

        btn_log = findViewById(R.id.btn_log);

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this, AddStudyLog.class));
            }
        });

        btn_result = findViewById(R.id.btn_result);

        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this, StudentResults.class));
            }
        });

        btn_rep = findViewById(R.id.btn_report);
        btn_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this, StudyHistory.class));
            }
        });

        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 2) {
            // Send Weekly email
            sendEmail();
        }
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

    //Get total of time studied in the last week
    public int sumStudy() {
        String sid = Long.toString(StudentId);
        int totalStudy = 0;

        SimpleDateFormat dl = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date lastweek = cal.getTime();
        String fromDate = dl.format(lastweek);

        Cursor sumStudyCursor =  sumDb.rawQuery("select sum(time_spent) from study_log sl, student_subject ss where" +
                " ss.ssy_id = sl.ssy_id and ss.student_id = ? and sl.entry_date > ?", new String[]{sid, fromDate});

        if (sumStudyCursor.moveToNext())
        { totalStudy = sumStudyCursor.getInt(0); }

        sumStudyCursor.close();
        return totalStudy;
    }

    public void sendEmail() {
        Log.i("Send email", "");
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, StudentEmail);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Wise Owlet Tracker Weekly Report");
        emailIntent.putExtra(Intent.EXTRA_TEXT   , "Hello, " + StudentName + "." );

        try {
            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(emailIntent);
            }
//            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(StudentActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}


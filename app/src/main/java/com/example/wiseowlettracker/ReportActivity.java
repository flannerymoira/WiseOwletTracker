package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.wiseowlettracker.Entities.DatePickerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static Boolean FirstDate;
    Button btnAddStartDate, btnAddEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        FirstDate = true;
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

}

package com.example.wiseowlettracker;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Get quote of the day and give option to Log In or Register new user.
public class MainActivity extends AppCompatActivity {
    public static String DATABASE_NAME = "wiseOwlet.db";
    SQLiteDatabase db;
    TextView txtQuote;
    Date today = Calendar.getInstance().getTime();
    Button btn_login, btn_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Access to database
        DatabaseOpenHelper conn = new DatabaseOpenHelper(this, DATABASE_NAME, null, 1);
        db = conn.getWritableDatabase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtQuote = findViewById(R.id.txtQuote);
        txtQuote.setText(getQuote());

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });

        btn_reg = findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                startActivity(new Intent(MainActivity.this, StudentRegistration.class));
            }
        });

    }

    public String getQuote() {

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String todaysDate = df.format(today);

        //select quote from table quotes
        Cursor tcursor = db.rawQuery("select quote, author from quote where display_date = ?", new String[]{todaysDate});
        StringBuffer buffer = new StringBuffer();

        while (tcursor.moveToNext()) {
            String quote = tcursor.getString(0);
            String author = tcursor.getString(1);
            buffer.append(quote + "\n" + author);
        }

        tcursor.close();
        return buffer.toString();
    }

    //close the database connection
    public void close(){
        if (db!=null) {
            this.db.close();
        }
    } // close

}

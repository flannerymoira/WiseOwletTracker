package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.wiseowlettracker.DatabaseHelper.StudentId;
import static com.example.wiseowlettracker.MainActivity.DATABASE_NAME;
import static com.example.wiseowlettracker.ReportActivity.ReportFromDate;
import static com.example.wiseowlettracker.ReportActivity.ReportToDate;


public class StudyHistory extends AppCompatActivity {
    String sid = Long.toString(StudentId);
    SQLiteDatabase histDb;
    int totalTime, sub = 0;
    String stSubject;
    String [] subLabel = new String[6];
    int[] studyTime = new int[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_history);

        //Access to database
        DatabaseOpenHelper histConn = new DatabaseOpenHelper(this, DATABASE_NAME, null, 1);
        histDb = histConn.getReadableDatabase();

        // To get the time spent on to date must add max time to ReportToDate.
        String toDate = ReportToDate + " 23:59:59";

        // Get the total amount of time spent in each subject in the date range
        Cursor studyHistCursor= histDb.rawQuery("select s.subject_name, sum(sl.time_spent) from study_log sl, student_subject ss, subject s where" +
                " ss.ssy_id = sl.ssy_id and ss.subject_id = s.subject_id and ss.student_id = ? and sl.entry_date > ? and sl.entry_date < ? group by s.subject_id",
                new String[]{sid, ReportFromDate, toDate});

        // Move the data selected into arrays
        while (studyHistCursor.moveToNext())
        {   subLabel[sub] = studyHistCursor.getString(0);
            studyTime[sub] = studyHistCursor.getInt(1);
            sub++;
        }

        studyHistCursor.close();

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Study Report");
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, studyTime[0]),
                new DataPoint(1, studyTime[1]),
                new DataPoint(2, studyTime[2]),
                new DataPoint(3, studyTime[3]),
                new DataPoint(4, studyTime[4]),
                new DataPoint(5, studyTime[5])
        });

        graph.addSeries(series);
        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(10);
        series.setAnimated(true);

        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.studenthistory);

        // set the viewport wider than the data, to have a nice view
        graph.getViewport().setMinY(0);
        graph.getViewport().setXAxisBoundsManual(true);

        // use static labels for horizontal labels
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(subLabel);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

    }

}

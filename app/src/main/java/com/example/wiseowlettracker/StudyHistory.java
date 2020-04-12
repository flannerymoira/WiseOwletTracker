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

import java.util.ArrayList;

import static com.example.wiseowlettracker.DatabaseHelper.StudentId;
import static com.example.wiseowlettracker.MainActivity.DATABASE_NAME;
import static com.example.wiseowlettracker.ReportActivity.ReportFromDate;


public class StudyHistory extends AppCompatActivity {
    SQLiteDatabase histDb;
    String sid = Long.toString(StudentId);
    int totalTime;
    String stSubject;
    ArrayList<String> subNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_history);

        //Access to database
        DatabaseOpenHelper histConn = new DatabaseOpenHelper(this, DATABASE_NAME, null, 1);
        histDb = histConn.getReadableDatabase();

        GraphView graph = (GraphView) findViewById(R.id.graph);

        Cursor studyHistCursor= histDb.rawQuery("select s.subject_name, sum(sl.time_spent) from study_log sl, student_subject ss, subject s where" +
                " ss.ssy_id = sl.ssy_id and ss.subject_id = s.subject_id and ss.student_id = ? and sl.entry_date > ? group by s.subject_id", new String[]{sid, ReportFromDate});


        while (studyHistCursor.moveToNext())
        {   subNames.add(studyHistCursor.getString(0));
        }

      //  int num = subNames.size();
        String [] label = new String[3];
        label[0] = "   History";
        label[1] = "Art";
        label[2] = "English";

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{

                new DataPoint(0, 0),
                new DataPoint(1, 200),
                new DataPoint(2, 300),
                new DataPoint(3, 100)
        });

        studyHistCursor.close();

        graph.addSeries(series);
        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(20);
        series.setAnimated(true);

        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.studenthistory);
   //   int w = mainLayout.getWidth();
        //int h = mainLayout.getHeight();
        //mainLayout.setRotation(270.0f);

        // set the viewport wider than the data, to have a nice view
        graph.getViewport().setMinX(0d);
        graph.getViewport().setMaxX(5d);
        graph.getViewport().setMinY(0d);
        graph.getViewport().setMaxY(5d);
        graph.getViewport().setXAxisBoundsManual(true);

        // use static labels for horizontal labels
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(label);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

    }

}

package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static com.example.wiseowlettracker.DatabaseHelper.StudentId;
import static com.example.wiseowlettracker.MainActivity.DATABASE_NAME;
import static com.example.wiseowlettracker.ReportActivity.ExamId;

// Run the Exam Report.
// Display a linear chart of exam targets against actual results.
public class ExamReport extends AppCompatActivity {
    String sid = Long.toString(StudentId);
    String examId = String.valueOf(ExamId);
    SQLiteDatabase examDb;
    String [] subLabel = new String[6];
    int[] actualMark = new int[7];
    int[] targetMark = new int[7];
    int sub = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_report);

        //Access to database
        DatabaseOpenHelper histConn = new DatabaseOpenHelper(this, DATABASE_NAME, null, 1);
        examDb = histConn.getReadableDatabase();

        // Get subject, the mark achieved in an exam and the target set for this subject
        Cursor examResultCursor= examDb.rawQuery("select s.subject_name, er.achieved_mark, ss.subject_target from exam_result er, student_subject ss, subject s where" +
                        " er.ssy_id = ss.ssy_id and ss.subject_id = s.subject_id and ss.student_id = ? and er.exam_id = ? ",
                new String[]{sid, examId});

        // Move the data selected into arrays
        while (examResultCursor.moveToNext())
        {   subLabel[sub] = examResultCursor.getString(0);
            actualMark[sub] = examResultCursor.getInt(1);
            targetMark[sub] = examResultCursor.getInt(2);
            sub++;
        }

        examResultCursor.close();

        GraphView actualGraph = (GraphView) findViewById(R.id.actualGraph);
        actualGraph.setTitle("Exam Report");
        LineGraphSeries<DataPoint> actualSeries = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, actualMark[0]),
                new DataPoint(1, actualMark[1]),
                new DataPoint(2, actualMark[2]),
                new DataPoint(3, actualMark[3]),
                new DataPoint(4, actualMark[4]),
                new DataPoint(5, actualMark[5])
        });
        actualGraph.addSeries(actualSeries);

        // styling series
        actualSeries.setTitle("Exam Results");
        actualSeries.setColor(Color.GREEN);
        actualSeries.setDrawDataPoints(true);
        actualSeries.setDataPointsRadius(10);
        actualSeries.setThickness(8);

        LineGraphSeries<DataPoint> targetSeries = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, targetMark[0]),
                new DataPoint(1, targetMark[1]),
                new DataPoint(2, targetMark[2]),
                new DataPoint(3, targetMark[3]),
                new DataPoint(4, targetMark[4]),
                new DataPoint(5, targetMark[5])
        });
        actualGraph.addSeries(targetSeries);

        // styling series
        targetSeries.setTitle("Target Marks");
        targetSeries.setColor(Color.RED);
        targetSeries.setDrawDataPoints(true);
        targetSeries.setDataPointsRadius(10);
        targetSeries.setThickness(8);

        // set the viewport wider than the data, to have a nice view
        actualGraph.getViewport().setMinY(0);
        actualGraph.getViewport().setMaxY(100);
        actualGraph.getViewport().setXAxisBoundsManual(true);

        // use static labels for horizontal labels
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(actualGraph);
        staticLabelsFormatter.setHorizontalLabels(subLabel);
        actualGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }
}

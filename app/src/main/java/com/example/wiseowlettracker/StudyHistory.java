package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;


public class StudyHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_history);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(0.5, 2),
                new DataPoint(1, 5),
                new DataPoint(1.5, 4),
                new DataPoint(2, 3)
        });

        graph.addSeries(series);
        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(25);

        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.studenthistory);
        int w = mainLayout.getWidth();
        int h = mainLayout.getHeight();
        mainLayout.setRotation(270.0f);

   //     mainLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));

 //       mainLayout.setTranslationX((w - h) / 2);
  //      mainLayout.setTranslationY((h - w) / 2);

        /*ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mainLayout.getLayoutParams();
        lp.height = w;
        lp.width = h;
        mainLayout.requestLayout();*/

    }
}

package com.myApp.vilrolf.discgolfputting.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.R;

public class GraphTest extends AppCompatActivity {
    private DbHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mydb = new DbHelper(this);
        setupLineChart();

    }


    private void setupLineChart() {
        LineChartView lineChartView = (LineChartView) findViewById(R.id.linechart);

        LineSet dataset = new LineSet();// new LineSet(String[] labels, Float[] values);
        dataset.addPoint(new Point("1", 1.2f));
        dataset.addPoint("2", 1.4f);
        lineChartView.addData(dataset);
        lineChartView.show();

    }


}

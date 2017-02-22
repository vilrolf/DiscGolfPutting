package com.myApp.vilrolf.discgolfputting.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.db.chart.view.LineChartView;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.R;
import com.myApp.vilrolf.discgolfputting.Utils.ChartUtil;

public class ProgressActivity extends AppCompatActivity {
    DbHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        mydb = new DbHelper(this);
       // mydb.get7DayStatisticsFromDistance(3.1);

        LineChartView lineChartView = (LineChartView) findViewById(R.id.linechartProgress);
        //lineChartView.addData(ChartUtil.makeWeekGraph(mydb.get7DayStatisticsFromDistance(1)));
        lineChartView.addData(ChartUtil.makeDayProgressChart(mydb.getAllGames()));
        lineChartView.show();

    }
}

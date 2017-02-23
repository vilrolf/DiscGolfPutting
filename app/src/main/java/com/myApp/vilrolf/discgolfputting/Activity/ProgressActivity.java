package com.myApp.vilrolf.discgolfputting.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.db.chart.view.LineChartView;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.Game;
import com.myApp.vilrolf.discgolfputting.R;
import com.myApp.vilrolf.discgolfputting.Utils.ChartUtil;

import java.util.ArrayList;

public class ProgressActivity extends AppCompatActivity {
    private DbHelper mydb;
    private ArrayList<Game> activeGames = new ArrayList<>();
    private int spinnerPos;
    private LineChartView lineChartView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        mydb = new DbHelper(this);
        mydb.calculateAVGPointPerThrowForEachGame();

        lineChartView = (LineChartView) findViewById(R.id.linechartProgress);
        setupSpinner();
       // setupLineChart();
    }

    private void setupSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerProgress);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.progressChooseStat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPos = position;
                setupLineChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setAdapter(adapter);
    }

    private void setupLineChart() {
        //<item>Day</item>
       // <item>All games</item>

        activeGames = mydb.getAllGames();
        lineChartView.invalidate();
        lineChartView.reset();

       // lineChartView.rem
        if(spinnerPos == 0){
            lineChartView.addData(ChartUtil.makeDayProgressChart(activeGames));
        } else if(spinnerPos == 1){

            double max = ChartUtil.addMakeAllGameProgressChart(activeGames,lineChartView);
            int iMax = (int) Math.round(max);
            lineChartView.setAxisBorderValues(0, iMax, 1);
        }

        //lineChartView.addData(ChartUtil.makeWeekGraph(mydb.get7DayStatisticsFromDistance(1)));

        lineChartView.show();

    }
}

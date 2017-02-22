package com.myApp.vilrolf.discgolfputting.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.view.LineChartView;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.R;
import com.myApp.vilrolf.discgolfputting.Utils.ChartUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class StatisticsActivity extends AppCompatActivity {
    DbHelper mydb;
    String[] lineChartLabels;
    float[] lineChartValues;
    private Spinner spinnerSortTime;
    private int selectedSortingMethod = 0; // 0 = ALL, 1 = YEAR, 2 = MONTH, 3 = WEEK, 4 = DAY
    private String distanceMarker;
    private ArrayList<GameType> gameTypes;// = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mydb = new DbHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        spinnerSortTime = (Spinner) findViewById(R.id.spinnerStatisticsSortTime);
        mydb.getAllGameTypes();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        distanceMarker = sharedPref.getString(getString(R.string.distance_type), "");
        if (mydb.isGames()) {
            loadSpinners();
            setupContent();
        }

    }

    private void loadSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sortOptionsTime, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortTime.setAdapter(adapter);
/*
        spinnerSortTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeSelectedMethod(position);
            }
        });
        */


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.sortOptionsTime, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortTime.setAdapter(adapter);

    }

    private void changeSelectedMethod(int v) {
        selectedSortingMethod = v;
        setupContent();
    }

    private void setupContent() {
        TextView tv = new TextView(this);
        TableLayout tableLayout1 = (TableLayout) findViewById(R.id.tableLayoutStatistics1);
        TableLayout tableLayout2 = (TableLayout) findViewById(R.id.tableLayoutStatistics2);
        // First second last etc
        setupStaticTableData(tableLayout1);
        // different distances
        setupDynamicTableData(tableLayout2);

        setupLineChart();

    }

    private void setupLineChart() {

        LineChartView lineChartView = (LineChartView) findViewById(R.id.lineChartStatisticsActivity);
        lineChartView = ChartUtil.setupLineChart(lineChartView, lineChartLabels, lineChartValues);
        Animation anim = ChartUtil.getAnimation();
        lineChartView.show(anim);

    }

    private void setupDynamicTableData(TableLayout tableLayout) {

        double[] distances = mydb.getAllDifferentDistancesUsed();

        Arrays.sort(distances);
        lineChartValues = new float[distances.length];
        lineChartLabels = new String[distances.length];
        for (int i = 0; i < distances.length; i++) {
            long nrOfHits = mydb.getNrOfHitsFromDistance(distances[i]);
            long nrOfThrows = mydb.getNrOfThrowsFromDistance(distances[i]);
            double hitPerc = ((double) nrOfHits / (double) nrOfThrows) * 100;

            lineChartLabels[i] = Double.toString(distances[i]);
            lineChartValues[i] = (float) hitPerc;
            //TODO hva fanken gjorde jeg nå?
            TableRow tr = new TableRow(this);
            TextView tvText = new TextView(this);
            tvText.setPadding(7, 7, 7, 7);
            tvText.setText(distances[i] + distanceMarker);

            TextView tvHits = new TextView(this);
            tvHits.setPadding(7, 7, 7, 7);
            tvHits.setText(nrOfHits + "/" + nrOfThrows);

            TextView tvPerc = new TextView(this);
            tvPerc.setPadding(7, 7, 7, 7);

            tvPerc.setText(String.format("%.1f", hitPerc) + "%");

            tr.addView(tvText);
            tr.addView(tvHits);
            tr.addView(tvPerc);
            tableLayout.addView(tr);
        }
    }

    private void setupStaticTableData(TableLayout tableLayout) {
        long nrOfHits = mydb.getNrOfHits(0);
        long nrOfThrows = mydb.getNrOfThrows();

        TableRow tableRowAll = new TableRow(this);

        TextView tvHitsText = new TextView(this);
        tvHitsText.setText(R.string.hits);
        TextView tvHitsNumbers = new TextView(this);

        tvHitsNumbers.setText(nrOfHits + "/" + nrOfThrows);

        TextView tvAllHitsPercentage = new TextView(this);
        double allHitPerc = ((double) nrOfHits / (double) nrOfThrows) * 100;
        tvAllHitsPercentage.setText(String.format("%.1f", allHitPerc) + "%");

        tvAllHitsPercentage.setPadding(7, 7, 7, 7);
        tvHitsNumbers.setPadding(7, 7, 7, 7);
        tvHitsText.setPadding(7, 7, 7, 7);
        tableRowAll.addView(tvHitsText);
        tableRowAll.addView(tvHitsNumbers);
        tableRowAll.addView(tvAllHitsPercentage);
        tableLayout.addView(tableRowAll);

        //1ste kast
        long nrOfHitsType1 = mydb.getNrOfHitsWithType(1);
        long nrOfThrowType1 = mydb.getNrOfThrowsWithType(1);
        double type1Perc = ((double) nrOfHitsType1 / (double) nrOfThrowType1) * 100;

        TableRow tv1ThrowRow = new TableRow(this);

        TextView tv1ThrowText = new TextView(this);
        tv1ThrowText.setPadding(7, 7, 7, 7);

        TextView tv1ThrowHits = new TextView(this);
        tv1ThrowHits.setPadding(7, 7, 7, 7);

        TextView tv1ThrowPerc = new TextView(this);
        tv1ThrowPerc.setPadding(7, 7, 7, 7);

        tv1ThrowText.setText(R.string.throwType1);
        tv1ThrowHits.setText(nrOfHitsType1 + "/" + nrOfThrowType1);
        tv1ThrowPerc.setText(String.format("%.1f", type1Perc) + "%");

        tv1ThrowRow.addView(tv1ThrowText);
        tv1ThrowRow.addView(tv1ThrowHits);
        tv1ThrowRow.addView(tv1ThrowPerc);
        tableLayout.addView(tv1ThrowRow);

        //siste kast
        long nrOfHitsType2 = mydb.getNrOfHitsWithType(2);
        long nrOfThrowType2 = mydb.getNrOfThrowsWithType(2);
        double type2Perc = ((double) nrOfHitsType2 / (double) nrOfThrowType2) * 100;

        TableRow tv2ThrowRow = new TableRow(this);

        TextView tv2ThrowText = new TextView(this);
        tv2ThrowText.setPadding(7, 7, 7, 7);

        TextView tv2ThrowHits = new TextView(this);
        tv2ThrowHits.setPadding(7, 7, 7, 7);

        TextView tv2ThrowPerc = new TextView(this);
        tv2ThrowPerc.setPadding(7, 7, 7, 7);

        tv2ThrowText.setText(R.string.throwType2);
        tv2ThrowHits.setText(nrOfHitsType2 + "/" + nrOfThrowType2);
        tv2ThrowPerc.setText(String.format("%.1f", type2Perc) + "%");

        tv2ThrowRow.addView(tv2ThrowText);
        tv2ThrowRow.addView(tv2ThrowHits);
        tv2ThrowRow.addView(tv2ThrowPerc);
        tableLayout.addView(tv2ThrowRow);

        //midten
        long nrOfHitsType0 = mydb.getNrOfHitsWithType(0);
        long nrOfThrowType0 = mydb.getNrOfThrowsWithType(0);
        double type0Perc = ((double) nrOfHitsType0 / (double) nrOfThrowType0) * 100;

        TableRow tv0ThrowRow = new TableRow(this);

        TextView tv0ThrowText = new TextView(this);
        tv0ThrowText.setPadding(7, 7, 7, 7);

        TextView tv0ThrowHits = new TextView(this);
        tv0ThrowHits.setPadding(7, 7, 7, 7);

        TextView tv0ThrowPerc = new TextView(this);
        tv0ThrowPerc.setPadding(7, 7, 7, 30);

        tv0ThrowText.setText(R.string.throwType0);
        tv0ThrowHits.setText(nrOfHitsType0 + "/" + nrOfThrowType0);
        tv0ThrowPerc.setText(String.format("%.1f", type0Perc) + "%");

        tv0ThrowRow.addView(tv0ThrowText);
        tv0ThrowRow.addView(tv0ThrowHits);
        tv0ThrowRow.addView(tv0ThrowPerc);
        tableLayout.addView(tv0ThrowRow);
    }
}

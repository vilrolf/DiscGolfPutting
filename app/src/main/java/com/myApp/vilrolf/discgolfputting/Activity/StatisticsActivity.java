package com.myApp.vilrolf.discgolfputting.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.view.LineChartView;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.Database.Throw;
import com.myApp.vilrolf.discgolfputting.Engine.StatisticsEngine;
import com.myApp.vilrolf.discgolfputting.R;
import com.myApp.vilrolf.discgolfputting.Utils.ChartUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class StatisticsActivity extends AppCompatActivity {
    DbHelper mydb;
    String[] lineChartLabels;
    float[] lineChartValues;
    private Spinner spinnerSortTime;
    private int selectedSortingMethod = 0; // 0 = ALL, 1 = YEAR, 2 = MONTH, 3 = WEEK, 4 = DAY
    private String distanceMarker;
    private ArrayList<GameType> gameTypes;// = new ArrayList<>();
    private ArrayList<Throw> allThrows;
    private ArrayList<Throw> activeThrows;
    private TableLayout tableLayout1;
    private TableLayout tableLayout2;
    private LineChartView lineChartView;
    StatisticsEngine se;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mydb = new DbHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        spinnerSortTime = (Spinner) findViewById(R.id.spinnerStatisticsSortTime);
        mydb.getAllGameTypes();

        tableLayout1 = (TableLayout) findViewById(R.id.tableLayoutStatistics1);
        tableLayout2 = (TableLayout) findViewById(R.id.tableLayoutStatistics2);
        lineChartView = (LineChartView) findViewById(R.id.lineChartStatisticsActivity);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        distanceMarker = sharedPref.getString(getString(R.string.distance_type), "");
        if (mydb.hasGames()) {
            loadSpinners();
           // setupContent();
            allThrows = mydb.getAllThrows();
        }
    }

    private void loadSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sortOptionsTime, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortTime.setAdapter(adapter);
        spinnerSortTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeSelectedMethod(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.sortOptionsTime, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortTime.setAdapter(adapter);

    }

    private void changeSelectedMethod(int v) {
        Calendar c = Calendar.getInstance();
        Date now = new Date();
        c.setTime(now);
        int currentDay = c.get(Calendar.DAY_OF_YEAR);
        selectedSortingMethod = v;
        if (v == 0) {
            activeThrows = allThrows;
        } else if (v == 1) {
            activeThrows = new ArrayList<>();
            for (Throw th : allThrows) {
                if (th.getCalendar().get(Calendar.DAY_OF_YEAR) >= (currentDay - 30)) {
                    activeThrows.add(th);
                }
            }
        } else if (v == 2) {
            activeThrows = new ArrayList<>();
            for (Throw th : allThrows) {
                if (th.getCalendar().get(Calendar.DAY_OF_YEAR) >= (currentDay - 7)) {
                    activeThrows.add(th);
                }
            }

        } else if (v == 3) {
            activeThrows = new ArrayList<>();
            for (Throw th : allThrows) {
                if (th.getCalendar().get(Calendar.DAY_OF_YEAR) == (currentDay)) {
                    activeThrows.add(th);
                }
            }
        } else {
            activeThrows = allThrows;
        }
        setupContent();
    }

    private void setupContent() {
        se = new StatisticsEngine(activeThrows);

        // First second last etc
        tableLayout1.removeAllViews();
        tableLayout2.removeAllViews();
        lineChartView.invalidate();
        lineChartView.reset();
        if(activeThrows.size() != 0){
            setupStaticTableData(tableLayout1);
            // different distances

            setupDynamicTableData(tableLayout2);

            setupLineChart();
        }



    }

    private void setupLineChart() {


        lineChartView = ChartUtil.setupLineChart(lineChartView, lineChartLabels, lineChartValues);
        Animation anim = ChartUtil.getAnimation();
        lineChartView.show(anim);

    }

    private void setupDynamicTableData(TableLayout tableLayout) {

        double[] distances = se.getAllDifferentDistancesUsed();

        Arrays.sort(distances);
        lineChartValues = new float[distances.length];
        lineChartLabels = new String[distances.length];
        for (int i = 0; i < distances.length; i++) {
            int x = se.distances.indexOf(distances[i]);
            long nrOfHits = se.getNrOfHitsFromDistance(x);
            long nrOfThrows = se.getNrOfThrowsFromDistance(x);
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
        long nrOfHits = se.getNrOfHits();
        long nrOfThrows = se.getNrOfThrows();

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
        long nrOfHitsType1 = se.hitsType[1];
        long nrOfThrowType1 = se.throwType[1];
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
        long nrOfHitsType2 = se.hitsType[2];
        long nrOfThrowType2 = se.throwType[2];
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
        long nrOfHitsType0 = se.hitsType[0];
        long nrOfThrowType0 = se.throwType[0];
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

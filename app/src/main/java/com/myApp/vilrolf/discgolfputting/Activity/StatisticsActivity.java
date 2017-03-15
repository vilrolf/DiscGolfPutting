package com.myApp.vilrolf.discgolfputting.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.db.chart.animation.Animation;
import com.db.chart.view.LineChartView;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.Throw;
import com.myApp.vilrolf.discgolfputting.Engine.StatisticsEngine;
import com.myApp.vilrolf.discgolfputting.R;
import com.myApp.vilrolf.discgolfputting.Utils.ChartUtil;
import com.myApp.vilrolf.discgolfputting.Utils.ColorUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class StatisticsActivity extends AppCompatActivity {
    private static int amountsOfTimePeriods = 4;
    DbHelper mydb;
    ArrayList<ArrayList<String>> linechartsLabels = new ArrayList<>();
    ArrayList<String> lineChartLabels = new ArrayList<>();
    ArrayList<Float> lineChartValues = new ArrayList<>();
    StatisticsEngine se;
    private int selectedSortingMethod = 0; // 0 = ALL, 1 = YEAR, 2 = MONTH, 3 = WEEK, 4 = DAY
    private boolean[] filter = new boolean[amountsOfTimePeriods];
    private String distanceMarker;
    private ArrayList<Throw> allThrows;
    private ArrayList<Throw> activeThrows;
    private TableLayout tableLayout1;
    private LineChartView lineChartView;
    private ArrayList<ArrayList<Float>> lineChartsValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mydb = new DbHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        mydb.getAllGameTypes();

        tableLayout1 = (TableLayout) findViewById(R.id.tableLayoutStatistics1);
        lineChartView = (LineChartView) findViewById(R.id.lineChartStatisticsActivity);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        distanceMarker = sharedPref.getString(getString(R.string.distance_type), "");
        for (int i = 0; i < filter.length; i++) {
            filter[i] = true;
        }
        if (mydb.hasGames()) {
            activeThrows = mydb.getAllThrows();
            se = new StatisticsEngine(activeThrows);
            setupContent();
            allThrows = mydb.getAllThrows();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter:
                View menuItemViewFilter = findViewById(R.id.filter);
                showPopupFilter(menuItemViewFilter);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showPopupFilter(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        String[] timePeriods = getResources().getStringArray(R.array.sortOptionsTime);
        int count = 0;
        for (String s : timePeriods) {
            popup.getMenu().add(s).setCheckable(true).setChecked(filter[count]);
            count++;
        }
        popup.setOnMenuItemClickListener(item -> {
            // http://stackoverflow.com/questions/29726039/how-to-prevent-popup-menu-from-closing-on-checkbox-click/1
            item.setChecked(!item.isChecked());
            for (int i = 0; i < timePeriods.length; i++) {
                if (timePeriods[i].equals(item.getTitle())) {
                    filter[i] = item.isChecked();
                }
            }
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW); // im not sure whats happening now
            item.setActionView(new View(this));
            setupContent();
            return false;
        });

        popup.show();

    }

    private void setupContent() {


        // First second last etc
        tableLayout1.removeAllViews();
        lineChartView.invalidate();
        lineChartView.reset();

        boolean filterActive = false;
        for(int i = 0; i < filter.length && !filterActive; i++){
            filterActive = filter[i];
        }
        if (activeThrows.size() != 0 && filterActive) {
            setupStaticTableData(tableLayout1);
            setupDynamicTableData(tableLayout1);
            setupLineChart();
        }


    }

    private void setupLineChart() {
        lineChartView = ChartUtil.setupLineChartMultipleTimePeriods(lineChartView, lineChartsValues, linechartsLabels);  //ChartUtil.setupLineChart(lineChartView, strings, floats);
        Animation anim = ChartUtil.getAnimation();
        lineChartView.show(anim);

    }

    private void setupDynamicTableData(TableLayout tableLayout) {
        linechartsLabels.clear();
        lineChartsValues.clear();
        double[] distances = se.getAllDifferentDistancesUsed();
        TableRow[] trDistances = new TableRow[distances.length];
        Arrays.sort(distances);

        for (int i = 0; i < distances.length; i++) {
            trDistances[i] = new TableRow(this);
        }
        for (int i = 0; i < amountsOfTimePeriods; i++) {
            if (filter[i]) {
                linechartsLabels.add(new ArrayList<>());
                lineChartsValues.add(new ArrayList<>());
                float lastValue = 100;
                boolean noData = false;

                for (int j = 0; j < distances.length; j++) {
                    int x = se.distances.get(i).indexOf(distances[j]);
                    long nrOfHits;
                    long nrOfThrows;
                    linechartsLabels.get(linechartsLabels.size() - 1).add(Double.toString(distances[j]));
                    if (x == -1) {
                        noData = true;
                        nrOfHits = 0;
                        nrOfThrows = 0;
                        lineChartsValues.get(lineChartsValues.size() - 1).add(lastValue);
                        // TODO THIS IS CHEATING, but oh well
                    } else {

                        nrOfHits = se.hitsFromDistances.get(i).get(x);
                        nrOfThrows = se.amountOfThrowsFromDistances.get(i).get(x);
                        float currentPerc = (float) ((double) nrOfHits / (double) nrOfThrows) * 100;
                        if (noData) {
                            lineChartsValues.get(lineChartsValues.size() - 1).set(j - 1, (lastValue + currentPerc) / 2);
                            noData = false;
                        }
                        lastValue = currentPerc;
                        lineChartsValues.get(lineChartsValues.size() - 1).add(currentPerc);
                    }

                    double hitPerc = ((double) nrOfHits / (double) nrOfThrows) * 100;

                    if (i == 0) {
                        TextView tvText = new TextView(this);
                        tvText.setText(distances[j] + distanceMarker);
                        trDistances[j].addView(tvText);
                    }


                    TextView tvHits = new TextView(this);
                    tvHits.setText(nrOfHits + "/" + nrOfThrows);

                    TextView tvPerc = new TextView(this);
                    tvPerc.setText(String.format("%.1f", hitPerc) + "%");

                    trDistances[j].addView(tvHits);
                    trDistances[j].addView(tvPerc);

                }
            }
        }
        for (int i = 0; i < trDistances.length; i++) {
            tableLayout.addView(trDistances[i]);
        }
    }

    private void setupStaticTableData(TableLayout tableLayout) {

        String[] timeNames = getResources().getStringArray(R.array.sortOptionsTime);
        String[] throwTypesNames = getResources().getStringArray(R.array.throwTypes);

        TableRow tableRowNameOnTimes = new TableRow(this);
        tableRowNameOnTimes.addView(new View(this));

        int colorCount = 0;
        for (int i = 0; i < timeNames.length; i++) {
            if (filter[i]) {
                TextView textView = new TextView(this);
                textView.setText(timeNames[i]);
                textView.setTextColor(ColorUtil.getColorArray()[colorCount % ColorUtil.getColorArray().length]);
                colorCount++;
                tableRowNameOnTimes.addView(textView);
                tableRowNameOnTimes.addView(new View(this));
            }

        }
        tableLayout.addView(tableRowNameOnTimes);

        TableRow tableRowHits = new TableRow(this);
        TextView tvHitsText = new TextView(this);
        tvHitsText.setText(R.string.hits);
        tableRowHits.addView(tvHitsText);

        TableRow tableRowsTypes[] = new TableRow[3]; // what is this again  Oh, mid first and last
        for (int i = 0; i < tableRowsTypes.length; i++) {
            tableRowsTypes[i] = new TableRow(this);
        }
        for (int i = 0; i < amountsOfTimePeriods; i++) {
            if (filter[i]) {
                long nrOfHits = se.hits[i];
                long nrOfThrows = se.amountOfThrows[i];
                TextView tvHitsNumbers = new TextView(this);

                tvHitsNumbers.setText(nrOfHits + "/" + nrOfThrows);

                TextView tvAllHitsPercentage = new TextView(this);

                double allHitPerc = ((double) nrOfHits / (double) nrOfThrows) * 100;
                tvAllHitsPercentage.setText(String.format("%.1f", allHitPerc) + "%");

                tvAllHitsPercentage.setPadding(7, 7, 150, 7);

                tableRowHits.addView(tvHitsNumbers);
                tableRowHits.addView(tvAllHitsPercentage);

                // FIRST MIDDLE LAST TYPES OF THROW
                for (int j = 0; j < throwTypesNames.length; j++) {

                    if (i == 0) {
                        TextView textViewThrowTypeName = new TextView(this);
                        textViewThrowTypeName.setText(throwTypesNames[j]);
                        tableRowsTypes[j].addView(textViewThrowTypeName);
                    }
                    long nrOfHitsOnType = se.hitsType[i][j];
                    long nrOfThrowOnType = se.throwType[i][j];

                    double type1Perc = ((double) nrOfHitsOnType / (double) nrOfThrowOnType) * 100;

                    TextView textViewHits = new TextView(this);
                    textViewHits.setPadding(7, 7, 7, 7);
                    textViewHits.setText(nrOfHitsOnType + "/" + nrOfThrowOnType);

                    tableRowsTypes[j].addView(textViewHits);

                    TextView textViewPerc = new TextView(this);
                    textViewPerc.setPadding(7, 7, 7, 7);
                    textViewPerc.setText(String.format("%.1f", type1Perc) + "%");
                    tableRowsTypes[j].addView(textViewPerc);

                }
            }


        }
        tableLayout.addView(tableRowHits);
        for (int i = 0; i < tableRowsTypes.length; i++) {
            tableLayout.addView(tableRowsTypes[i]);
        }
        tableLayout.addView(new TableRow(this));
    }
}

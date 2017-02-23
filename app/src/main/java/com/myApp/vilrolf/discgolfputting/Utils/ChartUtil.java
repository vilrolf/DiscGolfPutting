package com.myApp.vilrolf.discgolfputting.Utils;

import android.graphics.Color;
import android.graphics.Paint;

import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.myApp.vilrolf.discgolfputting.Database.Game;
import com.myApp.vilrolf.discgolfputting.Database.Throw;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Viljar on 07-Feb-17.
 */
public class ChartUtil {
    private static int maxValue = 40;

    public static LineSet setupLineSetWithColor(List<Throw> discThrows) {
        if (discThrows.size() == 0) {
            return null;
        }
        LineSet lineSet = new LineSet();

        int[] hits = new int[maxValue];
        int[] dThrows = new int[maxValue];
        String[] labels = new String[maxValue];

        for (Throw th : discThrows) {

            if (th.isHit()) {
                hits[th.getRoundNr()]++;
            }

            dThrows[th.getRoundNr()]++;
            labels[th.getRoundNr()] = th.getRoundedDistance();
        }

        for (int i = 0; i < maxValue && dThrows[i] != 0; i++) {
            float perc = ((float) hits[i] / (float) dThrows[i]) * 100;
            lineSet.addPoint(labels[i], perc);
        }
        lineSet.setThickness(Tools.fromDpToPx(5.0f));
        lineSet.setDotsRadius(Tools.fromDpToPx(7.0f));
        lineSet.setColor(Color.parseColor("#727272"));
        lineSet.setDotsColor(Color.parseColor("#727272"));


        return lineSet;
    }

    public static LineSet setupLineSet(List<Throw> discThrows) {
        if (discThrows.size() == 0) {
            return null;
        }
        LineSet lineSet = new LineSet();

        int[] hits = new int[maxValue];
        int[] dThrows = new int[maxValue];
        String[] labels = new String[maxValue];

        for (Throw th : discThrows) {

            if (th.isHit()) {
                hits[th.getRoundNr()]++;
            }

            dThrows[th.getRoundNr()]++;
            labels[th.getRoundNr()] = Double.toString(th.getDistance());

        }

        for (int i = 0; i < maxValue && dThrows[i] != 0; i++) {
            float perc = ((float) hits[i] / (float) dThrows[i]) * 100;
            lineSet.addPoint(labels[i], perc);
        }
        return lineSet;
    }


    public static LineSet setupLineSetWithColor(String[] lineChartLabels, float[] lineChartValues) {
        LineSet lineSet = new LineSet(lineChartLabels, lineChartValues);
        lineSet.setThickness(Tools.fromDpToPx(5.0f));
        lineSet.setDotsRadius(Tools.fromDpToPx(7.0f));
        lineSet.setColor(Color.parseColor("#727272"));
        lineSet.setDotsColor(Color.parseColor("#727272"));
        return lineSet;
    }

    public static Paint setupPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(Tools.fromDpToPx(1.0f));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#727272"));
        return paint;

    }

    public static LineChartView setupLineChart(LineChartView lineChartView, String[] lineChartLabels, float[] lineChartValues) {

        lineChartView.addData(setupLineSetWithColor(lineChartLabels, lineChartValues));
        lineChartView.setAxisBorderValues(0, 100, 25);
        lineChartView.setGrid(4, 0, setupPaint());
        return lineChartView;
    }

    public static Animation getAnimation() {
        return new Animation(500);
    }

    public static LineChartView setupLineChart(LineChartView lineChartView, List<Throw> discThrows) {
        lineChartView.addData(setupLineSetWithColor(discThrows));
        lineChartView.setAxisBorderValues(0, 100, 25);
        lineChartView.setGrid(4, 0, setupPaint());
        return lineChartView;
    }


    public static LineSet makeDayProgressChart(ArrayList<Game> allGames) {
        LineSet lineSet = new LineSet();
        ArrayList<Double> totalScore = new ArrayList<>();
        ArrayList<Integer> gamesOnDay = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int currentDayOfYear = -1;
        int currentIndex = -1;
        for (Game game : allGames) {
            Calendar cal = DateUtil.stringToCal(game.getCreated_at());
            int gameDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
            if(gameDayOfYear != currentDayOfYear){ // we're at a new day
                totalScore.add(game.getScore());
                currentDayOfYear = gameDayOfYear;
                labels.add("" + gameDayOfYear);
                currentIndex++;
                gamesOnDay.add(1);
            } else {
                totalScore.set(currentIndex,totalScore.get(currentIndex) + game.getScore());
                gamesOnDay.set(currentIndex, gamesOnDay.get(currentIndex) + 1);
            }
        }
        for(int i = 0; i < totalScore.size(); i++){
            float value = (float) (double) totalScore.get(i) / gamesOnDay.get(i);
            lineSet.addPoint(labels.get(i),value);
        }

        return lineSet;
    }

    public static LineSet makeWeekGraph(ArrayList<Throw> week) {
        LineSet lineSet = new LineSet();
        int[] hitsOnDay = new int[8];
        String[] labels = {"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar today = Calendar.getInstance();
        int days[] = new int[7];
        days[0] = today.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < days.length; i++) {
            if (days[i - 1] == 1) {
                days[i] = 7;
            } else {
                days[i] = days[i - 1] - 1;
            }

        }

        for (Throw th : week) {
            Calendar cal = DateUtil.stringToCal(th.getTimestamp());
            int day = cal.get(Calendar.DAY_OF_WEEK);
            hitsOnDay[day]++; // Guessing sunday is 0.. sunday is 1, sat is 7


            String dayLongName = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());


        }

        // my hack to
        for (int i = 6; i > -1; i--) {
            String l = labels[days[i]];
            int v = hitsOnDay[days[i]];
            lineSet.addPoint(l, v); // hitsOnDay[days[i]]
        }

        return lineSet;
    }

}

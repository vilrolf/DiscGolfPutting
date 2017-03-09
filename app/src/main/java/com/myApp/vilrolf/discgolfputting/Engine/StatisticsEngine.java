package com.myApp.vilrolf.discgolfputting.Engine;

import com.myApp.vilrolf.discgolfputting.Database.Throw;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Viljar on 01-Mar-17.
 */

public class StatisticsEngine {
    private static int amountsOfTimePeriods = 4;
    public int[] hits = new int[amountsOfTimePeriods];
    public int[] amountOfThrows = new int[amountsOfTimePeriods];
    public ArrayList<ArrayList<Double>> distances = new ArrayList<>(amountsOfTimePeriods);
    public ArrayList<ArrayList<Integer>> hitsFromDistances = new ArrayList<>(amountsOfTimePeriods);
    public ArrayList<ArrayList<Integer>> amountOfThrowsFromDistances = new ArrayList<>(amountsOfTimePeriods);
    public int[][] hitsType = new int[amountsOfTimePeriods][3];
    public int[][] throwType = new int[amountsOfTimePeriods][3];

    /*
        <item>All Time</item>
        <item>Month</item>
        <item>Week</item>
        <item>Day</item>
     */
    public StatisticsEngine(ArrayList<Throw> activeThrows) {
        for (int i = 0; i < hits.length; i++) {
            hits[i] = 0;
            amountOfThrows[i] = 0;
            distances.add(new ArrayList<>());
            hitsFromDistances.add(new ArrayList<>());
            amountOfThrowsFromDistances.add(new ArrayList<>());

        }

        Calendar c = Calendar.getInstance();
        Date now = new Date();
        c.setTime(now);
        long nowInMilli = c.getTimeInMillis();


        if (activeThrows.size() == 0) {

        } else {
            for (Throw th : activeThrows) {

                long thMilli = th.getCalendar().getTimeInMillis();
                int timePeriod = 0; // All
                if (thMilli > nowInMilli - 2629746000L) {
                    timePeriod = 1;
                }
                if (thMilli > nowInMilli - 604800000L) {
                    timePeriod = 2;
                }
                if (thMilli > nowInMilli - 86400000L) {
                    timePeriod = 3;
                }
                for(int k = timePeriod; k > -1; k--){
                    amountOfThrows[k]++;
                    throwType[k][th.getType()]++;
                    int i;
                    if (!distances.get(k).contains(th.getDistance())) {
                        distances.get(k).add(th.getDistance());
                        hitsFromDistances.get(k).add(0);
                        amountOfThrowsFromDistances.get(k).add(1);
                        i = distances.get(k).size() - 1;
                    } else {
                        i = distances.get(k).indexOf(th.getDistance());
                        amountOfThrowsFromDistances.get(k).set(i, amountOfThrowsFromDistances.get(k).get(i) + 1);
                    }

                    if (th.isHit()) {
                        hitsType[k][th.getType()]++;
                        hits[k]++;
                        hitsFromDistances.get(k).set(i, hitsFromDistances.get(k).get(i) + 1);
                    }
                }
                
            }
        }
    }

    public double[] getAllDifferentDistancesUsed() {
        ArrayList<Double> returnArray = new ArrayList<>();
        for (Double d : distances.get(0)) {
            if(!returnArray.contains(d)){
                returnArray.add(d);
            }
        }
        double[] rArray = new double[returnArray.size()];
        for(int i = 0; i < returnArray.size(); i++){
            rArray[i] = returnArray.get(i);
        }

       return rArray;
    }

    public long getNrOfHitsFromDistance(int i, int x) {
        return hitsFromDistances.get(i).get(x);
    }
}

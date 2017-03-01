package com.myApp.vilrolf.discgolfputting.Engine;

import com.myApp.vilrolf.discgolfputting.Database.Throw;

import java.util.ArrayList;

/**
 * Created by Viljar on 01-Mar-17.
 */

public class StatisticsEngine {
    public int hits = 0;
    public int amountOfThrows = 0;
    public ArrayList<Double> distances = new ArrayList<>();
    public ArrayList<Integer> hitsFromDistances = new ArrayList<>();
    public ArrayList<Integer> amountOfThrowsFromDistances = new ArrayList<>();

    public StatisticsEngine(ArrayList<Throw> activeThrows) {
        for (Throw th : activeThrows) {
            amountOfThrows++;
            int i;
            if (!distances.contains(th.getDistance())) {
                distances.add(th.getDistance());
                hitsFromDistances.add(0);
                amountOfThrowsFromDistances.add(1);
                i = distances.size() -1;
            } else {
                i = distances.indexOf(th.getDistance());
                amountOfThrowsFromDistances.set(i, amountOfThrowsFromDistances.get(i) + 1);
            }

            if (th.isHit()) {
                hits++;
                hitsFromDistances.set(i, hitsFromDistances.get(i) + 1);
            }


        }
    }

    public double[] getAllDifferentDistancesUsed() {
        double[] returnArray = new double[distances.size()];
        int i = 0;
        for (Double d : distances) {
            returnArray[i] = distances.get(i);
            i++;
        }
        return returnArray;
    }

    public int getNrOfHitsFromDistance(int i) {
        return hitsFromDistances.get(i);
    }

    public int getNrOfThrowsFromDistance(int i) {
        return amountOfThrowsFromDistances.get(i);
    }

    public long getNrOfHits() {
        return hits;
    }

    public long getNrOfThrows() {
        return amountOfThrows;
    }
}

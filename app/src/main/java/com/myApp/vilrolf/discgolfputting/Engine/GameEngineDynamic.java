package com.myApp.vilrolf.discgolfputting.Engine;

import android.widget.TableRow;
import android.widget.TextView;

import com.myApp.vilrolf.discgolfputting.Database.Game;
import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.Database.Throw;

import java.util.ArrayList;

/**
 * Created by Viljar on 16-Feb-17.
 */

public class GameEngineDynamic extends GameEngine {


    private TextView tvHits;
    private int remaining;
    public GameType gameType;
    private TextView tvRemaining;
    Game game;
    double currentDistance;
    ArrayList<Throw> currentDiscThrows;
    int roundNr = 0;
    private TableRow gameRow;

    private ArrayList<Throw> getNextRoundDynamic() {
        ArrayList<Throw> roundThrows = new ArrayList<>();
        double score = currentDistance * gameType.getPointsPerDistance();
        for (int i = 0; i < gameType.getNrOfThrowsPerRound(); i++) {
            Throw th = new Throw();
            th.setDistance(currentDistance);
            th.setUser(game.getUser());
            th.setGame(game);
            th.setScore(score);
            if (i == 0) {
                th.setType(1);
                th.setScore(th.getScore() * gameType.getFirstShotMultiplier());
            } else if (i == gameType.getNrOfThrowsPerRound() - 1) {
                th.setScore(th.getScore() * gameType.getLastShotMultiplier());
                th.setType(2);
            } else {
                th.setType(0);
            }
            th.setRoundNr(roundNr);
            th.setThrowNr(i);
            roundThrows.add(th);

        }
        currentDiscThrows = roundThrows;
        return roundThrows;
    }


    public ArrayList<Throw> getNextRoundThrows() {
        if (getRemaining() == 0) {
            return null;
        }

        return getNextRoundDynamic();

    }


    @Override
    public void setGame(Game game) {
        this.gameType = game.getGameType();
        this.game = game;
        super.setGame(game);
        currentDistance = gameType.getStart();
    }

    public TextView getTvHits() {
        return tvHits;
    }

    public void setTvHits(TextView tvHits) {
        this.tvHits = tvHits;
    }

    public int getRemaining() {
        int totalThrows = (int) (gameType.getNrOfThrowsPerRound() * gameType.getRounds());
        return totalThrows - super.getGame().getDiscThrows().size();
    }

    public TextView getTvRemaining() {
        return tvRemaining;
    }

    public void setTvRemaining(TextView tvRemaining) {
        this.tvRemaining = tvRemaining;
    }

    public ArrayList<Throw> getCurrentDiscThrows() {
        return currentDiscThrows;
    }

    public String getCurrentDistanceRounded() {
        return "" + currentDistance;
        /*
        if(currentDistance == (long) currentDistance)
            return String.format("%d", currentDistance);
        else
            return String.format("%s",currentDistance);
            */
    }

    public void saveThrows() {
        int hits = 0;
        for (Throw th : currentDiscThrows) {
            if (th.isHit()) {
                hits++;
            }
        }
        if (hits <= gameType.getThresholdDowngrade()) {
            if (currentDistance != gameType.getStart()) {
                currentDistance -= gameType.getIncrement();
                //  Toast.makeText(context, "Shorter distance", Toast.LENGTH_SHORT).show();
            }
        } else if (hits < gameType.getThresholdUpgrade()) {
            // WE STAY AT THE SAME SPOT! :)
            // Toast.makeText(context, "Same distance", Toast.LENGTH_SHORT).show();
        } else {
            // WE're going up
            //  Toast.makeText(context, "Longer distance", Toast.LENGTH_SHORT).show();
            currentDistance += gameType.getIncrement();
        }
        roundNr++;
        game.getDiscThrows().addAll(currentDiscThrows);

    }

    public TableRow getGameRow() {
        return gameRow;
    }

    public void setGameRow(TableRow gameRow) {
        this.gameRow = gameRow;
    }
}

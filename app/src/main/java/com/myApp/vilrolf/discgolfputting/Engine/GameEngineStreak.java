package com.myApp.vilrolf.discgolfputting.Engine;

import com.myApp.vilrolf.discgolfputting.Database.Throw;

import java.util.ArrayList;


public class GameEngineStreak extends GameEngineDynamic {
    private long discsLeft = -1;

    public ArrayList<Throw> getNextRoundThrows() {
        if (discsLeft == 0) {
            return null;
        }
        if (discsLeft == -1) {
            discsLeft = gameType.getNrOfThrowsPerRound();
        }
        ArrayList<Throw> roundThrows = new ArrayList<>();
        double score = currentDistance * gameType.getPointsPerDistance();

        for (int i = 0; i < discsLeft; i++) {
            Throw th = new Throw();
            th.setDistance(currentDistance);
            th.setUser(game.getUser());
            th.setGame(game);
            th.setScore(score);
            if (i == 0) {
                th.setType(1);
                th.setScore(th.getScore() * gameType.getFirstShotMultiplier());
            } else if (i == discsLeft - 1) {
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

    public int getRemaining() {
        return (int) discsLeft;
    }

    public void saveThrows() {
        int hits = 0;
        for (Throw th : currentDiscThrows) {
            if (th.isHit()) {
                hits++;
            }
        }

        currentDistance += gameType.getIncrement();

        roundNr++;
        discsLeft = hits;
        game.getDiscThrows().addAll(currentDiscThrows);

    }
}

package com.myApp.vilrolf.discgolfputting.Engine;

import com.myApp.vilrolf.discgolfputting.Database.Throw;

import java.util.ArrayList;

/**
 * Created by Viljar on 10-Mar-17.
 */

public class GameEngineFlipIt extends GameEngineDynamic {
    boolean gameIsDone = false;
    public void saveThrows() {
        int hits = 0;
        for (Throw th : currentDiscThrows) {
            if (th.isHit()) {
                hits++;
            }
        }

        currentDistance += gameType.getIncrement() * hits;
        roundNr++;
        game.getDiscThrows().addAll(currentDiscThrows);

        if(hits == gameType.getNrOfThrowsPerRound()){
            // GAME IS DONE
            gameIsDone = true;

        }
    }
    // Distance finished x (X amount of points) - Consecutive putts made (X, X+1, X+2) - Negative (X) points for an empty frame
    /*




     */

    public double updateScore() {
        double sum = 0;
        for (Throw th : game.getDiscThrows()) {
            if (th.isHit()) {
                sum += th.getScore();
            }
        }
        game.setScore(sum);
        return sum;
    }
    public int getRemaining(){
        return gameIsDone ? 0 : 1;
    }

}

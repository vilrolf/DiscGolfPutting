package com.myApp.vilrolf.discgolfputting.Database;

import java.io.Serializable;

/**
 * Created by Viljar on 24-Sep-16.
 */
public class Throw implements Serializable {
    private double score;
    private boolean hit = false;
    private int throwNr;
    private int type; // 0: Normal, 1: First, 2: Last
    private Game game;
    private long gameId;
    private User user;
    private int roundNr;
    private double distance;
    private String timestamp;

    public Throw() {

    }

    public Throw(int score, int roundNr, int throwNr) {
        this.score = score;
        this.throwNr = throwNr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getType() {
        return type;
    }

    /**
     * 1 = First, 2 = last 0 = middle
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(long hit) {
        if (hit == 1) {
            this.hit = true;
        } else {
            this.hit = false;
        }
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public double getScore() {

        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }


    public int getThrowNr() {
        return throwNr;
    }

    public void setThrowNr(int throwNr) {
        this.throwNr = throwNr;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getRoundNr() {
        return roundNr;
    }

    public void setRoundNr(int roundNr) {
        this.roundNr = roundNr;
    }

    public String getRoundedDistance() {
        if (distance == (long) distance)
            return String.format("%d", (long) distance);
        else
            return String.format("%s", distance);
    }
}

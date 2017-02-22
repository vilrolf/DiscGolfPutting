package com.myApp.vilrolf.discgolfputting.Database;

import java.io.Serializable;

/**
 * Created by Viljar on 06-Feb-17.
 */
public class GameType implements Serializable, DbClass {
    private long id;
    private long rounds;
    private long nrOfThrowsPerRound;
    private double firstShotMultiplier;
    private double lastShotMultiplier;
    private double allShotHitBonus;
    private double increment;
    private double start;
    private double pointsPerDistance;
    private String gameName;
    private long gameMode;
    private long thresholdDowngrade;
    private long thresholdUpgrade;

    public GameType() {
    }

    public GameType(long id) {
        this.id = id;
    }

    public long getGameMode() {
        return gameMode;
    }

    public void setGameMode(long gameMode) {
        this.gameMode = gameMode;
    }

    public String getDisplayName() {
        return gameName + " " + rounds + "x" + nrOfThrowsPerRound;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRounds() {
        return rounds;
    }

    public void setRounds(long rounds) {
        this.rounds = rounds;
    }

    public long getNrOfThrowsPerRound() {
        return nrOfThrowsPerRound;
    }

    public void setNrOfThrowsPerRound(long nrOfThrowsPerRound) {
        this.nrOfThrowsPerRound = nrOfThrowsPerRound;
    }

    public double getFirstShotMultiplier() {
        return firstShotMultiplier;
    }

    public void setFirstShotMultiplier(double firstShotMultiplier) {
        this.firstShotMultiplier = firstShotMultiplier;
    }

    public double getLastShotMultiplier() {
        return lastShotMultiplier;
    }

    public void setLastShotMultiplier(double lastShotMultiplier) {
        this.lastShotMultiplier = lastShotMultiplier;
    }

    public double getAllShotHitBonus() {
        return allShotHitBonus;
    }

    public void setAllShotHitBonus(double allShotHitBonus) {
        this.allShotHitBonus = allShotHitBonus;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getPointsPerDistance() {
        return pointsPerDistance;
    }

    public void setPointsPerDistance(double pointsPerDistance) {
        this.pointsPerDistance = pointsPerDistance;
    }

    @Override
    public void deleteObject(DbHelper mydb) {
        mydb.deleteGameType(this);
    }

    @Override
    public String toString() {
        return  "id" + id + gameName + " mode: " + gameMode;
    }

    public long getThresholdDowngrade() {
        return thresholdDowngrade;
    }

    public long getThresholdUpgrade() {
        return thresholdUpgrade;
    }

    public void setThresholdUpgrade(long thresholdUpgrade) {
        this.thresholdUpgrade = thresholdUpgrade;
    }

    public void setThresholdDowngrade(long thresholdDowngrade) {
        this.thresholdDowngrade = thresholdDowngrade;
    }
}

package com.myApp.vilrolf.discgolfputting.Database;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Viljar on 20-Sep-16.
 */
public class Game implements Serializable {
    public ArrayList<Throw> currentDiscThrows = new ArrayList<>();
    private long id;
    private GameType gameType;
    private String created_at;
    private User user = null;
    private int hits = -1;
    private double score;
    private ArrayList<Throw> discThrows = new ArrayList<>();
    private int gameTypeId;
    private long multiplayerId;
    private long userId;
    private TextView tvScore;
    private Button btnScore;
    private int color;
    private TextView tvHits;
    private TextView tvRemaining;
    private int throwNr;
    private double currentDistance;
    private int roundNr = 0;

    public Game() {

    }

    public Game(User user) {
        this.user = user;
    }

    public Button getBtnScore() {
        return btnScore;
    }

    public void setBtnScore(Button btnScore) {
        this.btnScore = btnScore;
    }

    public TextView getTvScore() {
        return tvScore;
    }

    public void setTvScore(TextView tvScore) {
        this.tvScore = tvScore;
    }

    public double addScore(double score) {
        this.score += score;
        return score;
    }

    public void setUpThrows() {
        if (gameType.getGameMode() == 2) {

        } else {
            for (int i = 0; i < gameType.getRounds(); i++) {
                for (int j = 0; j < gameType.getNrOfThrowsPerRound(); j++) {
                    Throw th = new Throw();
                    double distance = gameType.getStart() + (gameType.getIncrement() * i);
                    th.setDistance(distance);
                    th.setThrowNr(j);
                    th.setRoundNr(i);

                    double throwScore = distance * gameType.getPointsPerDistance();

                    if (j == 0) {
                        th.setType(1);
                        throwScore = throwScore * gameType.getFirstShotMultiplier();
                    } else if (j == (gameType.getNrOfThrowsPerRound() - 1)) {
                        throwScore = throwScore * gameType.getLastShotMultiplier();
                        th.setType(2);
                    } else {
                        th.setType(0);
                    }
                    th.setScore(throwScore);
                    discThrows.add(th);
                }
            }
        }


    }

    public double getAndUpdateScore() {
        double sum = 0;
        int hitsSum = 0;
        int hitChecker[] = new int[(int) gameType.getRounds()];
        for (Throw th : discThrows) {
            if (th.isHit()) {
                hitsSum++;
                sum += th.getScore();
                hitChecker[th.getRoundNr()]++;
            }
        }
        for (int i = 0; i < hitChecker.length; i++) {
            if (hitChecker[i] == gameType.getNrOfThrowsPerRound()) {
                double distance = gameType.getStart() + (gameType.getIncrement() * i);
                double thScore = distance * gameType.getPointsPerDistance();
                sum += gameType.getAllShotHitBonus() * thScore;
            }
        }
        hits = hitsSum;
        score = sum;
        return sum;
    }

    private void updateHits() {
        int sum = 0;
        for (Throw th : discThrows) {
            if (th.isHit()) {
                sum++;
            }
        }
        hits = sum;
    }

    public String getRoundedScore() {
        return String.format("%.1f", score);
    }

    public String getRoundedPPT() {
        double ppt = (score / (double) discThrows.size());
        return String.format("%.1f", ppt);
    }


    public ArrayList<Throw> getDiscThrows() {
        return discThrows;
    }

    public void setDiscThrows(ArrayList<Throw> discThrows) {
        this.discThrows = discThrows;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addThrow(Throw dThrow) {
        discThrows.add(dThrow);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.currentDistance = gameType.getStart();
        this.gameType = gameType;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        if (user == null) {
            return id + " : " + score;
        } else {
            return id + " : " + score + " - " + user.getName();
        }

    }

    public void setGameTypeId(int gameTypeId) {
        this.gameTypeId = gameTypeId;
    }

    public double getPercHit() {
        if (hits == -1) {
            updateHits();
        }
        updateHits();
        return ((double) hits / (double) discThrows.size()) * 100;
    }

    public int getHits() {
        if (hits == -1) {
            updateHits();
        }
        return hits;
    }

    public String getRoundedPercHit() {
        return String.format("%.1f", getPercHit());
    }

    public long getMultiplayerId() {
        return multiplayerId;
    }

    public void setMultiplayerId(long multiplayerId) {
        this.multiplayerId = multiplayerId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getRemaining() {
        int totalThrows = (int) (gameType.getNrOfThrowsPerRound() * gameType.getRounds());
        return totalThrows - discThrows.size();
    }

    public TextView getTvHits() {
        return tvHits;
    }

    public void setTvHits(TextView tvHits) {
        this.tvHits = tvHits;
    }

    public TextView getTvRemaining() {
        return tvRemaining;
    }

    public void setTvRemaining(TextView tvRemaining) {
        this.tvRemaining = tvRemaining;
    }

    public void updateScore() {
        getAndUpdateScore();
    }

    public void saveThrows(ArrayList<Throw> currentDiscThrows, Context context) {
        int hits = 0;
        for (Throw th : currentDiscThrows) {
            if (th.isHit()) {
                hits++;
            }
        }
        double hitRate = (double) hits / gameType.getNrOfThrowsPerRound();
        double downgradeThreshold = 0.4; // GET THIS FROM DATABASE
        // TODO get this in db
        double upgradeThreshold = 0.8;
        if (hitRate < downgradeThreshold) {
            if (currentDistance != gameType.getStart()) {
                currentDistance -= gameType.getIncrement();
                //  Toast.makeText(context, "Shorter distance", Toast.LENGTH_SHORT).show();
            }
        } else if (hitRate < upgradeThreshold) {
            // WE STAY AT THE SAME SPOT! :)
            // Toast.makeText(context, "Same distance", Toast.LENGTH_SHORT).show();
        } else {
            // WE're going up
            //  Toast.makeText(context, "Longer distance", Toast.LENGTH_SHORT).show();
            currentDistance += gameType.getIncrement();
        }

        roundNr++;
        discThrows.addAll(currentDiscThrows);


    }

    public void updateTextViews() {
        tvHits.setText(hits + "/" + discThrows.size());
        tvRemaining.setText("" + getRemaining());
        tvScore.setText("" + getRoundedScore());
    }

    public ArrayList<Throw> getCurrentDiscThrows() {
        return currentDiscThrows;
    }
}

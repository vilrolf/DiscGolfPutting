package com.myApp.vilrolf.discgolfputting.Engine;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myApp.vilrolf.discgolfputting.Database.Game;
import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.Database.Throw;
import com.myApp.vilrolf.discgolfputting.Database.User;

/**
 * Created by Viljar on 16-Feb-17.
 */

public class GameEngine {
    private Game game;
    private GameType gameType;
    private Button btnScore;
    private TextView tvScore;
    private int color;
    private int hits;
    private String scoreText;
    private double score;
    public GameEngine() {
    }

    public void setGame(Game game) {
        this.gameType = game.getGameType();
        this.game = game;
    }

    /**
     * Creates a new Game!
     * @param game
     */


    public GameEngine(Game game) {
        this.game = game;
        this.gameType = game.getGameType();
    }

    public void setUpThrows() {
        /*
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
                game.setDiscThrows();discThrows.add(th);


        } */
    }

    public Button getBtnScore() {
        return btnScore;
    }

    public TextView getTvScore() {
        return tvScore;
    }

    public Game getGame() {
        return game;
    }

    public void setBtnScore(Button btnScore) {
        this.btnScore = btnScore;
    }

    public User getUser() {
        return game.getUser();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getScore() {

        return game.getRoundedScore();
    }

    public void setTvScore(TextView tvScore) {
        this.tvScore = tvScore;
    }


    public double updateScore() {
        double sum = 0;
        int hitsSum = 0;
        int hitChecker[] = new int[(int) gameType.getRounds()];
        int maxRound = 1;
        for (Throw th : game.getDiscThrows()) {
            if (th.isHit()) {
                hitsSum++;
                sum += th.getScore();
                hitChecker[th.getRoundNr()]++;
                if(th.getRoundNr() > maxRound){
                    maxRound = th.getRoundNr();
                }
            }
        }
        for (int i = 0; i < maxRound; i++) {
            if (hitChecker[i] == gameType.getNrOfThrowsPerRound()) {
                double distance = gameType.getStart() + (gameType.getIncrement() * i);
                double thScore = distance * gameType.getPointsPerDistance();
                sum += gameType.getAllShotHitBonus() * thScore;
            }
        }
        game.setScore(sum);
        hits = hitsSum;
        score = sum;
        return sum;

    }

    public int getHits() {
        return hits;
    }
}

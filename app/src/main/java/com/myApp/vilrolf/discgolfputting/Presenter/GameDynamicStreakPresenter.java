package com.myApp.vilrolf.discgolfputting.Presenter;

import android.content.Intent;
import android.widget.Toast;

import com.myApp.vilrolf.discgolfputting.Activity.GameStatsticsActivity;
import com.myApp.vilrolf.discgolfputting.Activity.MultiplayerGameStatistics;
import com.myApp.vilrolf.discgolfputting.Database.MultiplayerGame;
import com.myApp.vilrolf.discgolfputting.Database.Throw;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.Engine.GameEngine;
import com.myApp.vilrolf.discgolfputting.Engine.GameEngineDynamic;
import com.myApp.vilrolf.discgolfputting.Engine.GameEngineStreak;
import com.myApp.vilrolf.discgolfputting.MvpView.GameDynamicView;

import java.util.ArrayList;

/**
 * Created by Viljar on 16-Feb-17.
 */

public class GameDynamicStreakPresenter extends GameDynamicPresenter {
    private boolean gameStarted = false;
    private ArrayList<GameEngineStreak> gameEngineStreaks = new ArrayList<>();

    public GameDynamicStreakPresenter() {

    }


    public void removeUser(GameEngineDynamic gameEngineDynamic) {
        super.removeUser(gameEngineDynamic);
        gameDynamicView.removeGame(gameEngineDynamic);
        gameEngineStreaks.remove(gameEngineDynamic);

    }


    public void addUser(User user) {

        GameEngineStreak gameEngineStreak = new GameEngineStreak();
        super.addUser(user, gameEngineStreak);
        gameDynamicView.addUserTvHits(gameEngineStreak);
        gameDynamicView.addUserTvRemaining(gameEngineStreak);
        gameEngineStreaks.add(gameEngineStreak);
        gameDynamicView.addUserButton(gameEngineStreak);

        gameEngineStreak.getNextRoundThrows();
        gameDynamicView.createRound(gameEngineStreak);
    }


    public void nextRound() {
        boolean gameDone = false;
        if (!gameStarted) {
            startGame();
        }
        gameDynamicView.clearGameTable();
        for (GameEngineStreak ge : gameEngineStreaks) {
            ge.saveThrows(); // Also updates distance
            if (ge.getRemaining() == 0) {
                gameDone = true;
            } else {
                ge.updateScore();
                ge.getNextRoundThrows();
                gameDynamicView.updateScore(ge);
                gameDynamicView.createRound(ge);
            }
        }
        if (gameDone) {
            Toast.makeText(context, "Game done, gonna save now!", Toast.LENGTH_SHORT).show();
            MultiplayerGame mpGame = saveGame();
            openGameStatistics(mpGame);
        }
    }

    private MultiplayerGame saveGame() {
        MultiplayerGame multiplayerGame = new MultiplayerGame();
        if (gameEngineStreaks.size() > 1) {
            multiplayerGame.setId(db.createMultiplayerGame());
        }

        for (GameEngine ge : gameEngineStreaks) {
            if (gameEngineStreaks.size() > 1) {
                ge.getGame().setMultiplayerId(multiplayerGame.getId());
                multiplayerGame.addGameId(ge.getGame().getId());
            } else {
                ge.getGame().setMultiplayerId(-1);
            }
            //game.setGameType(gameType);
            long id = db.createGame(ge.getGame());
            ge.getGame().setId(id);
            for (Throw th : ge.getGame().getDiscThrows()) {
                th.setGameId(id);
                th.setUser(ge.getUser());
                db.createThrow(th);
            }
        }

        return multiplayerGame;

    }

    private void openGameStatistics(MultiplayerGame multiplayerGame) {
        if (gameEngineStreaks.size() == 1) {
            Intent intent = new Intent(context, GameStatsticsActivity.class);
            intent.putExtra("fromGameActivity", true);
            intent.putExtra("gameId", gameEngineStreaks.get(0).getGame().getId());
            context.startActivity(intent);
        } else {

            Intent intent = new Intent(context, MultiplayerGameStatistics.class);
            intent.putExtra("fromGameActivity", true);
            intent.putExtra("mpg", multiplayerGame);
            context.startActivity(intent);
        }
    }

    private void startGame() {
        gameStarted = true;
        for (GameEngine gameEngine : gameEngineStreaks) {
            gameDynamicView.makeButtonsNotClickable(gameEngine);
        }
        gameDynamicView.makeAddButtonHidden();

    }
}

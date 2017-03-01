package com.myApp.vilrolf.discgolfputting.Presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.myApp.vilrolf.discgolfputting.Activity.GameStatsticsActivity;
import com.myApp.vilrolf.discgolfputting.Activity.MultiplayerGameStatistics;
import com.myApp.vilrolf.discgolfputting.Database.MultiplayerGame;
import com.myApp.vilrolf.discgolfputting.Database.Throw;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.Engine.GameEngine;
import com.myApp.vilrolf.discgolfputting.Engine.GameEngineDynamic;
import com.myApp.vilrolf.discgolfputting.MvpView.GameDynamicView;

import java.util.ArrayList;

/**
 * Created by Viljar on 16-Feb-17.
 */

public class GameDynamicPresenter extends GamePresenter {
    private boolean gameStarted = false;
    GameDynamicView gameDynamicView;
    private ArrayList<GameEngineDynamic> gameEngineDynamics = new ArrayList<>();

    public GameDynamicPresenter() {
    }


    public void setGdw(GameDynamicView gdw) {
        super.setGw(gdw);
        this.gameDynamicView = gdw;
        gdw.setGamePresenter(this);

    }

    public void removeUser(GameEngineDynamic gameEngineDynamic) {
        super.removeUser(gameEngineDynamic);
        gameDynamicView.removeGame(gameEngineDynamic);
        gameEngineDynamics.remove(gameEngineDynamic);

    }

    public void setupGdp(Context context) {
        super.setupGp(context);
        addUser(super.getUnusedUsers().get(0));
        // newRound();
    }


    public void newUser() {
        if (super.getUnusedUsers().size() == 0) {
            gameDynamicView.createDialogCreateNewUser();
        } else {
            gameDynamicView.createDialogSelectUser();
        }
    }


    public void addUser(User user) {

        GameEngineDynamic gameEngineDynamic = new GameEngineDynamic();
        super.addUser(user, gameEngineDynamic);
        gameDynamicView.addUserTvHits(gameEngineDynamic);
        gameDynamicView.addUserTvRemaining(gameEngineDynamic);
        gameEngineDynamics.add(gameEngineDynamic);
        gameDynamicView.addUserButton(gameEngineDynamic);

        gameEngineDynamic.getNextRoundThrows();
        gameDynamicView.createRound(gameEngineDynamic);
    }

    public User createNewUser(String value) {
        User user = super.createNewUser(value);
        addUser(user);
        return user;
    }


    public void nextRound() {
        boolean gameDone = false;
        if (!gameStarted) {
            startGame();
        }
        gameDynamicView.clearGameTable();
        for (GameEngineDynamic ge : gameEngineDynamics) {
            ge.saveThrows();
            ge.updateScore();
            if (ge.getRemaining() == 0) {
                gameDone = true;
            } else {

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
        if (gameEngineDynamics.size() > 1) {
            multiplayerGame.setId(db.createMultiplayerGame());
        }

        for (GameEngine ge : gameEngineDynamics) {
            if (gameEngineDynamics.size() > 1) {
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
        if (gameEngineDynamics.size() == 1) {
            Intent intent = new Intent(context, GameStatsticsActivity.class);
            intent.putExtra("fromGameActivity", true);
            intent.putExtra("gameId", gameEngineDynamics.get(0).getGame().getId());
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
        for (GameEngine gameEngine : gameEngineDynamics) {
            gameDynamicView.makeButtonsNotClickable(gameEngine);
        }
        gameDynamicView.makeAddButtonHidden();

    }

    public void setAllHitRound(GameEngineDynamic ged) {
        ged.setAllHitRow();
        gameDynamicView.makeRowChecked(ged);
    }
}

package com.myApp.vilrolf.discgolfputting.Presenter;

import android.content.Intent;
import android.widget.Toast;

import com.myApp.vilrolf.discgolfputting.Activity.GameStatsticsActivity;
import com.myApp.vilrolf.discgolfputting.Activity.MultiplayerGameStatistics;
import com.myApp.vilrolf.discgolfputting.Database.MultiplayerGame;
import com.myApp.vilrolf.discgolfputting.Database.Throw;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.Engine.GameEngine;
import com.myApp.vilrolf.discgolfputting.Engine.GameEngineFlipIt;

import java.util.ArrayList;

/**
 * Created by Viljar on 10-Mar-17.
 */

public class GameFlipItPresenter extends GameDynamicPresenter {
    private boolean gameStarted = false;
    private ArrayList<GameEngineFlipIt> gameEngineFlipIts = new ArrayList<>();

    public GameFlipItPresenter() {

    }


    public void removeUser(GameEngineFlipIt GameEngineFlipIt) {
        super.removeUser(GameEngineFlipIt);
        gameDynamicView.removeGame(GameEngineFlipIt);
        gameEngineFlipIts.remove(GameEngineFlipIt);

    }


    public void addUser(User user) {

        GameEngineFlipIt gameEngineFlipIt = new GameEngineFlipIt();
        super.addUser(user, gameEngineFlipIt);
        gameDynamicView.addUserTvHits(gameEngineFlipIt);
        gameDynamicView.addUserTvRemaining(gameEngineFlipIt);
        gameEngineFlipIts.add(gameEngineFlipIt);
        gameDynamicView.addUserButton(gameEngineFlipIt);

        gameEngineFlipIt.getNextRoundThrows();
        gameDynamicView.createRound(gameEngineFlipIt);
    }


    public void nextRound() {
        boolean gameDone = false;
        if (!gameStarted) {
            startGame();
        }
        gameDynamicView.clearGameTable();
        for (GameEngineFlipIt ge : gameEngineFlipIts) {
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
        if (gameEngineFlipIts.size() > 1) {
            multiplayerGame.setId(db.createMultiplayerGame());
        }

        for (GameEngine ge : gameEngineFlipIts) {
            if (gameEngineFlipIts.size() > 1) {
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
        if (gameEngineFlipIts.size() == 1) {
            Intent intent = new Intent(context, GameStatsticsActivity.class);
            intent.putExtra("fromGameActivity", true);
            intent.putExtra("gameId", gameEngineFlipIts.get(0).getGame().getId());
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
        for (GameEngine gameEngine : gameEngineFlipIts) {
            gameDynamicView.makeButtonsNotClickable(gameEngine);
        }
        gameDynamicView.makeAddButtonHidden();

    }


}

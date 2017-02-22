package com.myApp.vilrolf.discgolfputting.Presenter;

import android.content.Context;

import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.Game;
import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.Engine.GameEngine;
import com.myApp.vilrolf.discgolfputting.MvpView.GameView;
import com.myApp.vilrolf.discgolfputting.Utils.ColorUtil;

import java.util.ArrayList;

/**
 * Created by Viljar on 16-Feb-17.
 */

public class GamePresenter {
    private int[] colors;
    public DbHelper db;
    private ArrayList<User> allUsers;
    public ArrayList<User> activeUsers;
    private ArrayList<User> unusedUsers;
    private GameType gameType;
    private ArrayList<GameEngine> gameEngines = new ArrayList<>();
    public Context context;
    private GameView gw;

    public void setupGp(Context context) {
        this.context = context;
        allUsers = db.getAllUsers();
        activeUsers = new ArrayList<>();
        unusedUsers = allUsers;
        colors = ColorUtil.getColorArray();
    }


    public void setDb(DbHelper db) {
        this.db = db;
    }

    /**
     * Adds user/game to the Presenter
     * @param user
     * @param gameEngine if null, will create its own
     */
    public void addUser(User user, GameEngine gameEngine) {

        activeUsers.add(user);
        unusedUsers.remove(user);
        Game game = new Game();
        game.setGameType(gameType);
        game.setUser(user);

        if(gameEngine == null){
            gameEngine = new GameEngine();
        }
        gameEngine.setGame(game);
        gameEngine.setColor(colors[activeUsers.indexOf(game.getUser()) % colors.length]);
        // SHOULD BE IN STANDARD!
        //gameEngine.setUpThrows();

        gw.addUserTextViewScore(gameEngine);
    }

    public void addUser(User user) {

        activeUsers.add(user);
        unusedUsers.remove(user);
        Game game = new Game();
        game.setGameType(gameType);
        game.setUser(user);

        GameEngine gameEngine = new GameEngine();

        gameEngine.setGame(game);
        gameEngine.setColor(colors[activeUsers.indexOf(game.getUser()) % colors.length]);
        // SHOULD BE IN STANDARD!
        //gameEngine.setUpThrows();

        gw.addUserTextViewScore(gameEngine);
    }


    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void removeUser(GameEngine ge) {
        activeUsers.remove(ge.getGame().getUser());
        unusedUsers.add(ge.getUser());
        gw.removeGame(ge);

        gameEngines.remove(ge);
        updateTable();
    }

    private void updateTable() {

    }

    public void setGw(GameView gw) {
        this.gw = gw;
        gw.setGamePresenter(this);
    }

    public ArrayList<User> getActiveUsers() {
        return activeUsers;
    }

    public User createNewUser(String value) {
        User user = new User(value);
        user.setId(db.createUser(new User(value)));
        allUsers.add(user);
        addUser(user, null);
        return user;
    }

    public ArrayList<User> getUnusedUsers() {
        return unusedUsers;
    }
}

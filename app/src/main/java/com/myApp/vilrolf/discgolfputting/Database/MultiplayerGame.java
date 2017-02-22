package com.myApp.vilrolf.discgolfputting.Database;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Viljar on 13-Feb-17.
 */

public class MultiplayerGame implements Serializable {
    private long id;
    private ArrayList<Long> gamesIds = new ArrayList<>();
    private ArrayList<Game> games = new ArrayList<>();

    public MultiplayerGame() {
    }

    public MultiplayerGame(long id) {
        this.id = id;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public ArrayList<Long> getGamesIds() {
        return gamesIds;
    }

    public void setGamesIds(ArrayList<Game> games) {
        for (Game game : games) {
            gamesIds.add(game.getId());
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addGameId(long id) {
        gamesIds.add(id);
    }

    public void addGame(Game game) {
        gamesIds.add(game.getId());
        games.add(game);
    }


    public String allUserWithSpaceBetween() {
        String out = "";
        for (Game game : games) {
            out += game.getUser().getName() + " ";
        }
        return out;
    }

    @Override
    public String toString() {

        return id  + " : "  + allUserWithSpaceBetween();
    }
}

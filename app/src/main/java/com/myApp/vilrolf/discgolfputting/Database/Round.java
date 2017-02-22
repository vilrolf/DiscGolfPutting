package com.myApp.vilrolf.discgolfputting.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viljar on 20-Sep-16.
 */
public class Round {
    private long id;
    private User user;
    private Game game;
    private long nrOfDiscThrows;
    private long discHits;
    private double distance;
    List<Throw> discThrows;

    public Round() {
        discThrows = new ArrayList<>();
    }
    public long addHit(){
        discHits++;
        return discHits;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public long getNrOfDiscThrows() {
        return nrOfDiscThrows;
    }

    public void setNrOfDiscThrows(long nrOfDiscThrows) {
        this.nrOfDiscThrows = nrOfDiscThrows;
    }

    public long getDiscHits() {
        return discHits;
    }

    public void setDiscHits(long discHits) {
        this.discHits = discHits;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long removeHit() {
        discHits--;
        return discHits;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

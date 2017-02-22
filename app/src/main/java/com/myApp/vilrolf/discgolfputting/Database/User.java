package com.myApp.vilrolf.discgolfputting.Database;

import java.io.Serializable;

/**
 * Created by Viljar on 20-Sep-16.
 */
public class User implements Serializable, DbClass {
    long id;
    String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(int i) {
        this.id = i;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public void deleteObject(DbHelper mydb) {
        mydb.deleteUser(this);

    }
}

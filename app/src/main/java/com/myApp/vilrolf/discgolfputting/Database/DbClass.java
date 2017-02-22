package com.myApp.vilrolf.discgolfputting.Database;

import java.io.Serializable;

/**
 * Created by Viljar on 14-Feb-17.
 */
public interface DbClass extends Serializable {

    @Override
    String toString();
    void deleteObject(DbHelper mydb);
}

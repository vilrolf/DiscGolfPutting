package com.myApp.vilrolf.discgolfputting.Presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.myApp.vilrolf.discgolfputting.Activity.MainActivity;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.MvpView.GameTypeView;

/**
 * Created by Viljar on 18-Feb-17.
 */

public class NewGameTypePresenter {
    private final DbHelper mydb;
    GameTypeView gameTypeView;
    private Context context;

    public NewGameTypePresenter(Context context) {
        this.context = context;
        gameTypeView = new GameTypeView(context);
        mydb = new DbHelper(context);

    }

    public GameTypeView getGameTypeView() {
        return gameTypeView;
    }

    public void start() {
        gameTypeView.createSpinner();
        // SPINNER IN ON SELECTED WILL MAKE TABLE
    }

    public void save() {
        GameType gameType = gameTypeView.saveTable();
        if(gameType == null){
            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
        } else {
            mydb.createGameType(gameType);
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }

    }
}

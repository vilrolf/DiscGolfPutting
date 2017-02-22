package com.myApp.vilrolf.discgolfputting.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.MvpView.GameTypeView;
import com.myApp.vilrolf.discgolfputting.Presenter.NewGameTypePresenter;
import com.myApp.vilrolf.discgolfputting.R;

public class NewGameType extends AppCompatActivity {
    private DbHelper mydb;
    private  NewGameTypePresenter newGameTypePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newGameTypePresenter = new NewGameTypePresenter(this);

        GameTypeView gameTypeView = newGameTypePresenter.getGameTypeView();
        gameTypeView.setTable((TableLayout) findViewById(R.id.tableLayoutNewGameType));
        gameTypeView.setSpinner((Spinner) findViewById(R.id.spinnerNewGameType));

        newGameTypePresenter.start();

    }

    public void saveGameType(View view) {
        newGameTypePresenter.save();

    }


}

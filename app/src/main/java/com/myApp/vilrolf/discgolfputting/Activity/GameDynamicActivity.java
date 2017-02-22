package com.myApp.vilrolf.discgolfputting.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.db.chart.Tools;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.Game;
import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.Database.MultiplayerGame;
import com.myApp.vilrolf.discgolfputting.Database.Throw;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.MvpView.GameDynamicView;
import com.myApp.vilrolf.discgolfputting.Presenter.GameDynamicPresenter;
import com.myApp.vilrolf.discgolfputting.R;
import com.myApp.vilrolf.discgolfputting.Utils.ColorUtil;

import java.util.ArrayList;

public class GameDynamicActivity extends AppCompatActivity {
    public TableRow userRowNames;
    public TableRow userRowScores;
    public TableRow userRowHits;
    public TableRow userRowRemaining;
    public TableLayout gameTable;
    private DbHelper mydb;
    private GameDynamicPresenter gdp;
    private int[] colors;
    private ArrayList<Game> games = new ArrayList<>();
    private boolean gameStarted = false;
    private GameDynamicView gdw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_dynamic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            gdp.nextRound();
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gdw = new GameDynamicView(this);


        mydb = new DbHelper(this);

        colors = ColorUtil.getColorArray();

        userRowNames = (TableRow) findViewById(R.id.tableRowDynamicGameUsers);
        userRowScores = (TableRow) findViewById(R.id.tableRowDynamicGameUsersScore);
        userRowHits = (TableRow) findViewById(R.id.tableRowGameDynamicHits);
        userRowRemaining = (TableRow) findViewById(R.id.tableRowGameDynamicRemaining);
        gameTable = (TableLayout) findViewById(R.id.tableLayoutDynamicGame);
        Button addButton = (Button) findViewById(R.id.buttonDynamicGameAddUser);
        gdw.setUp(userRowNames, userRowScores);
        gdw.setUpDynamic(userRowHits, userRowRemaining, gameTable, addButton);

        setupGdp();

    }

    private void setupGdp() {
        gdp = new GameDynamicPresenter();
        gdp.setDb(new DbHelper(this));
        gdp.setGdw(gdw);
        gdp.setGameType((GameType) getIntent().getSerializableExtra("gameType"));
        gdp.setupGdp(this);


    }


    private void openGameStatistics(MultiplayerGame multiplayerGame) {
        if (games.size() == 1) {
            Intent intent = new Intent(this, GameStatsticsActivity.class);
            intent.putExtra("fromGameActivity", true);
            intent.putExtra("gameId", games.get(0).getId());
            startActivity(intent);
        } else {
            MultiplayerGame mpg = new MultiplayerGame();
            mpg.setGamesIds(games);
            Intent intent = new Intent(this, MultiplayerGameStatistics.class);
            intent.putExtra("fromGameActivity", true);
            intent.putExtra("mpg", mpg);
            startActivity(intent);
        }
        ;
    }


    public void gameDynamicAddUser(View view) {
        gdp.newUser();
    }
}

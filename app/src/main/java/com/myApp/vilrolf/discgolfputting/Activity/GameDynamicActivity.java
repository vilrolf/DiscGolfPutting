package com.myApp.vilrolf.discgolfputting.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.MvpView.GameDynamicView;
import com.myApp.vilrolf.discgolfputting.Presenter.GameDynamicPresenter;
import com.myApp.vilrolf.discgolfputting.Presenter.GameDynamicStreakPresenter;
import com.myApp.vilrolf.discgolfputting.Presenter.GameFlipItPresenter;
import com.myApp.vilrolf.discgolfputting.R;

public class GameDynamicActivity extends AppCompatActivity {
    public TableRow userRowNames;
    public TableRow userRowScores;
    public TableRow userRowHits;
    public TableRow userRowRemaining;
    public TableLayout gameTable;
    private GameDynamicPresenter gdp;
    private GameDynamicStreakPresenter gdsp;
    private GameDynamicView gdw;
    private GameFlipItPresenter gameFlipItPresenter;
    private long gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_dynamic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (gameMode == 2) {
                gdp.nextRound();
            } else if (gameMode == 3) {
                gdsp.nextRound();
            } else if(gameMode == 4){
                gameFlipItPresenter.nextRound();
            }


        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gdw = new GameDynamicView(this);

        userRowNames = (TableRow) findViewById(R.id.tableRowDynamicGameUsers);
        userRowScores = (TableRow) findViewById(R.id.tableRowDynamicGameUsersScore);
        userRowHits = (TableRow) findViewById(R.id.tableRowGameDynamicHits);
        userRowRemaining = (TableRow) findViewById(R.id.tableRowGameDynamicRemaining);
        gameTable = (TableLayout) findViewById(R.id.tableLayoutDynamicGame);
        Button addButton = (Button) findViewById(R.id.buttonDynamicGameAddUser);
        gdw.setUp(userRowNames, userRowScores);
        gdw.setUpDynamic(userRowHits, userRowRemaining, gameTable, addButton);
        GameType gt = (GameType) getIntent().getSerializableExtra("gameType");
        gameMode = gt.getGameMode();


        if (gameMode == 2) {
            setupGdp();
        } else if (gameMode == 3) {
            setupGameDynamicStreakPresenter();
        } else if( gameMode == 4){
            setupGameFlipItPresenter();
        }
    }

    private void setupGameFlipItPresenter() {
        gameFlipItPresenter = new GameFlipItPresenter();
        gameFlipItPresenter.setDb(new DbHelper(this));
        gameFlipItPresenter.setGdw(gdw);
        gameFlipItPresenter.setGameType((GameType) getIntent().getSerializableExtra("gameType"));
        gameFlipItPresenter.setupGdp(this);
    }

    private void setupGameDynamicStreakPresenter() {
        gdsp = new GameDynamicStreakPresenter();
        gdsp.setDb(new DbHelper(this));
        gdsp.setGdw(gdw);
        gdsp.setGameType((GameType) getIntent().getSerializableExtra("gameType"));
        gdsp.setupGdp(this);
    }

    private void setupGdp() {
        gdp = new GameDynamicPresenter();
        gdp.setDb(new DbHelper(this));
        gdp.setGdw(gdw);
        gdp.setGameType((GameType) getIntent().getSerializableExtra("gameType"));
        gdp.setupGdp(this);
    }

    public void gameDynamicAddUser(View view) {
        if (gameMode == 2) {
            gdp.newUser();
        } else if (gameMode == 3) {
            gdsp.newUser();
        }
    }
}

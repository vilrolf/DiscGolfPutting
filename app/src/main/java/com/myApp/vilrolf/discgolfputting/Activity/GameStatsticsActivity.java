package com.myApp.vilrolf.discgolfputting.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.view.LineChartView;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.Game;
import com.myApp.vilrolf.discgolfputting.R;
import com.myApp.vilrolf.discgolfputting.Utils.ChartUtil;

public class GameStatsticsActivity extends AppCompatActivity {
    DbHelper mydb;
    private Game game;
    private boolean fromGameActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mydb = new DbHelper(this);
        game = mydb.getGameFromId(getIntent().getLongExtra("gameId",0)) ;
        fromGameActivity = getIntent().getBooleanExtra("fromGameActivity",false);
        game.setDiscThrows(mydb.getThrowsFromGame(game.getId()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_statstics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (game.getDiscThrows().size() == 0) {
            game.setDiscThrows(mydb.getThrowsFromGame(game.getId()));
        }
        //Toast.makeText(GameStatsticsActivity.this, "size: " + game.getDiscThrows().size(), Toast.LENGTH_SHORT).show();
        loadStatistics();
        setupLineChart();

        //TODO lage new game knapp.
    }

    private void setupLineChart() {
        LineChartView lineChartView = (LineChartView) findViewById(R.id.linechartGameStatistics);
        lineChartView = ChartUtil.setupLineChart(lineChartView, game.getDiscThrows());
        Animation anim = ChartUtil.getAnimation();
        lineChartView.show(anim);

    }

    private void loadStatistics() {
        TextView tvScore = (TextView) findViewById(R.id.textViewGameStatisticsScore);
        tvScore.setText(getString(R.string.score) + " " + game.getRoundedScore());

        TextView tvHits = (TextView) findViewById(R.id.textViewGameStatisticsHitPerc);
        tvHits.setText(getString(R.string.hits) + ": " + game.getHits() + "/" + game.getDiscThrows().size() + " - " + game.getRoundedPercHit() + "%");

        TextView tvPpt = (TextView) findViewById(R.id.textViewGameStatisticsPointsPerThrow);
        tvPpt.setText(getString(R.string.pointsPerThrow) + game.getRoundedPPT());

        TextView tvTimeStamp = (TextView) findViewById(R.id.textViewGameStatisticsTimeStamp);
        tvTimeStamp.setText(game.getCreated_at());

    }

    @Override
    public void onBackPressed() {
        //

        if(fromGameActivity){ // SUPER HACK!
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else {
            super.onBackPressed();
        }
    }

    public void deleteGame(View view) {
        mydb.deleteGame(game.getId(),null);
        onBackPressed();
    }

}

package com.myApp.vilrolf.discgolfputting.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.Game;
import com.myApp.vilrolf.discgolfputting.Database.MultiplayerGame;
import com.myApp.vilrolf.discgolfputting.R;
import com.myApp.vilrolf.discgolfputting.Utils.ChartUtil;
import com.myApp.vilrolf.discgolfputting.Utils.ColorUtil;

import java.util.ArrayList;

public class MultiplayerGameStatistics extends AppCompatActivity {
    ArrayList<Game> games = new ArrayList<>();
    private DbHelper mydb;
    private int[]colors;
    private MultiplayerGame mpg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_game_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        colors = ColorUtil.getColorArray();


        mydb = new DbHelper(this);
        mpg = (MultiplayerGame) getIntent().getSerializableExtra("mpg");
        games = mydb.getGamesFromIds(mpg.getGamesIds());
        if(mpg.getGames().size() == 0){
            mpg.setGames(games);
        }

        for(Game game : games){
            game.setDiscThrows(mydb.getThrowsFromGame(game.getId()));
            game.setUser(mydb.getUserFromId(game.getUserId()));
        }

        loadStatistics();
        setupLineChart();

    }

    private void setupLineChart() {
        LineChartView lcv = (LineChartView) findViewById(R.id.linechartGameStatistics);
        int i = 0;
        for(Game game : games){
            LineSet ls = ChartUtil.setupLineSet(game.getDiscThrows());
            ls.setColor(colors[i]);
            ls.setDotsColor(colors[i]);
            ls.setThickness(Tools.fromDpToPx(3.5f));
            ls.setDotsRadius(Tools.fromDpToPx(5.0f));
            lcv.addData(ls);
            i++;
        }
        lcv.setAxisBorderValues(0, 100, 25);
        lcv.setGrid(4, 0, ChartUtil.setupPaint());
        Animation anim = ChartUtil.getAnimation();
        lcv.show(anim);


    }

    private void loadStatistics() {
        TableRow trName = (TableRow) findViewById(R.id.trMultiName);
        TableRow trScore = (TableRow) findViewById(R.id.trMultiScore);
        TableRow trHit = (TableRow) findViewById(R.id.trMultiHit);
        TableRow trPerc = (TableRow) findViewById(R.id.trMultiPerc);
        TableRow trPpt = (TableRow) findViewById(R.id.trMultiPpt);
       // ADD DISTANCES SOMEDAY MBY
        int i = 0;
        for(Game game : games){
            TextView tvName = new TextView(this);
            tvName.setText(game.getUser().getName());
            tvName.setTextAppearance(this, android.R.style.TextAppearance_Material_Body1);
            tvName.setTextColor(colors[i]);
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            tvName.setPadding(12,0,12,0);
            trName.addView(tvName);

            
            TextView tvScore = new TextView(this);
            tvScore.setText(game.getRoundedScore());
            tvScore.setTextAppearance(this, android.R.style.TextAppearance_Material_Body1);
            tvScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            trScore.addView(tvScore);

            TextView tvHit= new TextView(this);
            tvHit.setText(game.getHits() + "/" + game.getDiscThrows().size());
            tvHit.setTextAppearance(this, android.R.style.TextAppearance_Material_Body1);
            tvHit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            trHit.addView(tvHit);

            TextView tvPerc = new TextView(this);
            tvPerc.setText(game.getRoundedPercHit() + "%");
            tvPerc.setTextAppearance(this, android.R.style.TextAppearance_Material_Body1);
            tvPerc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            trPerc.addView(tvPerc);

            TextView tvPpt = new TextView(this);
            tvPpt.setText(game.getRoundedPPT());
            tvPpt.setTextAppearance(this, android.R.style.TextAppearance_Material_Body1);
            tvPpt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            trPpt.addView(tvPpt);

            i++;
        }
    }


    public void deleteMultiplayerGame(View view) {
        mydb.deleteMultiPlayerGame(mpg);
        //Toast.makeText(this, "Whoopops, not yet implemented", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}

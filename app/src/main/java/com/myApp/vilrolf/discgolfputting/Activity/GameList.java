package com.myApp.vilrolf.discgolfputting.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.Game;
import com.myApp.vilrolf.discgolfputting.Database.MultiplayerGame;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.R;
import com.myApp.vilrolf.discgolfputting.Utils.SpinnerUtil;

import java.util.ArrayList;
import java.util.Collections;

public class GameList extends AppCompatActivity {
    private ArrayList<Game> games;
    private ArrayList<Game> activeGames;
    private ArrayList<MultiplayerGame> multiplayerGames;
    private ArrayList<User> users;
    private Spinner spinnerUser;
    private Spinner spinnerSorter;
    private String sortMethod = "";
    private boolean singlePlayerFlag = true;

    private DbHelper mydb;



    @Override
    public void onResume() {
        loadGamesFromDB();
        sortActiveGames();
        fillList();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mydb = new DbHelper(this);
        users = mydb.getAllUsers();
        loadGamesFromDB();
        spinnerSorter = (Spinner) findViewById(R.id.spinnerSort);
        spinnerUser = (Spinner) findViewById(R.id.spinnerUsers);
        loadSpinnerUser();
        loadSpinnerSort();

    }

    private void loadSpinnerUser() {
        ArrayList<String> spinnerUserValues = new ArrayList<>();
        spinnerUserValues.add(getString(R.string.all));
        spinnerUserValues.add(getString(R.string.multiplayer));
        for (User user : users) {
            spinnerUserValues.add(user.getName());
        }
        spinnerUser.setAdapter(SpinnerUtil.createSpinnerAdapter(spinnerUserValues, this));
        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerUserValues.get(position).equals(getString(R.string.all))) {
                    activeGames = games;
                    singlePlayerFlag = true;

                } else if (spinnerUserValues.get(position).equals(getString(R.string.multiplayer))) {
                    multiplayerGames = mydb.getAllMultiplayerGames();
                    singlePlayerFlag = false;

                } else {
                    User selecetedUser = users.get(position - 2);
                    activeGames = mydb.getAllGamesFromUser(selecetedUser);
                    singlePlayerFlag = true;
                }
                fillList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadSpinnerSort() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sortOptionsContent, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSorter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeSortMethod(position);
                sortActiveGames();
                fillList();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerSorter.setAdapter(adapter);
    }

    public void changeSortMethod(int pos) {
        sortMethod = getResources().getStringArray((R.array.sortOptionsContent))[pos];
    }

    private void fillList() {

        if(singlePlayerFlag){
            ArrayAdapter<Game> adapter;
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, activeGames);
            ListView lw = (ListView) findViewById(R.id.listView);
            lw.setOnItemClickListener((parent, view, position, id) -> {
                long realId = games.get((int) id).getId();
                openGameStatistics(realId);
            });
            lw.setAdapter(adapter);
        } else {
            ArrayAdapter<MultiplayerGame> adapter;
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, multiplayerGames);
            ListView lw = (ListView) findViewById(R.id.listView);
            lw.setOnItemClickListener((parent, view, position, id) -> {
                MultiplayerGame mpg = multiplayerGames.get((int) id);
                openMultiplayerGameStatistics(mpg);
            });
            lw.setAdapter(adapter);
        }


    }



    private void sortActiveGames() {
        switch (sortMethod) {
            case "Time": {
                Collections.sort(activeGames, (a, b) -> a.getId() > b.getId() ? 1 : a.getId() == b.getId() ? 0 : -1);
                break;
            }
            case "Score": {
                Collections.sort(activeGames, (a, b) -> a.getScore() < b.getScore() ? 1 : a.getScore() == b.getScore() ? 0 : -1);
                break;
            }
            case "Hit%": {
                //TODO
                break;
            }
            default: {
                break;
            }
        }
    }

    public void loadGamesFromDB() {
        games = mydb.getAllGames();
        for (Game game : games) {
            game.setUser(mydb.getUserFromId(game.getUserId()));
        }
        activeGames = games;
        if(!singlePlayerFlag){
            multiplayerGames = mydb.getAllMultiplayerGames();
        }

    }

    public void openGameStatistics(long id) {
        Intent intent = new Intent(this, GameStatsticsActivity.class);
        intent.putExtra("gameId", id);
        startActivity(intent);
    }

    private void openMultiplayerGameStatistics(MultiplayerGame mpg) {
        Intent intent = new Intent(this, MultiplayerGameStatistics.class);
        intent.putExtra("mpg", mpg);
        startActivity(intent);
    }


}

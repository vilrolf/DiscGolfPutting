package com.myApp.vilrolf.discgolfputting.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.Game;
import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.Database.MultiplayerGame;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.R;
import com.myApp.vilrolf.discgolfputting.Utils.SpinnerUtil;

import java.util.ArrayList;
import java.util.Collections;

public class GameList extends AppCompatActivity {
    int currentGameTypeId = -1;
    private ArrayList<Game> allGames;
    private ArrayList<Game> activeGames;
    private ArrayList<MultiplayerGame> allMultiplayerGames;
    private ArrayList<MultiplayerGame> activeMultiplayerGames;
    private ArrayList<User> users;
    private Spinner spinnerUser;
    private String sortMethod = "";
    private boolean singlePlayerFlag = true;
    private DbHelper mydb;


    @Override
    public void onResume() {
        loadGamesFromDB();
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
        spinnerUser = (Spinner) findViewById(R.id.spinnerGametypes);
        loadSpinnerGameType();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sort:
                //showPopupSort();
                View menuitemViewSort = findViewById(R.id.sort);
                showPopupSort(menuitemViewSort);
                return true;
            case R.id.filter:
                View menuItemViewFilter = findViewById(R.id.filter);
                showPopupFilter(menuItemViewFilter);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showPopupFilter(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        ArrayList<String> spinnerUserValues = new ArrayList<>();
        spinnerUserValues.add(getString(R.string.all));
        spinnerUserValues.add(getString(R.string.multiplayer));
        for (User user : users) {
            spinnerUserValues.add(user.getName());
        }
        for (String s : spinnerUserValues) {
            popup.getMenu().add(s);
        }
        popup.setOnMenuItemClickListener(item -> {
            Toast.makeText(this, item.getTitle() + " " + item.getOrder(), Toast.LENGTH_SHORT).show();

            if (item.getTitle().equals(getString(R.string.all))) {
                activeGames = allGames;
                singlePlayerFlag = true;

            } else if (item.getTitle().equals(getString(R.string.multiplayer))) {
                allMultiplayerGames = mydb.getAllMultiplayerGames();
                singlePlayerFlag = false;
            } else {
                for (User user : users) {
                    if (item.getTitle().equals(user.getName())) {
                        activeGames = mydb.getAllGamesFromUser(user);
                        singlePlayerFlag = true;
                    }
                }
            }
            fillList();
            return true;
        });

        popup.show();

    }


    public void showPopupSort(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_filter, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            Toast.makeText(GameList.this, item.getTitle(), Toast.LENGTH_SHORT).show();
            switch (item.getItemId()) {
                case R.id.menu_score:
                    sortMethod = "Score";
                    break;

                case R.id.menu_time:
                    sortMethod = "Time";
                    break;
            }
            fillList();
            return true;
        });

        popup.show();
    }


    private void loadSpinnerGameType() {
        ArrayList<GameType> allGameTypes = mydb.getAllGameTypes();
        ArrayList<String> spinnerGameTypeValues = new ArrayList<>();
        spinnerGameTypeValues.add("All Gametypes");
        for (GameType gameType : mydb.getAllGameTypes()) {
            spinnerGameTypeValues.add(gameType.getDisplayName());
        }
        spinnerUser.setAdapter(SpinnerUtil.createSpinnerAdapter(spinnerGameTypeValues, this));
        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) { // ALL
                    currentGameTypeId = -1;

                } else {
                    currentGameTypeId = (int) allGameTypes.get(position - 1).getId();

                }
                fillList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void fillList() {


        if (currentGameTypeId != -1) {
            if (singlePlayerFlag) {
                activeGames = new ArrayList<>();
                for (Game game : allGames) {
                    if (game.getGameTypeId() == currentGameTypeId) {
                        activeGames.add(game);
                    }
                }
            } else { // multiplayer
                activeMultiplayerGames = new ArrayList<>();
                for (MultiplayerGame multiplayerGame : allMultiplayerGames) {
                    if (multiplayerGame.getGames().get(0).getGameTypeId() == currentGameTypeId) {
                        activeMultiplayerGames.add(multiplayerGame);
                    }
                }
            }

        } else {
            activeGames = allGames;
            activeMultiplayerGames = allMultiplayerGames;
        }
        sortActiveGames();

        if (singlePlayerFlag) {
            ArrayAdapter<Game> adapter;
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, activeGames);
            ListView lw = (ListView) findViewById(R.id.listView);
            lw.setOnItemClickListener((parent, view, position, id) -> {
                long realId = activeGames.get((int) id).getId();
                openGameStatistics(realId);
            });
            lw.setAdapter(adapter);
        } else {
            ArrayAdapter<MultiplayerGame> adapter;
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, activeMultiplayerGames);
            ListView lw = (ListView) findViewById(R.id.listView);
            lw.setOnItemClickListener((parent, view, position, id) -> {
                MultiplayerGame mpg = activeMultiplayerGames.get((int) id);
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
        allGames = mydb.getAllGames();
        for (Game game : allGames) {
            game.setUser(mydb.getUserFromId(game.getUserId()));
        }
        activeGames = allGames;
        if (!singlePlayerFlag) {
            allMultiplayerGames = mydb.getAllMultiplayerGames();
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

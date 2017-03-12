package com.myApp.vilrolf.discgolfputting.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.Game;
import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.Database.MultiplayerGame;
import com.myApp.vilrolf.discgolfputting.Database.Throw;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.R;
import com.myApp.vilrolf.discgolfputting.Utils.ColorUtil;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private TextView textViewScore;
    private DbHelper mydb;
    private String distanceMarker;
    private ArrayList<User> allUsers;
    private ArrayList<User> unusedUsers;
    private ArrayList<Game> games = new ArrayList<>();
    private GameType gameType;
    private TableLayout gameTable;
    private TableLayout userTable;
    private TableRow userRowNames;
    private TableRow userRowScores;
    private ArrayList<User> activeUsers;
    private ArrayList<TextView> textViewScores;
    private ArrayList<Spinner> spinners = new ArrayList<Spinner>();
    private int colors[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gameType = (GameType) getIntent().getSerializableExtra("gameType");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        distanceMarker = sharedPref.getString(getString(R.string.distance_type), "");

        mydb = new DbHelper(this);
        allUsers = mydb.getAllUsers();
        activeUsers = new ArrayList<User>();
        unusedUsers = allUsers;

        colors = ColorUtil.getColorArray();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> saveAndGoToStatistics());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gameTable = (TableLayout) findViewById(R.id.gameTableLayout);
        userTable = (TableLayout) findViewById(R.id.tableLayoutGameUser);
        userRowNames = (TableRow) findViewById(R.id.tableRowUsers);
        userRowScores = (TableRow) findViewById(R.id.tableRowUsersScore);

        addUserToGameActivity(unusedUsers.get(0));


    }

    private void saveAndGoToStatistics() {
        MultiplayerGame multiplayerGame = new MultiplayerGame();
        if(games.size() > 1){
            multiplayerGame.setId(mydb.createMultiplayerGame());
        }

        for (Game game : games) {
            if(games.size() > 1){
                game.setMultiplayerId(multiplayerGame.getId());
                multiplayerGame.addGameId(game.getId());
            } else {
                game.setMultiplayerId(-1);
            }
            game.setGameType(gameType);
            long id = mydb.createGame(game);
            game.setId(id);
            for (Throw th : game.getDiscThrows()) {
                th.setGameId(id);
                th.setUser(game.getUser());
                mydb.createThrow(th);
            }
        }
        openGameStatistics(multiplayerGame);
    }



    public void gameAddUser(View view) {
        if (unusedUsers.size() == 0) {
            createDialogNewUser();
        } else {
            createDialogNewActiveUser();
        }
    }

    private void addUserToGameActivity(User user) {
        activeUsers.add(user);
        unusedUsers.remove(user);

        Game game = new Game();
        game.setGameType(gameType);
        game.setUser(user);
        game.setColor(colors[activeUsers.indexOf(game.getUser()) % colors.length]);
        game.setUpThrows();
        games.add(game);
        addUserButton(game);
        addUserTextViewScore(game);
        createGameTable();
    }

    private void addUserTextViewScore(Game game) {
        TextView newScoreTextView = new TextView(this);
        newScoreTextView.setTextAppearance(this, android.R.style.TextAppearance_Material_Display1);
        newScoreTextView.setTextColor(game.getColor());
        newScoreTextView.setText("" + game.getScore());
        game.setTvScore(newScoreTextView);
        userRowScores.addView(newScoreTextView, activeUsers.size() - 1);
    }

    private void createDialogNewActiveUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] unusedUsersNames = new String[unusedUsers.size() + 1];
        for (int i = 0; i < unusedUsers.size(); i++) {
            unusedUsersNames[i] = unusedUsers.get(i).getName();
        }
        unusedUsersNames[unusedUsers.size()] = getString(R.string.create_new_user);

        builder.setTitle(R.string.select_user)
                .setItems(unusedUsersNames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == (unusedUsers.size())) {
                            createDialogNewUser();
                        } else {
                            addUserToGameActivity(unusedUsers.get(which));
                        }
                    }
                });
        builder.create();
        builder.show();
    }

    private void addUserButton(Game game) {
        Button newUserButton = new Button(this);
        newUserButton.setText(game.getUser().getName());
        game.setBtnScore(newUserButton);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Toast.makeText(GameActivity.this, "user removed", Toast.LENGTH_SHORT).show();
                                removeUser(game);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setMessage(getString(R.string.are_you_sure_you_want_to_remove_player)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.no), dialogClickListener).show();
            }
        });
        userRowNames.addView(newUserButton, activeUsers.size() - 1);
    }

    private void removeUser(Game game) {
        activeUsers.remove(game.getUser());
        unusedUsers.add(game.getUser());
        userRowNames.removeView(game.getBtnScore());
        userRowScores.removeView(game.getTvScore());
        games.remove(game);
        createGameTable();
    }

    private void createDialogNewUser() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.create_user_mame);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(input);
        // TODO ADD CANCEL
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if (value.equals("")) {
                    // ERROR NO IDEA IF THIS WORKKS
                    // alert.show();
                    Toast.makeText(GameActivity.this, "Need to enter a username", Toast.LENGTH_SHORT).show();
                    createDialogNewUser();
                } else {
                    User user = new User(value);
                    user.setId(mydb.createUser(new User(value)));
                    allUsers.add(user);
                    addUserToGameActivity(user);
                }
            }
        });
        alert.show();
    }

    private void createGameTable() {
        gameTable.removeAllViews();

        for (int i = 0; i < gameType.getRounds(); i++) {
            for (Game game : games) {
                createRound(i, game);
            }
            View ruler = new View(this);
            ruler.setBackgroundColor(0xFF000000);
            gameTable.addView(ruler,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
        }
    }

    private void createRound(int i, Game game) {

        //TODO IDK, make it better plz
        TableRow tableRow = new TableRow(this);
        TextView textViewDist = new TextView(this);


        textViewDist.setTextColor(game.getColor());
        double distance;

        distance = gameType.getStart() + (gameType.getIncrement() * i);

        textViewDist.setText("" + distance + distanceMarker);
        tableRow.addView(textViewDist);

        for (int j = 0; j < gameType.getNrOfThrowsPerRound(); j++) {
            Throw th;

            th = game.getDiscThrows().get((i* (int) gameType.getNrOfThrowsPerRound()) +j);

            CheckBox checkBox = new CheckBox(this);
            if (gameType.getNrOfThrowsPerRound() > 4) {
                checkBox.setButtonDrawable(R.drawable.custom_checkbox_design_medium);
            } else {
                checkBox.setButtonDrawable(R.drawable.custom_checkbox_design_big);
            }
            if(th.isHit()){
                checkBox.setChecked(true);
            }
            checkBox.setTag(th);
            tableRow.addView(checkBox);
            checkBox.setOnClickListener(v -> {
                CheckBox cb = (CheckBox) v;
                Throw kastet = (Throw) v.getTag();
                kastet.setHit(true);
                if (!cb.isChecked()) {
                    kastet.setHit(false);
                }
                updateScore(game);
            });
        }
        gameTable.addView(tableRow);
    }

    private void updateScore(Game game) {
        game.getAndUpdateScore();
        game.getTvScore().setText(game.getRoundedScore());
    }

    public void openGameStatistics(MultiplayerGame multiplayerGame) {

        if(games.size() == 1){
            Intent intent = new Intent(this, GameStatsticsActivity.class);
            intent.putExtra("fromGameActivity",true);
            intent.putExtra("gameId", games.get(0).getId());
            startActivity(intent);
        } else {
            MultiplayerGame mpg = new MultiplayerGame();
            mpg.setGamesIds(games);
            Intent intent = new Intent(this, MultiplayerGameStatistics.class);
            intent.putExtra("mpg", mpg);
            startActivity(intent);
        }
    }


}



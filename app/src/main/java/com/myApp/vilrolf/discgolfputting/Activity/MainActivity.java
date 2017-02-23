package com.myApp.vilrolf.discgolfputting.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DbHelper mydb;
    private List<GameType> gameTypeList;
    private Spinner gameTypesSpinner;
    private boolean noGameTypes = false;
    private String distanceMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new DbHelper(this);

        if (mydb.getAllUsers().size() == 0) {
            createDialogNewUser();
        }
        // SJEKK FOR HVOR MANGE DISCER
        SharedPreferences sharedPref = getSharedPreferences("PREF_DISTANCE", Context.MODE_PRIVATE);
        distanceMarker = sharedPref.getString(getString(R.string.distance_type), "empty");
        if (distanceMarker.equals("empty")) {
            createDistanceListDialog();
        }
        checkGameTypes();
    }

    private void checkGameTypes() {
        gameTypeList = mydb.getAllGameTypes();
        SharedPreferences sharedPref = getSharedPreferences("PREF_DISTANCE", Context.MODE_PRIVATE);
        distanceMarker = sharedPref.getString(getString(R.string.distance_type), "empty");
        gameTypesSpinner = (Spinner) findViewById(R.id.spinnerMainActivityGameTypes);
        boolean hasDynamic = false;
        boolean hasStandard = false;
        boolean hasStreak = false;
        if (gameTypeList.size() == 0) {
            createHowManyDiscsDialog();

        } else { // yeh, this is getting stupid now
            for (GameType gameType : gameTypeList) {
                if (gameType.getGameMode() == 1) {
                    hasStandard = true;
                }

                if (gameType.getGameMode() == 2) {
                    hasDynamic = true;
                }
                if (gameType.getGameMode() == 3) {
                    hasStreak = true;
                }
            }
            if (!hasDynamic) {
                mydb.createStandardDynamicGameType((int) gameTypeList.get(0).getNrOfThrowsPerRound(), distanceMarker);
                gameTypeList = mydb.getAllGameTypes();
            }
            if (!hasStandard) {
                mydb.createStandardGameType((int) gameTypeList.get(0).getNrOfThrowsPerRound(), distanceMarker);
                gameTypeList = mydb.getAllGameTypes();
            }
            if (!hasStreak) {
                mydb.createStandardStreakGame((int) gameTypeList.get(0).getNrOfThrowsPerRound(), distanceMarker);
                gameTypeList = mydb.getAllGameTypes();
            }

            gameTypesSpinner.setAdapter(createSpinnerAdapter());
        }

    }

    private void createDialogNewUser() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.create_user_mame);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(input);
        alert.setPositiveButton("Ok", (dialog, whichButton) -> {
            String value = input.getText().toString();
            if (value.equals("")) {
                // ERROR
                Toast.makeText(MainActivity.this, "You need to enter a username", Toast.LENGTH_SHORT).show();
                alert.show();
            } else {
                User user = new User(value);
                user.setId(mydb.createUser(new User(value)));
                mydb.setAllGamesToUser(user);
                Toast.makeText(MainActivity.this, "User created", Toast.LENGTH_SHORT).show();
            }

        });
        alert.show();
    }

    private ArrayAdapter<String> createSpinnerAdapter() {
        String[] gameTypeNames;
        gameTypeNames = new String[gameTypeList.size()];
        for (int i = 0; i < gameTypeNames.length; i++) {
            gameTypeNames[i] = gameTypeList.get(i).getDisplayName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gameTypeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;

    }

    public void createDistanceListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.imperial_or_metric)
                .setItems(R.array.distance_types, (dialog, which) -> {
                    // The 'which' argument contains the index position
                    // of the selected item

                    SharedPreferences sharedPref = getSharedPreferences("PREF_DISTANCE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    // HACK
                    String distanceMarker1 = "ft";
                    if (which == 1) {
                        distanceMarker1 = "m";

                    }
                    editor.putString(getString(R.string.distance_type), distanceMarker1);
                    editor.apply();
                    if (noGameTypes) {
                        createHowManyDiscsDialog();
                    }

                });
        builder.create();
        builder.show();
    }

    public void createHowManyDiscsDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.how_many_discs_per_round);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setPositiveButton("Ok", (dialog, whichButton) -> {
            String value = input.getText().toString();
            int n = Integer.parseInt(value);
            if (n <= 0) {
                // SHIT IS WRONG
            } else {
                createStandardGameTypes(n);
            }
        });
        alert.show();
    }

    private void createStandardGameTypes(int n) {
        SharedPreferences sharedPref = getSharedPreferences("PREF_DISTANCE", Context.MODE_PRIVATE);
        String distanceMarker = sharedPref.getString(getString(R.string.distance_type), "empty");
        mydb.createStandardGameType(n, distanceMarker);
        mydb.createStandardDynamicGameType(n, distanceMarker);
        gameTypeList = mydb.getAllGameTypes();
        gameTypesSpinner.setAdapter(createSpinnerAdapter());
    }

    @Override
    public void onResume() {
        checkGameTypes();
        super.onResume();
    }

    public void goToGameActivity(View view) {
        // CHECK IF THIS IS THE FIRST TIME MATE!

        SharedPreferences sharedPref = getSharedPreferences("PREF_DISTANCE", Context.MODE_PRIVATE);
        String distanceMarker = sharedPref.getString(getString(R.string.distance_type), "empty");
        if (distanceMarker.equals("empty")) {
            createDistanceListDialog();
        } else {
            if (noGameTypes) {
                createHowManyDiscsDialog();
            } else {
                GameType selectedGameType = gameTypeList.get(gameTypesSpinner.getSelectedItemPosition());
                if (selectedGameType.getGameMode() >= 2) {
                    Intent intent = new Intent(this, GameDynamicActivity.class);
                    intent.putExtra("gameType", selectedGameType);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, GameActivity.class);
                    intent.putExtra("gameType", selectedGameType);
                    startActivity(intent);
                }

            }
        }
    }

    public void goToDbTest(View view) {

        Intent intent = new Intent(this, GameList.class);
        startActivity(intent);
    }


    public void goToGameNewGameType(View view) {
        Intent intent = new Intent(this, NewGameType.class);
        startActivity(intent);
    }

    public void goToStatistics(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }


    public void goToSettings(View view) {
        Intent intent = new Intent(this, AppSettingsActivity.class);
        startActivity(intent);
    }

    public void goToProgress(View view) {
        Intent intent = new Intent(this, ProgressActivity.class);
        startActivity(intent);
    }
}

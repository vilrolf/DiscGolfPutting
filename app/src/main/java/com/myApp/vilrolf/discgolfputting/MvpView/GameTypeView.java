package com.myApp.vilrolf.discgolfputting.MvpView;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.myApp.vilrolf.discgolfputting.Database.GameType;
import com.myApp.vilrolf.discgolfputting.R;

import java.util.ArrayList;


/**
 * Created by Viljar on 18-Feb-17.
 */

public class GameTypeView {
    private TableLayout table;
    private Spinner spinner;
    private int gameMode = 0;
    private Context context;
    private ArrayList<EditText> editTexts = new ArrayList<>();

    public GameTypeView(Context context) {
        this.context = context;
    }

    public void setTable(TableLayout table) {
        this.table = table;
    }

    public void createSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.gameTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gameMode = position + 1;
                createTable();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setAdapter(adapter);
    }

    private void createTable() {
        SharedPreferences sharedPref = context.getSharedPreferences("PREF_DISTANCE",Context.MODE_PRIVATE);
        String distanceMarker = sharedPref.getString(context.getString(R.string.distance_type), "empty");
        editTexts = new ArrayList<>();
        table.removeAllViews();
        String[] standardValues = context.getResources().getStringArray(R.array.gameTypeStandardValues);
        if(distanceMarker.equals("m")){
            standardValues = context.getResources().getStringArray(R.array.gameTypeStandardValuesMeter);
        }
        String[] standardFields = context.getResources().getStringArray(R.array.gameTypeStandardFields);

        for (int i = 0; i < standardFields.length; i++) {
            TableRow tr = new TableRow(context);
            TextView tv = new TextView(context);
            tv.setText(standardFields[i]);
            EditText editText = new EditText(context);
            //editText.setHint(standardFields[i]);
            editText.setTag(standardFields[i]);
            editText.setText(standardValues[i]);
            if (i == 0) { // STRING
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (i == 1 || i == 2) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (i > 2) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }

            editTexts.add(editText);
            tr.addView(tv);
            tr.addView(editText);
            table.addView(tr);
        }
        if (gameMode == 2) {
            String[] dynamicFields = context.getResources().getStringArray(R.array.gameTypeDynamicFields);
            String[] dynamicValues = context.getResources().getStringArray(R.array.gameTypeDynamicValues);
            for (int i = 0; i < dynamicFields.length; i++) {
                TableRow tr = new TableRow(context);
                TextView tv = new TextView(context);
                tv.setText(dynamicFields[i]);
                EditText editText = new EditText(context);
                editText.setText(dynamicValues[i]);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setTag(dynamicFields[i]);
                editTexts.add(editText);
                tr.addView(tv);
                tr.addView(editText);
                table.addView(tr);
            }
        }
    }

    /*
     <item>Game type name</item> 0
        <item>Number of rounds</item>1
        <item>Number of throws per round</item>2
        <item>First shot multiplier</item>3
        <item>Last shot multiplier</item>4
        <item>All shot hit bonus</item>5
        <item>Start distance</item>6
        <item>Distance increment</item>7
        <item>Points per distance</item>8
     */
    public GameType saveTable() {
        boolean error = false;
        String[] standardFields = context.getResources().getStringArray(R.array.gameTypeStandardFields);

        String[] dynamicFields = context.getResources().getStringArray(R.array.gameTypeDynamicFields);
        GameType gameType = new GameType();
        gameType.setGameMode(gameMode);
        for (int i = 0; i < editTexts.size(); i++) {
            String currentString = editTexts.get(i).getText().toString();
            if (TextUtils.isEmpty(currentString)) {
                editTexts.get(i).setError(context.getString(R.string.error_empty));
                error = true;
            }
            if (i == 1 || i == 2) { // INTS

                long l = Long.parseLong(editTexts.get(i).getText().toString());
                if (l == 0) {
                    editTexts.get(i).setError(context.getString(R.string.error_zero));
                    error = true;
                }
            } else if(i == 3 || i == 4 || i == 7 || i == 8){
                double d = Double.parseDouble(editTexts.get(i).getText().toString());
                if(d == 0){
                    editTexts.get(i).setError(context.getString(R.string.error_zero));
                    error = true;
                }
            }

            if(!error){
                switch (i) {
                    case 0:
                        gameType.setGameName(editTexts.get(0).getText().toString());
                        break;
                    case 1:
                        gameType.setRounds(Long.parseLong(editTexts.get(1).getText().toString()));
                        break;
                    case 2:
                        gameType.setNrOfThrowsPerRound(Long.parseLong(editTexts.get(2).getText().toString()));
                        break;
                    case 3:
                        gameType.setFirstShotMultiplier(Double.parseDouble(editTexts.get(3).getText().toString()));
                        break;
                    case 4:
                        gameType.setLastShotMultiplier(Double.parseDouble(editTexts.get(4).getText().toString()));
                        break;
                    case 5:
                        gameType.setAllShotHitBonus(Double.parseDouble(editTexts.get(5).getText().toString()));
                        break;
                    case 6:
                        gameType.setStart(Double.parseDouble(editTexts.get(6).getText().toString()));
                        break;
                    case 7:
                        gameType.setIncrement(Double.parseDouble(editTexts.get(7).getText().toString()));
                        break;
                    case 8:
                        gameType.setPointsPerDistance(Double.parseDouble(editTexts.get(8).getText().toString()));
                        break;
                }
            }


            if (gameMode == 2) {
                if (editTexts.get(i).getTag().equals(dynamicFields[0])) {
                    long up = Long.parseLong(editTexts.get(i).getText().toString());
                    if(up > gameType.getNrOfThrowsPerRound()){
                        editTexts.get(i).setError(context.getString(R.string.upgrade_error));
                        error = true;
                    } else {
                        gameType.setThresholdUpgrade(up);
                    }
                    
                } else if (editTexts.get(i).getTag().equals(dynamicFields[1])) {
                    long down = Long.parseLong(editTexts.get(i).getText().toString());
                    if(down > gameType.getNrOfThrowsPerRound()){
                        editTexts.get(i).setError(context.getString(R.string.downgrade_error));
                        error = true;
                    } else {
                        gameType.setThresholdDowngrade(down);
                    }
                    gameType.setThresholdDowngrade(Long.parseLong(editTexts.get(i).getText().toString()));
                }
            }
        }
        if(error) return null;
        return gameType;


    }

    public void setSpinner(Spinner spinner) {
        this.spinner = spinner;

    }
}

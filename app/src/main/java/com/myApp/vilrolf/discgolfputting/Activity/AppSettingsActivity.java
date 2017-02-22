package com.myApp.vilrolf.discgolfputting.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.myApp.vilrolf.discgolfputting.Database.DbClass;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.R;

import java.io.Serializable;
import java.util.ArrayList;

public class AppSettingsActivity extends AppCompatActivity {
    private DbHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mydb = new DbHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

    }

    public void changeMetricOrImperial(View view) {
        createDistanceListDialog();
    }

    public void createDistanceListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.imperial_or_metric)
                .setItems(R.array.distance_types, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        SharedPreferences sharedPref = getSharedPreferences("PREF_DISTANCE",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        // HACK
                        String distanceMarker = "ft";
                        if (which == 1) {
                            distanceMarker = "m";
                        }
                        editor.putString(getString(R.string.distance_type), distanceMarker);
                        editor.apply();

                    }
                });
        builder.create();
        builder.show();
    }


    public void setAllGames(View view) {
        mydb.setAllGamesToUser(new User(1));

    }

    public void openUserList(View view) {
        Intent intent = new Intent(this, GenericListActivity.class);
        intent.putExtra("list",mydb.getAllUsers());
        startActivity(intent);
       // ArrayList<? extends DbClass> list = mydb.getAllUsers();

    }

    public void openGameTypeList(View view) {
        Intent intent = new Intent(this, GenericListActivity.class);
        intent.putExtra("list", mydb.getAllGameTypes());
        startActivity(intent);
    }

    public void goToProgress(View view) {
        Intent intent = new Intent(this, ProgressActivity.class);
        startActivity(intent);
    }
}

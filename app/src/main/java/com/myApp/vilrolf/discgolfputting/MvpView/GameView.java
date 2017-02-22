package com.myApp.vilrolf.discgolfputting.MvpView;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.db.chart.Tools;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.Engine.GameEngine;
import com.myApp.vilrolf.discgolfputting.Engine.GameEngineDynamic;
import com.myApp.vilrolf.discgolfputting.Presenter.GamePresenter;
import com.myApp.vilrolf.discgolfputting.R;

import java.util.ArrayList;

/**
 * Created by Viljar on 16-Feb-17.
 */

public class GameView {
    private Context context;
    public TableRow userRowNames;
    public TableRow userRowScores;
    public TableRow userRowHits;
    public TableLayout gameTable;
    private GamePresenter gamePresenter;


    public GameView(Context context){this.context = context;}

    public void addUserButton(GameEngine ge) {
        Button newUserButton = new Button(context);
        newUserButton.setText(ge.getGame().getUser().getName());
        ge.setBtnScore(newUserButton);
        newUserButton.setOnClickListener(v -> {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            Toast.makeText(gamePresenter.context, "user removed", Toast.LENGTH_SHORT).show();
                            gamePresenter.removeUser(ge);
                            // Toast.makeText(GameDynamicActivity.context, "remove", Toast.LENGTH_SHORT).show();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getString(R.string.are_you_sure_you_want_to_remove_player)).setPositiveButton(context.getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(context.getString(R.string.no), dialogClickListener).show();
        });
        userRowNames.addView(newUserButton, gamePresenter.activeUsers.size());
    }

    public void setUp(TableRow userRowNames, TableRow userRowScores) {
        this.userRowNames = userRowNames;
        this.userRowScores = userRowScores;
    }

    public void removeGame(GameEngine gameEngine) {
        userRowNames.removeView(gameEngine.getBtnScore());
        userRowScores.removeView(gameEngine.getTvScore());
    }

    public void addUserTextViewScore(GameEngine ge) {
        TextView newScoreTextView = new TextView(context);
        newScoreTextView.setTextAppearance(context, android.R.style.TextAppearance_Material_Display1);
        newScoreTextView.setTextSize(Tools.fromDpToPx(15));
        newScoreTextView.setTextColor(ge.getColor());
        newScoreTextView.setText("" + ge.getScore());
        ge.setTvScore(newScoreTextView);
        userRowScores.addView(newScoreTextView, gamePresenter.activeUsers.size());
    }

    public void setGamePresenter(GamePresenter gamePresenter) {
        this.gamePresenter = gamePresenter;
    }


    public void createDialogCreateNewUser() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(R.string.create_user_mame);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(input);
        // TODO ADD CANCEL
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if (value.equals("")) {
                    // ERROR NO IDEA IF THIS WORKKS
                    // alert.show();
                    Toast.makeText(context, "Need to enter a username", Toast.LENGTH_SHORT).show();
                    createDialogCreateNewUser();
                } else {
                    gamePresenter.createNewUser(value);
                }
            }
        });
        alert.show();
    }

    public void createDialogSelectUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ArrayList<User> unusedUsers = gamePresenter.getUnusedUsers();
        String[] unusedUsersNames = new String[unusedUsers.size() + 1];
        for (int i = 0; i < unusedUsersNames.length -1; i++) {
            unusedUsersNames[i] = unusedUsers.get(i).getName();
        }
        unusedUsersNames[gamePresenter.getUnusedUsers().size()] = context.getString(R.string.create_new_user);

        builder.setTitle(R.string.select_user)
                .setItems(unusedUsersNames, (dialog, which) -> {
                    if (which == (unusedUsers.size())) {
                        createDialogCreateNewUser();
                    } else {
                        gamePresenter.addUser(gamePresenter.getUnusedUsers().get(which),null);
                    }
                });
        builder.create();
        builder.show();
    }

    public void updateScore(GameEngine ge) {
        ge.getTvScore().setText(ge.getScore());
    }
}

package com.myApp.vilrolf.discgolfputting.MvpView;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.db.chart.Tools;
import com.myApp.vilrolf.discgolfputting.Database.Throw;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.Engine.GameEngine;
import com.myApp.vilrolf.discgolfputting.Engine.GameEngineDynamic;
import com.myApp.vilrolf.discgolfputting.Presenter.GameDynamicPresenter;
import com.myApp.vilrolf.discgolfputting.R;

import java.util.ArrayList;

/**
 * Created by Viljar on 16-Feb-17.
 */

public class GameDynamicView extends GameView {
    private TableRow userRowRemaining;
    private TableRow userRowHits;
    private Button addButton;
    private Context context;
    private GameDynamicPresenter gameDynamicPresenter;
    private GameEngineDynamic allRowChecked;

    public GameDynamicView(Context context) {
        super(context);
        this.context = context;

    }
    //public void removeUser()

    public void setUpDynamic(TableRow userRowHits, TableRow userRowRemaining, TableLayout gameTable, Button addButton) {
        this.userRowHits = userRowHits;
        this.userRowRemaining = userRowRemaining;
        this.gameTable = gameTable;
        this.addButton = addButton;
    }

    public void addUserTvHits(GameEngineDynamic ged) {
        TextView tvHits = new TextView(context);
        tvHits.setText(0 + "/" + 0);
        ged.setTvHits(tvHits);
        userRowHits.addView(ged.getTvHits());
    }

    public void addUserTvRemaining(GameEngineDynamic ged) {
        int remaining = ged.getRemaining();
        TextView tvRemaining = new TextView(context);
        tvRemaining.setText("" + remaining);
        ged.setTvRemaining(tvRemaining);
        userRowRemaining.addView(ged.getTvRemaining());
    }

    public void createRound(GameEngineDynamic ged) {
        TableRow tr = new TableRow(context);
        TextView tvDistance = new TextView(context);
        tvDistance.setText("" + ged.getCurrentDistanceRounded());
        tvDistance.setTextColor(ged.getColor());
        tvDistance.setTextSize(Tools.fromDpToPx(10));
        tvDistance.setPadding(0, 0, 10, 0);
        tr.addView(tvDistance);

        ged.checkBoxes = new ArrayList<>();

        for (Throw dthrow : ged.getCurrentDiscThrows()) {
            CheckBox cb = new CheckBox(context);
            cb.setTag(dthrow);
            cb.setButtonDrawable(R.drawable.custom_checkbox_design_medium);
            cb.setOnClickListener(v -> {
                CheckBox checkBox = (CheckBox) v;
                Throw th = (Throw) v.getTag();
                th.setHit((checkBox.isChecked()));
            });
            tr.addView(cb);
            ged.checkBoxes.add(cb);
        }
        Button allHitButton = new Button(context);
        allHitButton.setText(R.string.All);
        allHitButton.setOnClickListener(v -> {
            gameDynamicPresenter.setAllHitRound(ged);
        }
        );
        tr.addView(allHitButton);

        ged.setGameRow(tr);
        gameTable.addView(tr);

    }

    public void removeGame(GameEngineDynamic gameEngineDynamic) {
        super.removeGame(gameEngineDynamic);
        userRowHits.removeView(gameEngineDynamic.getTvHits());
        userRowRemaining.removeView(gameEngineDynamic.getTvRemaining());
        gameTable.removeView(gameEngineDynamic.getGameRow());
    }

    public void clearGameTable() {
        gameTable.removeAllViews();
    }

    public void addUserButton(GameEngineDynamic gameEngineDynamic) {
        Button newUserButton = new Button(context);
        newUserButton.setText(gameEngineDynamic.getGame().getUser().getName());
        gameEngineDynamic.setBtnScore(newUserButton);
        newUserButton.setOnClickListener(v -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Toast.makeText(gameDynamicPresenter.context, "user removed", Toast.LENGTH_SHORT).show();
                        gameDynamicPresenter.removeUser(gameEngineDynamic);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getString(R.string.are_you_sure_you_want_to_remove_player)).setPositiveButton(context.getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(context.getString(R.string.no), dialogClickListener).show();
        });
        userRowNames.addView(newUserButton, gameDynamicPresenter.getActiveUsers().size());
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
                    gameDynamicPresenter.createNewUser(value);
                }
            }
        });
        alert.show();
    }

    public void createDialogSelectUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ArrayList<User> unusedUsers = gameDynamicPresenter.getUnusedUsers();
        String[] unusedUsersNames = new String[unusedUsers.size() + 1];
        for (int i = 0; i < unusedUsersNames.length - 1; i++) {
            unusedUsersNames[i] = unusedUsers.get(i).getName();
        }
        unusedUsersNames[gameDynamicPresenter.getUnusedUsers().size()] = context.getString(R.string.create_new_user);

        builder.setTitle(R.string.select_user)
                .setItems(unusedUsersNames, (dialog, which) -> {
                    if (which == (unusedUsers.size())) {
                        createDialogCreateNewUser();
                    } else {
                        gameDynamicPresenter.addUser(gameDynamicPresenter.getUnusedUsers().get(which));
                    }
                });
        builder.create();
        builder.show();
    }


    public void setGamePresenter(GameDynamicPresenter gameDynamicPresenter) {
        super.setGamePresenter(gameDynamicPresenter);
        this.gameDynamicPresenter = gameDynamicPresenter;
    }

    public void makeButtonsNotClickable(GameEngine gameEngine) {
        gameEngine.getBtnScore().setClickable(false);
    }

    public void makeAddButtonHidden() {
        addButton.setVisibility(View.INVISIBLE);

    }

    public void updateScore(GameEngineDynamic ge) {
        super.updateScore(ge);
        ge.getTvHits().setText(Integer.toString(ge.getHits()));
        ge.getTvRemaining().setText(Integer.toString(ge.getRemaining()));
    }

    public void makeRowChecked(GameEngineDynamic ged) {
        for(CheckBox cb : ged.checkBoxes){
            cb.setChecked(true);
        }
    }
}

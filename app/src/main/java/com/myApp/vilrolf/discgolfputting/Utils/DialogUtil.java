package com.myApp.vilrolf.discgolfputting.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.myApp.vilrolf.discgolfputting.Activity.MainActivity;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.User;
import com.myApp.vilrolf.discgolfputting.R;

/**
 * Created by Viljar on 11-Feb-17.
 */

public class DialogUtil {

    public static AlertDialog.Builder createDialogNewUser(Context context, DbHelper mydb) {

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
                    Toast.makeText(context, "You need to enter a username", Toast.LENGTH_SHORT).show();
                    alert.show();
                } else {
                    User user = new User(value);
                    user.setId(mydb.createUser(new User(value)));
                    mydb.setAllGamesToUser(user);
                    Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show();
                }
            }
        });
       return alert;
    }
}

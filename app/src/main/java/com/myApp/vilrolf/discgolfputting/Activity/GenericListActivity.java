package com.myApp.vilrolf.discgolfputting.Activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.myApp.vilrolf.discgolfputting.Database.DbClass;
import com.myApp.vilrolf.discgolfputting.Database.DbHelper;
import com.myApp.vilrolf.discgolfputting.Database.Game;
import com.myApp.vilrolf.discgolfputting.R;
import com.myApp.vilrolf.discgolfputting.Utils.DialogUtil;

import java.util.ArrayList;

public class GenericListActivity extends AppCompatActivity {
    private ArrayList<? extends DbClass> list;
    private  DbHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mydb = new DbHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_list);
        list = (ArrayList<? extends DbClass>) getIntent().getExtras().getSerializable("list");

        loadList();



    }

    private void loadList() {

        ArrayAdapter<? extends DbClass>  adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);
        ListView lw = (ListView) findViewById(R.id.listViewGenericList);
        lw.setOnItemClickListener((parent, view, position, id) -> {
            popAreYouSureDialog(position);


        });

        lw.setAdapter(adapter);
    }
    private void deletePos(int pos){
        list.get(pos).deleteObject(mydb);
        list.remove(pos);
        loadList();
    }

    private void popAreYouSureDialog(int pos){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        deletePos(pos);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_delete)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }
}

package com.myApp.vilrolf.discgolfputting.Utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

/**
 * Created by Viljar on 11-Feb-17.
 */

public class SpinnerUtil {

    public static <T> ArrayAdapter<String> createSpinnerAdapter(ArrayList<T> list, Context context) {
        String[] textList = new String[list.size()];
        for (int i = 0; i < textList.length; i++) {
            textList[i] = list.get(i).toString();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, textList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;

    }

}

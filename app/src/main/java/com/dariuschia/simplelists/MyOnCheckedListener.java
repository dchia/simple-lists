package com.dariuschia.simplelists;

import android.widget.CompoundButton;

/**
 * This class will listen for checkbox checked state changes.
 */
public class MyOnCheckedListener implements CompoundButton.OnCheckedChangeListener {

    private int position;
    private CustomAdapter ad;

    public MyOnCheckedListener(int position, CustomAdapter arrayAdapter) {
        this.position = position;
        ad = arrayAdapter;
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        ad.setCheckedStatus(position, b);

    }
}

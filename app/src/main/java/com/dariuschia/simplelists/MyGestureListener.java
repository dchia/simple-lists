package com.dariuschia.simplelists;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class will detect double taps and initiate edit mode for list items.
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

    private final String TAG = "MyGestureListener";
    private CustomAdapter adapter;
    private ListInfoDataSource ds;
    private String listName;
    private ListView listView;
    Context context;

    public MyGestureListener(CustomAdapter ad, ListView list, ListInfoDataSource data, Context c, String name) {
        adapter = ad;
        ds = data;
        listName = name;
        listView = list;
        context = c;
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public boolean onDoubleTap(MotionEvent e) {
        try {
            int position = listView.pointToPosition(Math.round(e.getX()), Math.round(e.getY()));

            final String oldItem = adapter.getListItemAtPos(position);

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle(R.string.edit_list_item);
            alert.setMessage("Please edit the list item.");

            final EditText input = new EditText(context);
            input.setText(oldItem, TextView.BufferType.EDITABLE);
            alert.setView(input);

            alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String newItem = input.getText().toString();

                    if (adapter.getPosition(newItem) != -1) {
                        Toast.makeText(context.getApplicationContext(), "List item already exists! Please try again.", Toast.LENGTH_SHORT).show();
                    } else if (newItem.trim().equals("")) {
                        Toast.makeText(context.getApplicationContext(), "Empty list item entered! Please try again.", Toast.LENGTH_SHORT).show();
                    } else if (newItem.contains("'")) {
                        Toast.makeText(context.getApplicationContext(), "Single quotes cannot be included! Please try again.", Toast.LENGTH_SHORT).show();
                    } else {
                        ds.updateListItem(listName, oldItem, newItem);
                        adapter.updateListItem(oldItem, newItem);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            alert.show();
        } catch (ArrayIndexOutOfBoundsException except) {}
        return true;
    }
}

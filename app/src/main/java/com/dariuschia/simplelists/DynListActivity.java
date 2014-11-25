package com.dariuschia.simplelists;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.TimedUndoAdapter;

import java.util.List;

public class DynListActivity extends Activity {

    private DynamicListView dynList;
    private EditText addEditText;
    private Button addButton;

    private static final String TAG = "DynListActivity";
    private ListInfoDataSource ds;
    private String listName;
    private CustomAdapter dynListAdapter;
    private int numListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        Intent intent = getIntent();
        listName = intent.getStringExtra("listName");
        this.setTitle(listName);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        addEditText = (EditText) findViewById(R.id.addEditText);
        addButton = (Button) findViewById(R.id.addButton);

        ds = new ListInfoDataSource(this);
        ds.open();
        List<String> listItems = ds.getAllListItems(listName);
        List<String> checkedStatus = ds.getAllListItemsCheckedStatus(listName);
        numListItems = listItems.size();

        dynList = (DynamicListView) findViewById(R.id.dynListView);

        dynListAdapter = new CustomAdapter(this, R.layout.list_items, listItems, checkedStatus, ds, listName);
        TimedUndoAdapter timedUndoAdapter = new TimedUndoAdapter(dynListAdapter, this, new MyOnDismissCallback(dynListAdapter, ds, listName));

        dynList.setAdapter(timedUndoAdapter);

        final GestureDetector gestureDetector = new GestureDetector(this, new MyGestureListener(dynListAdapter, dynList, ds, this, listName));
        dynList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });

        dynList.enableDragAndDrop();
        dynList.setDraggableManager(new TouchViewDraggableManager(R.id.dynGrip));
        dynList.setOnItemMovedListener(new MyOnItemMovedListener(dynListAdapter));

        dynList.enableSimpleSwipeUndo();
    }

    public void addNewListItem(View view) {
        String listItem = addEditText.getText().toString();
        if (dynListAdapter.getPosition(listItem) != -1) {
            Toast.makeText(getApplicationContext(), "List item already exists! Please try again.", Toast.LENGTH_SHORT).show();
        } else if (listItem.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Empty list item entered! Please try again.", Toast.LENGTH_SHORT).show();
        } else if (listItem.contains("'")) {
            Toast.makeText(getApplicationContext(), "Single quotes cannot be included! Please try again.", Toast.LENGTH_SHORT).show();
        } else {
            ds.insertListItem(listName, listItem, dynListAdapter.getCount());
            addEditText.setText("", TextView.BufferType.EDITABLE);
            dynListAdapter.add(listItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dyn_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent intent = new Intent(DynListActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(DynListActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_edit_name) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.edit_list_names);
            alert.setMessage("Please enter the new name of the list.");

            final EditText input = new EditText(this);
            input.setText(listName, TextView.BufferType.EDITABLE);
            alert.setView(input);

            alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String newName = input.getText().toString();

                    List<String> allNames = ds.getAllListNames();

                    if (allNames.contains(newName)) {
                        Toast.makeText(getApplicationContext(), "List already exists! Please try again.", Toast.LENGTH_SHORT).show();
                    } else if (newName.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "Empty list name entered! Please try again.", Toast.LENGTH_SHORT).show();
                    } else if (newName.contains("'")) {
                        Toast.makeText(getApplicationContext(), "Single quotes cannot be included! Please try again.", Toast.LENGTH_SHORT).show();
                    } else {
                        ds.updateListName(listName, newName);
                        setTitle(newName);
                        listName = newName;
                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            alert.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

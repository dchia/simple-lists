package com.dariuschia.simplelists;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private ListView selectionList;
    private ListInfoDataSource ds;
    private ArrayAdapter<String> selectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectionList = (ListView) findViewById(R.id.selectionList);
        ds = new ListInfoDataSource(this);
        ds.open();
        List<String> listNames = ds.getAllListNames();

        selectionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, listNames);

        selectionList.setAdapter(selectionAdapter);

        selectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String listName = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity.this, DynListActivity.class);
                intent.putExtra("listName", listName);
                startActivity(intent);
            }
        });

        selectionList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        selectionList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                return;
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.contextual_discard:
                        SparseBooleanArray checkedItems = selectionList.getCheckedItemPositions();
                        ArrayList<String> listsToDelete = new ArrayList<String>();
                        for (int i = 0; i < checkedItems.size(); i++) {

                            int pos = checkedItems.keyAt(i);

                            if (selectionList.isItemChecked(pos)) {
                                String listName = selectionList.getItemAtPosition(pos).toString();
                                listsToDelete.add(listName);
                            }
                        }

                        for (String listName : listsToDelete) {
                            ds.deleteList(listName);
                            selectionAdapter.remove(listName);
                        }
                        actionMode.finish();
                        selectionAdapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                return;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_new_list) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.add_new_list);
            alert.setMessage("Please enter the name of the list you want to add.");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String listName = input.getText().toString();
                    if (selectionAdapter.getPosition(listName) != -1) {
                        Toast.makeText(getApplicationContext(), "List already exists! Please try again.", Toast.LENGTH_SHORT).show();
                    } else if (listName.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "Empty list name entered! Please try again.", Toast.LENGTH_SHORT).show();
                    } else if (listName.contains("'")) {
                        Toast.makeText(getApplicationContext(), "Single quotes cannot be included! Please try again.", Toast.LENGTH_SHORT).show();
                    } else {
                        ds.createNewList(listName);
                        selectionAdapter.add(listName);
                        selectionAdapter.notifyDataSetChanged();
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

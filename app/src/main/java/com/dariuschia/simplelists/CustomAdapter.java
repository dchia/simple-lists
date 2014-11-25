package com.dariuschia.simplelists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.List;

/**
 * This class extends the ArrayAdapter and overrides getView so that list is
 * populated with checkboxes and edittext.
 */
public class CustomAdapter extends ArrayAdapter<String> implements UndoAdapter {

    private static final String TAG = "CustomAdapter";

    private Context context;
    private List<String> listItems;
    private List<String> checkedStatus;
    private int resourceID;
    private String listName;
    private ListInfoDataSource ds;

    public CustomAdapter(Context context, int resource, List objects, List checkedStatus, ListInfoDataSource dataSource, String name) {
        super(objects);
        this.context = context;
        resourceID = resource;
        listItems = objects;
        this.checkedStatus = checkedStatus;
        listName = name;
        ds = dataSource;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resourceID, parent, false);
        CheckBox checkbox = (CheckBox) rowView.findViewById(R.id.dynCheckBox);
        TextView textView = (TextView) rowView.findViewById(R.id.dynListItem);
        String currentListItem = listItems.get(position);
        if (checkedStatus.get(position).equals("t")) {
            checkbox.setChecked(true);
            rowView.setBackgroundResource(R.drawable.list_item_checked);
        }
        checkbox.setOnCheckedChangeListener(new MyOnCheckedListener(position, this));
        textView.setText(currentListItem, TextView.BufferType.EDITABLE);

        return rowView;
    }

    @Override
    public View getUndoView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.undo_row, parent, false);
        }
        return view;
    }

    @Override
    public View getUndoClickView(final View view) {
        return view.findViewById(R.id.undo_row_undobutton);
    }

    public int getPosition(String item) {
        return listItems.indexOf(item);
    }

    @Override
    public String remove(int location) {
        String itemToDelete = listItems.get(location);
        ds.deleteListItem(listName, itemToDelete);
        checkedStatus.remove(location);
        return super.remove(location);
    }

    public void updateListOrder() {
        for (int i = 0; i < getCount(); i++) {
            String listItem = getItem(i);
            ds.updateListItemOrder(listName, listItem, i);
        }
    }

    public void updateChecked(int oldpos, int newpos) {
        String itemStat = checkedStatus.get(oldpos);
        checkedStatus.remove(oldpos);
        checkedStatus.add(newpos, itemStat);
        notifyDataSetChanged();
    }

    public void updateListItem(String oldItem, String newItem) {
        int position = listItems.indexOf(oldItem);
        listItems.set(position, newItem);
    }

    @Override
    public boolean add(String object) {
        boolean result = listItems.add(object);
        boolean result2 = checkedStatus.add("f");
        notifyDataSetChanged();
        return result && result2;
    }

    public void setCheckedStatus(int position, boolean checkedStatus) {
        String listItem = listItems.get(position);
        if (checkedStatus) {
            this.checkedStatus.set(position, "t");
            ds.updateListItemChecked(listName, listItem, "t");
        } else {
            this.checkedStatus.set(position, "f");
            ds.updateListItemChecked(listName, listItem, "f");
        }
        notifyDataSetChanged();
    }

    public String getListItemAtPos(int position) {
        return listItems.get(position);
    }

}

package com.dariuschia.simplelists;

import android.view.ViewGroup;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

/**
 * This class will handle the swipe deletion of list items.
 */
public class MyOnDismissCallback implements OnDismissCallback {

    private final ArrayAdapter<String> mAdapter;
    private final ListInfoDataSource ds;
    private final String listName;

    MyOnDismissCallback (final ArrayAdapter<String> adapter, final ListInfoDataSource dataSource, final String name) {
        mAdapter = adapter;
        ds = dataSource;
        listName = name;
    }
    @Override
    public void onDismiss(final ViewGroup listView, final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            String itemToDelete = mAdapter.getItem(position);
            mAdapter.remove(position);
            ds.deleteListItem(listName, itemToDelete);

        }
    }
}

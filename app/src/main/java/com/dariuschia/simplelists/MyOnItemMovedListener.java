package com.dariuschia.simplelists;

import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;

/**
 * This class will handle the drag and drop rearranging of the list.
 */
public class MyOnItemMovedListener implements OnItemMovedListener {

    private final CustomAdapter mAdapter;

    public MyOnItemMovedListener(final CustomAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onItemMoved(final int originalPosition, final int newPosition) {
        mAdapter.updateListOrder();
        mAdapter.updateChecked(originalPosition, newPosition);
    }
}

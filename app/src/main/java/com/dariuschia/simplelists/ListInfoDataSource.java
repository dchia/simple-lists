package com.dariuschia.simplelists;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * This class maintains database connection, and adds and fetches ListInfo
 * objects.
 */
public class ListInfoDataSource {

    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;
    private String[] columnListItem = {MySQLiteHelper.COLUMN_LISTITEM};
    private String[] columnName = {MySQLiteHelper.COLUMN_NAME};
    private String[] columnChecked = {MySQLiteHelper.COLUMN_CHECKED};

    public ListInfoDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createNewList(String listName) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, listName);
        long insertID = db.insert(MySQLiteHelper.TABLE_NAME, null, values);
        return insertID;
    }

    public long insertListItem(String listName, String listItem, int order) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, listName);
        values.put(MySQLiteHelper.COLUMN_LISTITEM, listItem);
        values.put(MySQLiteHelper.COLUMN_ITEMORDER, order);
        values.put(MySQLiteHelper.COLUMN_CHECKED, "f");
        long insertID = db.insert(MySQLiteHelper.TABLE_LIST_NAME, null, values);
        return insertID;
    }

    public void deleteListItem(String listName, String listItem) {
        db.delete(MySQLiteHelper.TABLE_LIST_NAME, MySQLiteHelper.COLUMN_NAME + " = '" + listName + "' AND " + MySQLiteHelper.COLUMN_LISTITEM + " = '" + listItem + "'", null);
    }

    public void deleteList(String listName) {
        db.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_NAME + " = '" + listName + "'", null);
        db.delete(MySQLiteHelper.TABLE_LIST_NAME, MySQLiteHelper.COLUMN_NAME + " = '" + listName + "'", null);
    }

    public List<String> getAllListItems(String listName) {
        List<String> listItems = new ArrayList<String>();


        Cursor cursor = db.query(MySQLiteHelper.TABLE_LIST_NAME,
                columnListItem, MySQLiteHelper.COLUMN_NAME + " = '" + listName + "'",
                null, null, null, MySQLiteHelper.COLUMN_ITEMORDER + " ASC");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String item = cursor.getString(0);
            listItems.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return listItems;
    }

    public List<String> getAllListItemsCheckedStatus(String listName) {
        List<String> listItems = new ArrayList<String>();


        Cursor cursor = db.query(MySQLiteHelper.TABLE_LIST_NAME,
                columnChecked, MySQLiteHelper.COLUMN_NAME + " = '" + listName + "'",
                null, null, null, MySQLiteHelper.COLUMN_ITEMORDER + " ASC");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String item = cursor.getString(0);
            listItems.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return listItems;
    }

    public List<String> getAllListNames() {
        List<String> listNames = new ArrayList<String>();

        Cursor cursor = db.query(MySQLiteHelper.TABLE_NAME, columnName,
                null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String name = cursor.getString(0);
            listNames.add(name);
            cursor.moveToNext();
        }
        cursor.close();
        return listNames;
    }

    public void updateListName(String oldName, String newName) {
        db.execSQL("UPDATE " + MySQLiteHelper.TABLE_LIST_NAME + " SET " +
                MySQLiteHelper.COLUMN_NAME + " = '" + newName + "' WHERE " + MySQLiteHelper.COLUMN_NAME + " = '" + oldName + "'");
        db.execSQL("UPDATE " + MySQLiteHelper.TABLE_NAME + " SET " +
                MySQLiteHelper.COLUMN_NAME + " = '" + newName + "' WHERE " + MySQLiteHelper.COLUMN_NAME + " = '" + oldName + "'");
    }

    public void updateListItemOrder(String listName, String listItem, int newOrder) {
        db.execSQL("UPDATE " + MySQLiteHelper.TABLE_LIST_NAME + " SET " +
                MySQLiteHelper.COLUMN_ITEMORDER + " = " + newOrder + " WHERE " + MySQLiteHelper.COLUMN_NAME + " = '" + listName + "' AND "
                + MySQLiteHelper.COLUMN_LISTITEM + " = '" + listItem + "'");
    }

    public void updateListItem(String listName, String oldItem, String newItem) {
        db.execSQL("UPDATE " + MySQLiteHelper.TABLE_LIST_NAME + " SET " +
                MySQLiteHelper.COLUMN_LISTITEM + " = '" + newItem + "' WHERE " + MySQLiteHelper.COLUMN_NAME + " = '" + listName + "' AND "
                + MySQLiteHelper.COLUMN_LISTITEM + " = '" + oldItem + "'");
    }

    public void updateListItemChecked(String listName, String listItem, String checkedStatus) {
        db.execSQL("UPDATE " + MySQLiteHelper.TABLE_LIST_NAME + " SET " +
                MySQLiteHelper.COLUMN_CHECKED + " = '" + checkedStatus + "' WHERE " + MySQLiteHelper.COLUMN_NAME + " = '" + listName + "' AND "
                + MySQLiteHelper.COLUMN_LISTITEM + " = '" + listItem + "'");
    }

}
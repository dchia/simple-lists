package com.dariuschia.simplelists;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class is the SQLiteOpenHelper
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    // Strings for both ListNames and Lists table
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "ListName";

    // Strings for ListNames table
    public static final String TABLE_NAME = "ListNames";
    public static final String TABLE_CREATE = "create table " + TABLE_NAME +
            " (" + COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " text);";

    // Strings for Lists table
    public static final String TABLE_LIST_NAME = "Lists";
    public static final String COLUMN_LISTITEM = "ListItem";
    public static final String COLUMN_ITEMORDER = "ItemOrder";
    public static final String COLUMN_CHECKED = "Checked";
    private static final String TABLE_LIST_CREATE = "create table " + TABLE_LIST_NAME
            + " (" + COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " Text, " + COLUMN_LISTITEM + " Text, " + COLUMN_CHECKED + " Text, " + COLUMN_ITEMORDER + " Integer);";

    private static final String DATABASE_NAME = "lists.db";
    private static int DATABASE_VERSION = 1;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
        sqLiteDatabase.execSQL(TABLE_LIST_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        DATABASE_VERSION = i2;
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_LIST_NAME);
        onCreate(sqLiteDatabase);
    }
}

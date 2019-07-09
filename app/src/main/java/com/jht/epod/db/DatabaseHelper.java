package com.jht.epod.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_CLASS_STRING =
            "create table class ( " +
            "ID integer primary key autoincrement," +
            "NAME text," +
            "TIME integer," +
            "CALORIE integer," +
            "DEGREE integer," +
            "CLASSTYPE integer," +
            "ICONNAME text," +
            "SELECTED integer" + ")";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CLASS_STRING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

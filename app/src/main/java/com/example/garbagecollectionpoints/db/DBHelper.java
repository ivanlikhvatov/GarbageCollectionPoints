package com.example.garbagecollectionpoints.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DBConstants.DATABASE_NAME.getName(), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + DBConstants.TABLE_POINTS.getName() + "("
                        + DBConstants.KEY_ID.getName() + " string primary key," + DBConstants.KEY_NAME.getName() + " text,"
                        + DBConstants.KEY_LATITUDE.getName() + " text,"
                        + DBConstants.KEY_LONGITUDE.getName() + " text,"
                        + DBConstants.KEY_TYPE.getName() + " text,"
                        + DBConstants.KEY_DESCRIPTION.getName() + " text,"
                        + DBConstants.KEY_DATE.getName() + " text"
                        + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DBConstants.TABLE_POINTS.getName());

        onCreate(db);
    }
}

package com.example.garbagecollectionpoints.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.garbagecollectionpoints.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DBConstants.DATABASE_NAME.getName(), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + DBConstants.TABLE_POINTS.getName() + "("
                        + DBConstants.KEY_ID.getName() + " string primary key,"
                        + DBConstants.KEY_NAME.getName() + " text,"
                        + DBConstants.KEY_LATITUDE.getName() + " text,"
                        + DBConstants.KEY_LONGITUDE.getName() + " text,"
                        + DBConstants.KEY_TYPE.getName() + " text,"
                        + DBConstants.KEY_DESCRIPTION.getName() + " text,"
                        + DBConstants.KEY_DATE.getName() + " text"
                        + ")"
        );

        db.execSQL(
                "create table " + DBConstants.USER_TABLE.getName() + "("
                        + DBConstants.USER_KEY_ID.getName() + " string primary key,"
                        + DBConstants.USER_KEY_NAME.getName() + " text,"
                        + DBConstants.USER_KEY_EMAIL.getName() + " text,"
                        + DBConstants.USER_KEY_PASS.getName() + " text"
                        + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DBConstants.TABLE_POINTS.getName());
        db.execSQL("drop table if exists " + DBConstants.USER_TABLE.getName());

        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.USER_KEY_NAME.getName(), user.getName());
        values.put(DBConstants.USER_KEY_EMAIL.getName(), user.getEmail());
        values.put(DBConstants.USER_KEY_PASS.getName(), user.getPassword());

        db.insert(DBConstants.USER_TABLE.getName(), null, values);
        db.close();
    }

    public List<User> getAllUser() {

        String[] columns = {
                DBConstants.USER_KEY_ID.getName(),
                DBConstants.USER_KEY_EMAIL.getName(),
                DBConstants.USER_KEY_NAME.getName(),
                DBConstants.USER_KEY_PASS.getName()
        };

        String sortOrder = DBConstants.USER_KEY_NAME.getName() + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(
                DBConstants.USER_TABLE.getName(),
                columns,
                null,
                null,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBConstants.USER_KEY_ID.getName()))));
                user.setName(cursor.getString(cursor.getColumnIndex(DBConstants.USER_KEY_NAME.getName())));
                user.setEmail(cursor.getString(cursor.getColumnIndex(DBConstants.USER_KEY_EMAIL.getName())));
                user.setPassword(cursor.getString(cursor.getColumnIndex(DBConstants.USER_KEY_PASS.getName())));

                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return userList;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.USER_KEY_NAME.getName(), user.getName());
        values.put(DBConstants.USER_KEY_EMAIL.getName(), user.getEmail());
        values.put(DBConstants.USER_KEY_PASS.getName(), user.getPassword());

        db.update(
                DBConstants.USER_TABLE.getName(),
                values,
                DBConstants.USER_KEY_ID.getName() + " = ?",
                new String[] {
                        String.valueOf(user.getId())
                }
        );

        db.close();
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(
                DBConstants.USER_TABLE.getName(),
                DBConstants.USER_KEY_ID.getName() + " = ?",
                new String[] {
                        String.valueOf(user.getId())
                }
        );

        db.close();
    }

    public boolean checkUser(String email) {
        // array of columns to fetch
        String[] columns = {
                DBConstants.USER_KEY_ID.getName()
        };

        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = DBConstants.USER_KEY_EMAIL.getName() + " = ?";
        // selection argument
        String[] selectionArgs = {email};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(
                DBConstants.USER_TABLE.getName(), //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null
        );                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public boolean checkUser(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                DBConstants.USER_KEY_ID.getName()
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = DBConstants.USER_KEY_EMAIL.getName() + " = ?" + " AND " + DBConstants.USER_KEY_PASS.getName() + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(DBConstants.USER_TABLE.getName(), //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
}

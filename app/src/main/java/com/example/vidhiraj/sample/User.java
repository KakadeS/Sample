package com.example.vidhiraj.sample;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by vidhiraj on 10-08-2016.
 */
public class User extends ContentProvider {
    public static final String PROVIDER_NAME = "com.example.vidhiraj.sample.User";

    /**
     * A uri to do operations on cust_master table. A content provider is identified by its uri
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/users");

    /**
     * Constants to identify the requested operation
     */
    private static final int USERS = 1;


    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "users", USERS);
    }


    UserDB mUserDB;

    @Override
    public boolean onCreate() {
        mUserDB = new UserDB(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if (uriMatcher.match(uri) == USERS) {
            Cursor c = null;
            c = mUserDB.getAllCustomers();
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        } else {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mUserDB.getWritableDatabase();
        long id = db.insert(UserDB.DATABASE_TABLE, "", values);
        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }
}
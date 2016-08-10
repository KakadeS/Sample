package com.example.vidhiraj.sample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vidhiraj on 10-08-2016.
 */
public class UserDB extends SQLiteOpenHelper {


    private static String DBNAME = "EraDataBase";

    private static int VERSION = 3;


    public static final String KEY_ROW_ID = "_id";

    public static final String KEY_EMAIL = "cust_email";

    public static final String KEY_PASS = "cust_pass";

    public static final String KEY_PIN = "cust_pin";

    public static final String DATABASE_TABLE = "earcordrecords";

    public SQLiteDatabase mDB;

    private static final String sql = "create table "+DATABASE_TABLE +"(_id INTEGER primary key autoincrement,cust_email TEXT)";

    public UserDB(Context context) {
        super(context, DBNAME, null, VERSION);
        this.mDB = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
        Log.e("onCreate", "Database created");

    }

    public Cursor getAllCustomers() {
        Log.e("onCreate", "getAllCustomers");
        mDB = getReadableDatabase();
        String query = "SELECT * FROM " + DATABASE_TABLE;
        Cursor c = mDB.rawQuery(query, null);
        Log.e("values are====", String.valueOf(c));
        return c;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
        Log.e("table created",DATABASE_TABLE);
        // TODO Auto-generated method stub
    }
}
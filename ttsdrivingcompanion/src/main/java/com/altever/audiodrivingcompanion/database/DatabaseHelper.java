package com.altever.audiodrivingcompanion.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.altever.audiodrivingcompanion.database.table.TableLocationLog;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "ttsdrivingcompanion.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableLocationLog.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableLocationLog.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public Cursor getSpeedDetails()
    {

        SQLiteDatabase db   = this.getWritableDatabase();

        String rawQuery     =   "SELECT sum(speed) as speed,count (*) as count FROM (select * from location_log ORDER by id DESC LIMIT 30) as a";

        Cursor c = db.rawQuery(
                rawQuery,
                null
        );

        return c;
    }
}

package com.altever.audiodrivingcompanion.database.content_provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.altever.audiodrivingcompanion.database.DatabaseHelper;
import com.altever.audiodrivingcompanion.database.table.TableLocationLog;


public class ContentProviderSpeed extends ContentProvider {

    private DatabaseHelper database;

    private static final int ALL_SPEED_ID     = 1;
    private static final int SINGLE_SPEED_ID  = 2;

    private static final String AUTHORITY   = "ttsdrivingcompanion.speed";
    private static final String BASE_PATH   = "ttsdrivingcompanion";

    private static final String USER_SPEED_PATH    = "user_speed_path";

    public static final Uri ALL_SPEED_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH+ "/"+USER_SPEED_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH+"/"+USER_SPEED_PATH, ALL_SPEED_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH+"/"+USER_SPEED_PATH+"/*", SINGLE_SPEED_ID);
    }

    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase db = database.getWritableDatabase();

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case ALL_SPEED_ID:
                queryBuilder.setTables(TableLocationLog.TABLE_NAME);
                break;
            case SINGLE_SPEED_ID:
                queryBuilder.setTables(TableLocationLog.TABLE_NAME);
                queryBuilder.appendWhere(TableLocationLog.ID+ "=" + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        Uri _uri = null;

        switch (uriType) {

            case ALL_SPEED_ID:
                id = sqlDB.insert(TableLocationLog.TABLE_NAME, null, values);
                _uri = ContentUris.withAppendedId(ALL_SPEED_URI, id);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return _uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        String id;
        switch (uriType) {
            case ALL_SPEED_ID:
                rowsDeleted = sqlDB.delete(TableLocationLog.TABLE_NAME, selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        String id;
        switch (uriType) {
            case ALL_SPEED_ID:
                rowsUpdated = sqlDB.update(TableLocationLog.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case SINGLE_SPEED_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(TableLocationLog.TABLE_NAME,
                            values,
                            TableLocationLog.ID+ "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(TableLocationLog.TABLE_NAME,
                            values,
                            TableLocationLog.ID+ "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            // endregion

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}

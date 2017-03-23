package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.MovieContract.MovieEntry;


/**
 * Created by Michał on 2017-03-20.
 */

public class MovieDbHelper  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                        MovieEntry._ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieEntry.COLUMN_TITLE         + " TEXT NOT NULL, "                 +
                        MovieEntry.COLUMN_ORGINAL_TITLE + " TEXT NOT NULL, "                 +

                        MovieEntry.COLUMN_EXTERNAL_ID   + " INTEGER NOT NULL,"                  +

                        MovieEntry.COLUMN_OVERVIEW      + " TEXT, "                    +
                        MovieEntry.COLUMN_POSTER_PATH   + " TEXT, "                    +
                        MovieEntry.COLUMN_POSTER_BLOB   + " BLOB, "                    +

                        MovieEntry.COLUMN_RELEASE_DATE  + " TEXT, "                    +
                        MovieEntry.COLUMN_VOTE_AVERAGE  + " REAL, "                    +

                        " UNIQUE (" + MovieEntry.COLUMN_EXTERNAL_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

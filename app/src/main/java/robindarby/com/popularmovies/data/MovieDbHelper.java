package robindarby.com.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import robindarby.com.popularmovies.models.Movie;

/**
 * Created by darby on 8/26/15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String INT_TYPE = " INTEGER";
    private static final String UNIQUE_TYPE = " UNIQUE";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Movie.MovieEntry.TABLE_NAME +
            " ( " + Movie.MovieEntry._ID + " INTEGER PRIMARY KEY" +
            COMMA_SEP + Movie.MovieEntry.COLUMN_NAME_MOVIE_ID + INT_TYPE + UNIQUE_TYPE +
            COMMA_SEP + Movie.MovieEntry.COLUMN_NAME_TITLE + TEXT_TYPE +
            COMMA_SEP + Movie.MovieEntry.COLUMN_NAME_OVERVIEW + TEXT_TYPE +
            COMMA_SEP + Movie.MovieEntry.COLUMN_NAME_POSTER_PATH + TEXT_TYPE +
            COMMA_SEP + Movie.MovieEntry.COLUMN_NAME_VOTE_AVG + REAL_TYPE +
            COMMA_SEP + Movie.MovieEntry.COLUMN_NAME_VOTE_COUNT + REAL_TYPE +
            COMMA_SEP + Movie.MovieEntry.COLUMN_NAME_RELEASE_DATE + TEXT_TYPE +
            COMMA_SEP + Movie.MovieEntry.COLUMN_NAME_FAVORITE + INT_TYPE +
            COMMA_SEP + Movie.MovieEntry.COLUMN_NAME_POPULARITY + REAL_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Movie.MovieEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

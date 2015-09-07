package robindarby.com.popularmovies.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import robindarby.com.popularmovies.data.MovieDbHelper;
import robindarby.com.popularmovies.models.Movie;

/**
 * Created by darby on 9/7/15.
 */
public class PopularMoviesContentProvider extends ContentProvider {

    private MovieDbHelper mDbHelper;

    @Override
    public boolean onCreate() {

        mDbHelper = new MovieDbHelper(getContext());

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor c = null;

        try {
            c = db.query(
                    Movie.MovieEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    selection,    // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(c.getCount() > 0) {
            c.moveToFirst();
            return c;
        }

        db.close();

        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

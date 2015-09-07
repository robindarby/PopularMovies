package robindarby.com.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import robindarby.com.popularmovies.models.Movie;

/**
 * Created by darby on 8/26/15.
 */
public class MovieManager {

    private static final String TAG = "MOV_MANAGER";

    private Context mContext;
    private static MovieManager mInstance;
    private MovieDbHelper mDbHelper;

    public static final String[] mProjection = {
            Movie.MovieEntry._ID,
            Movie.MovieEntry.COLUMN_NAME_MOVIE_ID,
            Movie.MovieEntry.COLUMN_NAME_TITLE,
            Movie.MovieEntry.COLUMN_NAME_OVERVIEW,
            Movie.MovieEntry.COLUMN_NAME_POSTER_PATH,
            Movie.MovieEntry.COLUMN_NAME_VOTE_AVG,
            Movie.MovieEntry.COLUMN_NAME_VOTE_COUNT,
            Movie.MovieEntry.COLUMN_NAME_RELEASE_DATE,
            Movie.MovieEntry.COLUMN_NAME_FAVORITE,
            Movie.MovieEntry.COLUMN_NAME_POPULARITY
    };

    public static MovieManager getInstance(Context context) {
        if( mInstance == null) {
            mInstance = new MovieManager(context);
        }
        return mInstance;
    }


    public MovieManager(Context context) {
        mContext = context;
        mDbHelper = new MovieDbHelper(mContext);
    }

    public long setMovie(Movie movie) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long newRowId;
        newRowId = db.insert(
                Movie.MovieEntry.TABLE_NAME,
                null,
                movie.getContentValues());

        db.close();
        return newRowId;
    }

    public Movie getMovie(int movieId) {

        Movie movie = null;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = Movie.MovieEntry.COLUMN_NAME_MOVIE_ID + "=?";

        String[] selectionArgs = {
                String.valueOf(movieId)
        };

        try {
            Cursor c = db.query(
                    Movie.MovieEntry.TABLE_NAME,  // The table to query
                    mProjection,                               // The columns to return
                    selection,    // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            if(c.getCount() > 0) {
                c.moveToFirst();
                movie = new Movie(c);
            }
            c.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        db.close();

        return movie;
    }

    public void deleteMovie(int movieId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = Movie.MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?";

        String[] selectionArgs = { String.valueOf(movieId) };

        db.delete(Movie.MovieEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    public ArrayList<Movie> getMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        try {
            Cursor c = db.query(
                    Movie.MovieEntry.TABLE_NAME,  // The table to query
                    mProjection,                               // The columns to return
                    null,    // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            if(c.getCount() > 0) {
                c.moveToFirst();
                do {
                    movies.add(new Movie(c));
                } while (c.moveToNext());
            }
            c.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        db.close();

        return movies;
    }

    public ArrayList<Movie> getFavoriteMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = Movie.MovieEntry.COLUMN_NAME_FAVORITE + "=?";

        String[] selectionArgs = {
                "1"
        };

        try {
            Cursor c = db.query(
                    Movie.MovieEntry.TABLE_NAME,  // The table to query
                    mProjection,                               // The columns to return
                    selection,    // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    movies.add(new Movie(c));
                } while (c.moveToNext());
            }
            c.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        db.close();

        return movies;
    }

    public boolean setFavorite(int movieId, boolean favorite) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Movie.MovieEntry.COLUMN_NAME_FAVORITE, (favorite ? 1 : 0));

        String selection = Movie.MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(movieId) };

        int count = db.update(
                Movie.MovieEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();

        return (count > 0);
    }
}

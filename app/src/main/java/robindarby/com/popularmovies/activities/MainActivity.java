package robindarby.com.popularmovies.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import robindarby.com.popularmovies.R;
import robindarby.com.popularmovies.adapters.MovieAdapter;
import robindarby.com.popularmovies.models.Movie;
import robindarby.com.popularmovies.services.MoviesService;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MAIN";

    private final static String JSON_RESULTS_FIELD = "results";

    protected ProgressDialog mLoadingDialog;


    private ArrayList<Movie> mMovieList = new ArrayList<Movie>();


    public static final String MOVIE_INTENT_EXTRA = "movie";
    public static final String MOVIE_LIST_STATE_EXTRA = "movies";

    public static final String FAVORITE_MOVIES_PREF = "favoritemovies";

    private GridView mGridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridview = (GridView) findViewById(R.id.gridview);

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Movie selectedMovie = null;

                for (Movie movie : mMovieList) {
                    if(movie.getId() == id) {
                        selectedMovie = movie;
                        break;
                    }
                }

                if (selectedMovie != null) {
                    Intent detailsIntent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                    detailsIntent.putExtra(MOVIE_INTENT_EXTRA, selectedMovie);
                    startActivity(detailsIntent);
                }
            }
        });

        if (savedInstanceState != null && savedInstanceState.getSerializable(MOVIE_LIST_STATE_EXTRA) != null) {
            mMovieList = (ArrayList<Movie>) savedInstanceState.getSerializable(MOVIE_LIST_STATE_EXTRA);
            updateMovies(false);
        }
        else {
            startMovieService();
        }
    }

    private void startMovieService() {
        // go get the movies in a background thread.
        mLoadingDialog = ProgressDialog.show(this, "", getString(R.string.loading), true);
        LocalBroadcastManager.getInstance(this).registerReceiver(mDiscoverMoviesServiceMessageReceiver, new IntentFilter(MoviesService.ACTION));
        Intent mServiceIntent = new Intent(this, MoviesService.class);
        startService(mServiceIntent);
    }

    private BroadcastReceiver mDiscoverMoviesServiceMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mLoadingDialog.dismiss();
            mMovieList = (ArrayList<Movie>) intent.getSerializableExtra(MoviesService.MOVIES_INTENT_EXTRA);
            updateMovies(false);
        }
    };


    private void updateMovies(boolean favorites) {
        mGridview.setAdapter(new MovieAdapter(this, mMovieList, favorites));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MOVIE_LIST_STATE_EXTRA, mMovieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_most_pop_settings) {
            Collections.sort(mMovieList, Movie.MostPopularComparator);
            updateMovies(false);
            return true;
        } else if (id == R.id.action_rating_settings) {
            Collections.sort(mMovieList, Movie.RatedComparator);
            updateMovies(false);
            return true;
        } else if (id == R.id.action_fav_settings) {
            updateMovies(true);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mDiscoverMoviesServiceMessageReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

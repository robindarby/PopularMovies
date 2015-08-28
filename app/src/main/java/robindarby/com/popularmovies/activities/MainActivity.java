package robindarby.com.popularmovies.activities;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.Collections;
import robindarby.com.popularmovies.R;
import robindarby.com.popularmovies.adapters.MovieAdapter;
import robindarby.com.popularmovies.fragments.MovieDetailsFragment;
import robindarby.com.popularmovies.fragments.MovieListFragment;
import robindarby.com.popularmovies.models.Movie;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MAIN";


    public static final String MOVIE_INTENT_EXTRA = "movie";
    public static final String MOVIE_LIST_STATE_EXTRA = "movies";

    private boolean mTwoPane = false;
    private final static String MOVIE_DETAILS_FRAGMENT_TAG = "moviedetailsfragment";
    private final static String MOVIE_LIST_FRAGMENT_TAG = "movielistfragment";
    private MovieListFragment mMovieListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailsFragment(), MOVIE_DETAILS_FRAGMENT_TAG)
                        .commit();
            }
        }

        mMovieListFragment = (MovieListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_grid_container);
    }

    public void showMovieDetails(Movie movie) {
        if(mTwoPane) {
            MovieDetailsFragment detailsFragment = (MovieDetailsFragment) getSupportFragmentManager().findFragmentByTag(MOVIE_DETAILS_FRAGMENT_TAG);
            detailsFragment.loadMovieDetails(movie);
        }
        else {
            MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
            Bundle args = new Bundle();
            args.putSerializable(MOVIE_INTENT_EXTRA, movie);
            detailsFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_grid_container, detailsFragment, MOVIE_LIST_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void refreashFavorateMovieList() {
        mMovieListFragment.refreashFavorateMovieList();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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

        if (id == R.id.action_most_pop_settings) {
            mMovieListFragment.updateMovies(false, Movie.MostPopularComparator);
            return true;
        }
        else if (id == R.id.action_rating_settings) {
            mMovieListFragment.updateMovies(false, Movie.RatedComparator);
            return true;
        }
        else if (id == R.id.action_fav_settings) {
            mMovieListFragment.updateMovies(true, null);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

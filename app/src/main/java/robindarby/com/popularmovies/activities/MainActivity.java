package robindarby.com.popularmovies.activities;

import android.content.Intent;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import robindarby.com.popularmovies.R;
import robindarby.com.popularmovies.fragments.MovieDetailsFragment;
import robindarby.com.popularmovies.fragments.MovieListFragment;
import robindarby.com.popularmovies.models.Movie;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";


    public static final String MOVIE_INTENT_EXTRA = "movie";
    public static final String MOVIE_LIST_STATE_EXTRA = "movies";

    private boolean mTwoPane = false;
    private final static String MOVIE_DETAILS_FRAGMENT_TAG = "moviedetailsfragment";
    private final static String MOVIE_LIST_FRAGMENT_TAG = "movielistfragment";

    private ShareActionProvider mShareActionProvider;

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

        MovieListFragment movieListFragment = new MovieListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, movieListFragment, MOVIE_LIST_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
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
                    .replace(R.id.fragment_container, detailsFragment, MOVIE_DETAILS_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void refreashFavorateMovieList() {
        MovieListFragment movieListFragment = (MovieListFragment)getSupportFragmentManager().findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
        if( movieListFragment != null ) movieListFragment.refreashFavorateMovieList();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        MovieListFragment movieListFragment = (MovieListFragment)getSupportFragmentManager().findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
        if(movieListFragment != null) {
            if (id == R.id.action_most_pop_settings) {
                movieListFragment.updateMovies(false, Movie.MostPopularComparator);
                return true;
            }
            else if (id == R.id.action_rating_settings) {
                movieListFragment.updateMovies(false, Movie.RatedComparator);
                return true;
            }
            else if (id == R.id.action_fav_settings) {
                movieListFragment.updateMovies(true, null);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
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

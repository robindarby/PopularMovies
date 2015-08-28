package robindarby.com.popularmovies.fragments;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import robindarby.com.popularmovies.R;
import robindarby.com.popularmovies.activities.MainActivity;
import robindarby.com.popularmovies.adapters.MovieAdapter;
import robindarby.com.popularmovies.data.MovieManager;
import robindarby.com.popularmovies.models.Movie;
import robindarby.com.popularmovies.services.MoviesService;

/**
 * Created by darby on 8/12/15.
 */
public class MovieListFragment extends Fragment {

    protected ProgressDialog mLoadingDialog;
    private ArrayList<Movie> mMovieList = new ArrayList<Movie>();
    private GridView mGridview;
    private boolean mIsDisplayingFavorites = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGridview = (GridView) inflater.inflate(R.layout.movie_list_fragment_layout, container, false);

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Movie selectedMovie = null;

                for (Movie movie : mMovieList) {
                    if (movie.getId() == id) {
                        selectedMovie = movie;
                        break;
                    }
                }
                if (selectedMovie != null) ((MainActivity)getActivity()).showMovieDetails(selectedMovie);
            }
        });

        if (savedInstanceState != null && savedInstanceState.getSerializable(MainActivity.MOVIE_LIST_STATE_EXTRA) != null) {
            mMovieList = (ArrayList<Movie>) savedInstanceState.getSerializable(MainActivity.MOVIE_LIST_STATE_EXTRA);
            updateMovies(false, null);
        }
        else {
            startMovieService();
        }

        return mGridview;
    }

    private void startMovieService() {
        // go get the movies in a background thread.
        mLoadingDialog = ProgressDialog.show(getActivity(), "", getString(R.string.loading), true);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mDiscoverMoviesServiceMessageReceiver, new IntentFilter(MoviesService.ACTION));
        Intent mServiceIntent = new Intent(getActivity(), MoviesService.class);
        getActivity().startService(mServiceIntent);
    }

    private BroadcastReceiver mDiscoverMoviesServiceMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mLoadingDialog.dismiss();
            updateMovies(false, null);
        }
    };


    public void updateMovies(boolean favorites, Comparator comparator) {

        MovieManager manager = MovieManager.getInstance(getActivity());

        mIsDisplayingFavorites = favorites;
        mMovieList = (favorites) ? manager.getFavoriteMovies() : manager.getMovies();

        if(comparator != null)
            Collections.sort(mMovieList, comparator);

        mGridview.setAdapter(new MovieAdapter(getActivity(), mMovieList));
    }

    public void refreashFavorateMovieList() {
        if(mIsDisplayingFavorites) {
            MovieManager manager = MovieManager.getInstance(getActivity());
            mMovieList = manager.getFavoriteMovies();
            mGridview.setAdapter(new MovieAdapter(getActivity(), mMovieList));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MainActivity.MOVIE_LIST_STATE_EXTRA, mMovieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mDiscoverMoviesServiceMessageReceiver);
    }


}

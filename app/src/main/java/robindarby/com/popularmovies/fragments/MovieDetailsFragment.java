package robindarby.com.popularmovies.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import robindarby.com.popularmovies.R;
import robindarby.com.popularmovies.activities.MainActivity;
import robindarby.com.popularmovies.adapters.MovieDetailsListViewAdapter;
import robindarby.com.popularmovies.models.Movie;

/**
 * Created by darby on 8/12/15.
 */
public class MovieDetailsFragment extends Fragment {


    private MovieDetailsListViewAdapter mMovieDetailsListViewAdapter;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout fragmentLayout = (RelativeLayout) inflater.inflate(R.layout.movie_details_fragment, container, false);
        mListView = (ListView) fragmentLayout.findViewById(R.id.movie_details_listView);

        Bundle args = getArguments();
        if(args.getSerializable(MainActivity.MOVIE_INTENT_EXTRA) != null) {
            showMovieDetails((Movie)args.getSerializable(MainActivity.MOVIE_INTENT_EXTRA));
        }

        return fragmentLayout;
    }

    public void showMovieDetails(Movie movie) {
        mMovieDetailsListViewAdapter = new MovieDetailsListViewAdapter(getActivity(), movie);
        mListView.setAdapter(mMovieDetailsListViewAdapter);
    }


    @Override
    public void onDestroy() {
        if(mMovieDetailsListViewAdapter != null)
            mMovieDetailsListViewAdapter.releaseLoaders();
        super.onDestroy();
    }
}

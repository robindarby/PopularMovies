package robindarby.com.popularmovies.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;

import java.util.ArrayList;
import robindarby.com.popularmovies.R;
import robindarby.com.popularmovies.activities.MainActivity;
import robindarby.com.popularmovies.adapters.MovieDetailsListViewAdapter;
import robindarby.com.popularmovies.data.MovieManager;
import robindarby.com.popularmovies.models.Movie;
import robindarby.com.popularmovies.models.Review;
import robindarby.com.popularmovies.models.Video;
import robindarby.com.popularmovies.services.MovieDetailsService;

/**
 * Created by darby on 8/12/15.
 */
public class MovieDetailsFragment extends Fragment {

    private static final String TAG = "MOV_DETAILS_FRAG";

    protected ProgressDialog mLoadingDialog;
    private MovieDetailsListViewAdapter mMovieDetailsListViewAdapter;
    private ListView mListView;
    private Movie mMovie = null;
    private ArrayList<Review> mReviews = new ArrayList<Review>();
    private ArrayList<Video> mVideos = new ArrayList<Video>();

    private static final String MOVIE_STATE_EXTRA = "movie";
    private static final String VIDEOS_STATE_EXTRA = "videos";
    private static final String REVIEWS_STATE_EXTRA = "reviews";

    private static final String PROVIDER_NAME = "robindarby.com.popularmovies";
    private static final String URL = "content://" + PROVIDER_NAME + "/movie";
    private static final Uri CONTECT_URL = Uri.parse(URL);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout fragmentLayout = (RelativeLayout) inflater.inflate(R.layout.movie_details_fragment, container, false);
        mListView = (ListView) fragmentLayout.findViewById(R.id.movie_details_listView);

        if (savedInstanceState != null && savedInstanceState.getSerializable(MOVIE_STATE_EXTRA) != null) {
            mMovie = (Movie) savedInstanceState.getSerializable(MOVIE_STATE_EXTRA);
            mVideos = (ArrayList<Video>) savedInstanceState.getSerializable(VIDEOS_STATE_EXTRA);
            mReviews = (ArrayList<Review>) savedInstanceState.getSerializable(REVIEWS_STATE_EXTRA);
            showMovieDetails();
        }
        else {
            Bundle args = getArguments();
            if (args != null && args.getInt(MainActivity.MOVIE_ID_INTENT_EXTRA, -1) > 0) {
                loadMovieDetails(args.getInt(MainActivity.MOVIE_ID_INTENT_EXTRA, -1));
            }
        }

        return fragmentLayout;
    }


    private void startMovieDetailsService() {
        mLoadingDialog = ProgressDialog.show(getActivity(), "", getString(R.string.loading), true);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMovieDetailsServiceMessageReceiver, new IntentFilter(MovieDetailsService.ACTION));
        Intent mServiceIntent = new Intent(getActivity(), MovieDetailsService.class);
        mServiceIntent.putExtra(MovieDetailsService.MOVIE_ID_INTENT_EXTRA, mMovie.getId());
        getActivity().startService(mServiceIntent);
    }

    private BroadcastReceiver mMovieDetailsServiceMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getActivity();
            mLoadingDialog.dismiss();
            mReviews = (ArrayList<Review>)intent.getSerializableExtra(MovieDetailsService.REVIEWS_INTENT_EXTRA);
            mVideos = (ArrayList<Video>)intent.getSerializableExtra(MovieDetailsService.CLIPS_INTENT_EXTRA);
            showMovieDetails();
        }
    };

    public void loadMovieDetails(int movieId) {

        String selection = Movie.MovieEntry.COLUMN_NAME_MOVIE_ID + "=?";

        String[] selectionArgs = {
                String.valueOf(movieId)
        };

        Cursor c = getActivity().getContentResolver().query(
                CONTECT_URL,
                MovieManager.mProjection,       // The columns to return
                selection,                      // The columns for the WHERE clause
                selectionArgs,                  // The values for the WHERE clause
                null                            // The sort order
        );
        try {
            mMovie = new Movie(c);
            c.close();
            startMovieDetailsService();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMovieDetails() {
        mMovieDetailsListViewAdapter = new MovieDetailsListViewAdapter(getActivity(), mMovie, mReviews, mVideos);
        mListView.setAdapter(mMovieDetailsListViewAdapter);

        if(mVideos.size() > 0) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
            intent.putExtra(Intent.EXTRA_TEXT,"https://www.youtube.com/watch?v=" + mVideos.get(0).getId());
            ((MainActivity) getActivity()).setShareIntent(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MOVIE_STATE_EXTRA, mMovie);
        outState.putSerializable(REVIEWS_STATE_EXTRA, mReviews);
        outState.putSerializable(VIDEOS_STATE_EXTRA, mVideos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if(mMovieDetailsListViewAdapter != null) mMovieDetailsListViewAdapter.releaseLoaders();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMovieDetailsServiceMessageReceiver);
        super.onDestroy();
    }
}

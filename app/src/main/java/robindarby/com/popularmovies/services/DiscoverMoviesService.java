package robindarby.com.popularmovies.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import robindarby.com.popularmovies.models.Movie;
import robindarby.com.popularmovies.requests.DiscoverMovieJSONRequest;

/**
 * Created by darby on 7/13/15.
 */
public class DiscoverMoviesService extends IntentService {

    private static final String TAG = "MOVIE_SERVICE";

    public static final String ACTION = "robindarby.com.popularmovies.services.DiscoverMoviesService_ACTION";

    private static final String JSON_RESULTS_FIELD = "results";
    public static final String MOVIES_INTENT_EXTRA = "movies";

    public DiscoverMoviesService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DiscoverMovieJSONRequest request = new DiscoverMovieJSONRequest(getApplicationContext());

        JSONArray movieJSONList = null;
        try {
            JSONObject response = request.makeRequest();
            movieJSONList = response.getJSONArray(JSON_RESULTS_FIELD);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Movie> movieList = new ArrayList<Movie>();
        if(movieJSONList != null) {
            for (int i = 0; i < movieJSONList.length(); i++) {
                try {
                    movieList.add(new Movie(movieJSONList.getJSONObject(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Intent resultsIntent = new Intent(ACTION);
        resultsIntent.putExtra(MOVIES_INTENT_EXTRA, movieList);
        Log.d(TAG, "sendBroadcast");
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultsIntent);
    }
}

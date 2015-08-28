package robindarby.com.popularmovies.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import robindarby.com.popularmovies.activities.MainActivity;
import robindarby.com.popularmovies.data.MovieManager;
import robindarby.com.popularmovies.models.Movie;
import robindarby.com.popularmovies.requests.DiscoverMovieJSONRequest;
import robindarby.com.popularmovies.requests.MoviesJSONRequest;

/**
 * Created by darby on 7/13/15.
 */
public class MoviesService extends IntentService {

    private static final String TAG = "MOVIE_SERVICE";

    public static final String ACTION = "robindarby.com.popularmovies.services.MoviesService_ACTION";

    private static final String JSON_RESULTS_FIELD = "results";
    public static final String MOVIES_INTENT_EXTRA = "movies";

    public MoviesService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Discover new movies.
        DiscoverMovieJSONRequest request = new DiscoverMovieJSONRequest(getApplicationContext());

        JSONArray movieJSONList = null;
        try {
            JSONObject response = request.makeRequest();
            movieJSONList = response.getJSONArray(JSON_RESULTS_FIELD);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MovieManager manager = MovieManager.getInstance(getApplicationContext());

        ArrayList<Movie> movieList = new ArrayList<Movie>();
        if(movieJSONList != null) {
            for (int i = 0; i < movieJSONList.length(); i++) {
                try {
                    Movie newMovie = new Movie(movieJSONList.getJSONObject(i));
                    Movie oldMovie = manager.getMovie(newMovie.getId());
                    if(oldMovie != null) {
                        if(oldMovie.isFavorite()) newMovie.setFavorite(true);
                        manager.deleteMovie(oldMovie.getId());
                    }
                    manager.setMovie(newMovie);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        Intent resultsIntent = new Intent(ACTION);
        Log.d(TAG, "sendBroadcast");
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultsIntent);
    }

}

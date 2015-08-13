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

        ArrayList<Movie> movieList = new ArrayList<Movie>();
        if(movieJSONList != null) {
            for (int i = 0; i < movieJSONList.length(); i++) {
                try {
                    int movieId = movieJSONList.getJSONObject(i).getInt(Movie.ID_FIELD);
                    Movie movie = getFullMovie(movieId);
                    movie.setDiscovery(true);
                    movieList.add(movie);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Get favorite movies.
        ArrayList<String> favMovieIds = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.contains(MainActivity.FAVORITE_MOVIES_PREF)) {
            favMovieIds = new ArrayList<String>(preferences.getStringSet(MainActivity.FAVORITE_MOVIES_PREF, null));
        }

        if(favMovieIds != null) {
            for(String movieId : favMovieIds) {
                // do we already have this movie?
                boolean found = false;
                for(Movie movie : movieList) {
                    if(movie.getId() == Integer.valueOf(movieId)) {
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    try {
                        Movie movie = getFullMovie(Integer.valueOf(movieId));
                        movieList.add(movie);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        Intent resultsIntent = new Intent(ACTION);
        resultsIntent.putExtra(MOVIES_INTENT_EXTRA, movieList);
        Log.d(TAG, "sendBroadcast");
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultsIntent);
    }

    private Movie getFullMovie(int movieId) throws Exception {
        return new Movie(new MoviesJSONRequest(getApplicationContext(), movieId).makeRequest());
    }

}

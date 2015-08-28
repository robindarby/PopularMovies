package robindarby.com.popularmovies.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import robindarby.com.popularmovies.data.MovieManager;
import robindarby.com.popularmovies.models.Movie;
import robindarby.com.popularmovies.models.Review;
import robindarby.com.popularmovies.models.Video;
import robindarby.com.popularmovies.requests.DiscoverMovieJSONRequest;
import robindarby.com.popularmovies.requests.MoviesJSONRequest;

/**
 * Created by darby on 8/28/15.
 */
public class MovieDetailsService extends IntentService {
    private static final String TAG = "MOVIE_DET_SERVICE";

    public static final String ACTION = "robindarby.com.popularmovies.services.MoviesDetailsService_ACTION";

    public static final String MOVIE_ID_INTENT_EXTRA = "movieId";
    public static final String REVIEWS_INTENT_EXTRA = "reviews";
    public static final String CLIPS_INTENT_EXTRA = "clips";

    public MovieDetailsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int movieId = intent.getIntExtra(MOVIE_ID_INTENT_EXTRA, -1);
        ArrayList<Review> reviews = new ArrayList<Review>();
        ArrayList<Video> clips = new ArrayList<Video>();

        if(movieId > 0) {
            try {
                JSONObject json = new MoviesJSONRequest(getApplicationContext(), movieId).makeRequest();

                if(json.has(Movie.VIDEOS_FIELD)) {
                    JSONObject vidResults = json.getJSONObject(Movie.VIDEOS_FIELD);
                    JSONArray videosList = vidResults.getJSONArray(Movie.RESULTS_FIELD);
                    for(int i=0; i < videosList.length(); i++) {
                        JSONObject videoJSON = (JSONObject)videosList.get(i);
                        clips.add(new Video(videoJSON));
                    }
                }

                if(json.has(Movie.REVIEWS_FIELD)) {
                    JSONObject revResults = json.getJSONObject(Movie.REVIEWS_FIELD);
                    JSONArray revsList = revResults.getJSONArray(Movie.RESULTS_FIELD);
                    for(int i=0; i < revsList.length(); i++) {
                        JSONObject revJSON = (JSONObject)revsList.get(i);
                        reviews.add(new Review(revJSON));
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent resultsIntent = new Intent(ACTION);
        Log.d(TAG, "sendBroadcast");
        resultsIntent.putExtra(REVIEWS_INTENT_EXTRA, reviews);
        resultsIntent.putExtra(CLIPS_INTENT_EXTRA, clips);
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultsIntent);
    }
}

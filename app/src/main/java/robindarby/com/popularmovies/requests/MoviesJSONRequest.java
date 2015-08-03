package robindarby.com.popularmovies.requests;

import android.content.Context;

import robindarby.com.popularmovies.R;

/**
 * Created by darby on 7/15/15.
 */
public class MoviesJSONRequest extends HTTPJSONRequest {

    private final static String TAG = "DISCOVER_REQUEST";

    private final static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final static String APPEND_TO_RESP_PARAM = "append_to_response";

    public MoviesJSONRequest(Context context, int movieId) {
        super(context);
        setUrl(BASE_URL + movieId);
        setRequestParameter(APPEND_TO_RESP_PARAM, "videos,reviews");
        setRequestParameter(DiscoverMovieJSONRequest.API_KEY_PARAM, mContext.getResources().getString(R.string.themoviedb_api_key));
    }
}
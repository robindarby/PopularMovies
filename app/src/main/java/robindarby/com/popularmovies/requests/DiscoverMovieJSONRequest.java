package robindarby.com.popularmovies.requests;

import android.content.Context;

import robindarby.com.popularmovies.R;

/**
 * Created by darby on 7/10/15.
 */
public class DiscoverMovieJSONRequest extends HTTPJSONRequest {

    private final static String TAG = "DISCOVER_REQUEST";

    private final static String BASE_URL = "http://api.themoviedb.org/3/discover/movie";

    private final static String SORT_BY_PARAM = "sort_by";
    public final static String API_KEY_PARAM = "api_key";

    public DiscoverMovieJSONRequest(Context context) {
        super(context);
        setUrl(BASE_URL);
        setRequestParameter(SORT_BY_PARAM, "popularity.desc");
        setRequestParameter(API_KEY_PARAM, mContext.getResources().getString(R.string.themoviedb_api_key));
    }


}

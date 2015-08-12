package robindarby.com.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashSet;

import robindarby.com.popularmovies.R;
import robindarby.com.popularmovies.activities.MainActivity;
import robindarby.com.popularmovies.models.Movie;
import robindarby.com.popularmovies.models.Review;
import robindarby.com.popularmovies.models.Video;

/**
 * Created by darby on 7/16/15.
 */
public class MovieDetailsListViewAdapter extends BaseAdapter implements YouTubeThumbnailView.OnInitializedListener {

    private static final String TAG = "MOV_DETAILS_ADAP";

    private Activity mContext;
    private Movie mMovie;

    private static final int MOVIE_DETAILS_TYPE = 0;
    private static final int VIDEO_TYPE = 1;
    private static final int REVIEW_TYPE = 2;

    private LayoutInflater mInflater;

    private ArrayList<YouTubeThumbnailLoader> mLoaders = new ArrayList<YouTubeThumbnailLoader>();

    public MovieDetailsListViewAdapter(Activity context, Movie movie) {
        mContext = context;
        mMovie = movie;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (mInflater == null) throw new IllegalStateException ("unable to get Inflater Service");
    }

    @Override
    public int getCount() {
        return mMovie.getVideos().size() + mMovie.getReviews().size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MOVIE_DETAILS_TYPE;
        }
        else if(position < mMovie.getVideos().size() + 1) {
            return VIDEO_TYPE;
        }
        else {
            return REVIEW_TYPE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            if (position == 0) {
                convertView = mInflater.inflate(R.layout.movie_details_list_item, null);
            }
            else if(position < mMovie.getVideos().size() + 1) {
                convertView = mInflater.inflate(R.layout.video_list_item, null);
            }
            else {
                convertView = mInflater.inflate(R.layout.review_list_item, null);
            }
        }

        if (position == 0) { // details
            TextView titleTV = (TextView) convertView.findViewById(R.id.title_textView);
            titleTV.setText(mMovie.getTitle());

            ImageView posterIV = (ImageView)convertView.findViewById(R.id.poster_imageView);
            Picasso.with(convertView.getContext()).load(mMovie.getPosterPathForWidth(Movie.POSTER_WIDTH_500)).into(posterIV);

            TextView releaseDateTV = (TextView)convertView.findViewById(R.id.release_date_textView);
            DateFormat df = DateFormat.getDateInstance();
            releaseDateTV.setText(df.format(mMovie.getReleaseDate()));

            TextView ratingTV = (TextView)convertView.findViewById(R.id.rating_textView);
            ratingTV.setText(String.valueOf(mMovie.getVoteAverage()) + " / 10");

            TextView summaryTV = (TextView)convertView.findViewById(R.id.summary_textView);
            summaryTV.setText(mMovie.getOverview());

            final Button addToFavButton = (Button)convertView.findViewById(R.id.favorites_button);
            if(mMovie.isFavotite(convertView.getContext())) {
                addToFavButton.setText(convertView.getContext().getString(R.string.remove_favorites));
            }

            final int movieId = mMovie.getId();
            addToFavButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                    ArrayList<String> favMovieIds = new ArrayList<String>(preferences.getStringSet(MainActivity.FAVORITE_MOVIES_PREF, null));
                    if(favMovieIds.contains(String.valueOf(movieId))) {
                        favMovieIds.remove(String.valueOf(movieId));
                        addToFavButton.setText(v.getContext().getString(R.string.add_to_favorites));
                    }
                    else {
                        favMovieIds.add(String.valueOf(movieId));
                        addToFavButton.setText(v.getContext().getString(R.string.remove_favorites));
                    }
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putStringSet(MainActivity.FAVORITE_MOVIES_PREF, new HashSet<String>(favMovieIds));
                    editor.commit();
                }
            });
        }
        else if(position < mMovie.getVideos().size() + 1) { // Movie
            int videoIndex = position - 1;
            final Video video = mMovie.getVideos().get(videoIndex);
            YouTubeThumbnailView youTubeThumbView = (YouTubeThumbnailView) convertView.findViewById(R.id.youtube_thumbnailView);
            youTubeThumbView.initialize(convertView.getContext().getString(R.string.youtube_api_key), this);
            youTubeThumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(mContext, v.getContext().getString(R.string.youtube_api_key), video.getId(), 0, true, true);
                    v.getContext().startActivity(intent);
                }
            });
            youTubeThumbView.setTag(video.getId());
        }
        else { // review
            int reviewIndex = position - 1 - mMovie.getVideos().size();
            final Review review = mMovie.getReviews().get(reviewIndex);
            TextView authorTextView = (TextView)convertView.findViewById(R.id.author_textView);
            authorTextView.setText(review.getAuthor());

            TextView contentTextView = (TextView)convertView.findViewById(R.id.content_textView);
            contentTextView.setText(review.getContent());
        }


        return convertView;
    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
        String videoId = (String)youTubeThumbnailView.getTag();
        youTubeThumbnailLoader.setVideo(videoId);
        mLoaders.add(youTubeThumbnailLoader);
    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
        Log.v(TAG, "Failed to load thumb :( - " + youTubeInitializationResult);
    }

    public void releaseLoaders() {
        for(YouTubeThumbnailLoader loader : mLoaders) {
            loader.release();
        }
    }
}

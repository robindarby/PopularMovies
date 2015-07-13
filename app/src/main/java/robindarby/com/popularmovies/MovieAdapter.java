package robindarby.com.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import robindarby.com.popularmovies.models.Movie;

/**
 * Created by darby on 7/11/15.
 */
public class MovieAdapter extends BaseAdapter {

    private static final String TAG = "MOVIE_ADAPTER";

    private Context mContext;
    private ArrayList<Movie> mMovieList = new ArrayList<Movie>();

    private LayoutInflater mInflater;

    public MovieAdapter(Context context, ArrayList<Movie> movieList) {
        mContext = context;
        mMovieList = movieList;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (mInflater == null) throw new IllegalStateException ("unable to get Inflater Service");
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMovieList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = mMovieList.get(position);

        if(movie == null) return null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.movie_list_item, null);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.poster_imageView);
        Picasso.with(mContext).load(movie.getPosterPathForWidth(Movie.POSTER_WIDTH_185)).into(imageView);

        TextView ratingTV = (TextView) convertView.findViewById(R.id.rating_textView);
        ratingTV.setText(String.valueOf(movie.getVoteAverage()) + " / 10");

        TextView popTV = (TextView) convertView.findViewById(R.id.popular_textView);
        popTV.setText(String.valueOf((int)movie.getPopularity()));

        return convertView;
    }
}

package robindarby.com.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;

import robindarby.com.popularmovies.models.Movie;

/**
 * Created by darby on 7/11/15.
 */
public class MovieDetailsActivity extends Activity {

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        mMovie = (Movie)intent.getSerializableExtra(MainActivity.MOVIE_INTENT_EXTRA);

        TextView titleTV = (TextView) findViewById(R.id.title_textView);
        titleTV.setText(mMovie.getTitle());

        ImageView posterIV = (ImageView)findViewById(R.id.poster_imageView);
        Picasso.with(this).load(mMovie.getPosterPathForWidth(Movie.POSTER_WIDTH_500)).into(posterIV);

        TextView releaseDateTV = (TextView)findViewById(R.id.release_date_textView);
        DateFormat df = DateFormat.getDateInstance();
        releaseDateTV.setText(df.format(mMovie.getReleaseDate()));

        TextView ratingTV = (TextView)findViewById(R.id.rating_textView);
        ratingTV.setText(String.valueOf(mMovie.getVoteAverage()) + " / 10");

        TextView summaryTV = (TextView)findViewById(R.id.summary_textView);
        summaryTV.setText(mMovie.getOverview());
    }
}

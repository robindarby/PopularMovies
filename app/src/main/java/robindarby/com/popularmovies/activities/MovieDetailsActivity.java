package robindarby.com.popularmovies.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import robindarby.com.popularmovies.R;
import robindarby.com.popularmovies.activities.MainActivity;
import robindarby.com.popularmovies.adapters.MovieDetailsListViewAdapter;
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
        mMovie = (Movie) intent.getSerializableExtra(MainActivity.MOVIE_INTENT_EXTRA);

        ListView listView = (ListView) findViewById(R.id.movie_details_listView);
        listView.setAdapter(new MovieDetailsListViewAdapter(this, mMovie));
    }
}

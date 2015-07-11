package robindarby.com.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import robindarby.com.popularmovies.models.Movie;
import robindarby.com.popularmovies.requests.DiscoverMovieJSONRequest;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MAIN";

    private final static String JSON_RESULTS_FIELD = "results";

    protected ProgressDialog mLoadingDialog;
    private DiscoverMovieAsyncTask task;

    private ArrayList<Movie> movieList = new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // go get the movies in a background thread.
        task = new DiscoverMovieAsyncTask(this);
        task.execute();
    }

    private void updateMovies() {

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new MovieAdapter(this, movieList));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_most_pop_settings) {
            Collections.sort(movieList, Movie.MostPopularComparator);
            updateMovies();
            return true;
        }
        else if(id == R.id.action_rating_settings) {
            Collections.sort(movieList, Movie.RatedComparator);
            updateMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class DiscoverMovieAsyncTask extends AsyncTask<String, Void, JSONObject> {

        private Context mContext;

        public DiscoverMovieAsyncTask(Context context) {
            mContext = context;
        }

        protected void onPreExecute() {
            mLoadingDialog = ProgressDialog.show(mContext, "", getString(R.string.loading), true);
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            // call the movie DB API.
            DiscoverMovieJSONRequest request = new DiscoverMovieJSONRequest(mContext);

            try {
                return request.makeRequest();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        // invoked on the UI thread - so we are good to go!
        protected void onPostExecute(JSONObject json) {
            if(!isCancelled()) {
                mLoadingDialog.dismiss();

                // parse the json response into movie models.
                if (json != null) {
                    try {
                        movieList.clear();
                        JSONArray movieJSONList = json.getJSONArray(JSON_RESULTS_FIELD);
                        for(int i=0; i < movieJSONList.length(); i++) {
                            JSONObject movieJSON = movieJSONList.getJSONObject(i);
                            movieList.add(new Movie(movieJSON));
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    // make a call to update the UI.
                    updateMovies();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(task != null) task.cancel(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(task != null) task.cancel(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(task != null) task.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

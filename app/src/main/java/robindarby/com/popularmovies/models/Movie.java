package robindarby.com.popularmovies.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import robindarby.com.popularmovies.activities.MainActivity;

/**
 * Created by darby on 7/11/15.
 */
public class Movie implements Serializable {

    private static final long serialVersionUID = 6684019190445541112L;

    private static final String TAG = "MOVIE_MODEL";

    public static final String ID_FIELD = "id";
    private static final String ORIGNAL_LANG_FIELD = "original_language";
    private static final String ORIGNAL_TITLE_FIELD = "orignal_title";
    public static final String OVERVIEW_FIELD = "overview";
    public static final String RELEASE_DATE_FIELD = "release_date";
    private static final String BACKDROP_PATH_FIELD = "backdrop_path";
    private static final String ADULT_FIELD = "adult";
    private static final String GENRE_IDS_FIELD = "genre_ids";
    public static final String POSTER_PATH_FIELD = "poster_path";
    private static final String POPULARITY_FIELD = "popularity";
    public static final String TITLE_FIELD = "title";
    private static final String VIDEO_FIELD = "video";
    public static final String VOTE_AVG_FIELD = "vote_average";
    public static final String VOTE_COUNT_FIELD = "vote_count";
    public static final String VIDEOS_FIELD = "videos";
    public static final String REVIEWS_FIELD = "reviews";
    public static final String RESULTS_FIELD = "results";
    public static final String FAVORITE_FIELD = "favorite";

    private static final String POSTER_BASE_URL_STR = "http://image.tmdb.org/t/p/";
    public static final String POSTER_WIDTH_92 = "w92";
    public static final String POSTER_WIDTH_154 = "w154";
    public static final String POSTER_WIDTH_185 = "w185";
    public static final String POSTER_WIDTH_342 = "w342";
    public static final String POSTER_WIDTH_500 = "w500";
    public static final String POSTER_WIDTH_780 = "w780";
    public static final String POSTER_WIDTH_ORIGNAL = "original";

    private int id;
    private String title;
    private boolean adult = false;
    private String backdropPath;
    private ArrayList<String> genreIds = new ArrayList<String>();
    private String orignalLanguage;
    private String orignalTitle;
    private String overview;
    private Date releaseDate;
    private String posterPath;
    private double popularity;
    private boolean video = false;
    private double voteAverage;
    private int voteCount;
    private boolean favorite = false;


    public Movie(Cursor c) throws Exception {
        setId(c.getInt(c.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_MOVIE_ID)));
        setTitle(c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_TITLE)));
        setOverview(c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_OVERVIEW)));
        setPosterPath(c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_POSTER_PATH)));
        setVoteAverage(c.getFloat(c.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_VOTE_AVG)));
        setVoteCount(c.getInt(c.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_VOTE_COUNT)));
        String releaseDateStr = c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_RELEASE_DATE));
        DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        releaseDate = format.parse(releaseDateStr);
        setFavorite(c.getInt(c.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_FAVORITE)) == 1);
        setPopularity(c.getFloat(c.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_POPULARITY)));
    }

    public Movie(JSONObject json) throws Exception {
        if(json.has(ID_FIELD)) {
            setId(json.getInt(ID_FIELD));
        }

        if(json.has(TITLE_FIELD)) {
            setTitle(json.getString(TITLE_FIELD));
        }

        if(json.has(ADULT_FIELD)) {
            setAdult(json.getBoolean(ADULT_FIELD));
        }

        if(json.has(BACKDROP_PATH_FIELD)) {
            setBackdropPath(json.getString(BACKDROP_PATH_FIELD));
        }

        if(json.has(GENRE_IDS_FIELD)) {
            setGenreIds(json.getJSONArray(GENRE_IDS_FIELD));
        }

        if(json.has(ORIGNAL_LANG_FIELD)) {
            setOrignalLanguage(json.getString(ORIGNAL_LANG_FIELD));
        }

        if(json.has(ORIGNAL_TITLE_FIELD)) {
            setOrignalTitle(json.getString(ORIGNAL_TITLE_FIELD));
        }

        if(json.has(OVERVIEW_FIELD)) {
            setOverview(json.getString(OVERVIEW_FIELD));
        }

        if(json.has(RELEASE_DATE_FIELD)) {
            setReleaseDate(json.getString(RELEASE_DATE_FIELD));
        }

        if(json.has(POSTER_PATH_FIELD)) {
            setPosterPath(json.getString(POSTER_PATH_FIELD));
        }

        if(json.has(POPULARITY_FIELD)) {
            setPopularity(json.getDouble(POPULARITY_FIELD));
        }

        if(json.has(VIDEO_FIELD)) {
            setVideo(json.getBoolean(VIDEO_FIELD));
        }

        if(json.has(VOTE_AVG_FIELD)) {
            setVoteAverage(json.getDouble(VOTE_AVG_FIELD));
        }

        if(json.has(VOTE_COUNT_FIELD)) {
            setVoteCount(json.getInt(VOTE_COUNT_FIELD));
        }
    }


    public String getPosterPathForWidth(String width) {
        return POSTER_BASE_URL_STR + "/" + width + getPosterPath();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public ArrayList<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(JSONArray jsonArray) throws Exception {
        for(int i=0; i < jsonArray.length(); i++) {
            String genreId = jsonArray.getString(i);
            this.genreIds.add(genreId);
        }
    }

    public String getOrignalLanguage() {
        return orignalLanguage;
    }

    public void setOrignalLanguage(String orignalLanguage) {
        this.orignalLanguage = orignalLanguage;
    }

    public String getOrignalTitle() {
        return orignalTitle;
    }

    public void setOrignalTitle(String orignalTitle) {
        this.orignalTitle = orignalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDateStr) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        Date date = format.parse(releaseDateStr);
        this.releaseDate = date;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public static Comparator<Movie> MostPopularComparator = new Comparator<Movie>() {

        @Override
        public int compare(Movie lhs, Movie rhs) {

            int popA = (int)lhs.getPopularity();
            int popB = (int)rhs.getPopularity();

            return popB - popA;
        }

    };

    public static Comparator<Movie> RatedComparator = new Comparator<Movie>() {

        @Override
        public int compare(Movie lhs, Movie rhs) {

            int voteA = (int)lhs.getVoteAverage();
            int voteB = (int)rhs.getVoteAverage();

            return voteB - voteA;
        }

    };


    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(MovieEntry.COLUMN_NAME_MOVIE_ID, getId());
        values.put(MovieEntry.COLUMN_NAME_TITLE, getTitle());
        values.put(MovieEntry.COLUMN_NAME_OVERVIEW, getOverview());
        values.put(MovieEntry.COLUMN_NAME_POSTER_PATH, getPosterPath());
        values.put(MovieEntry.COLUMN_NAME_VOTE_AVG, getVoteAverage());
        values.put(MovieEntry.COLUMN_NAME_VOTE_COUNT, getVoteCount());
        values.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, getReleaseDate().toString());
        values.put(MovieEntry.COLUMN_NAME_FAVORITE, isFavorite());
        values.put(MovieEntry.COLUMN_NAME_POPULARITY, getPopularity());

        return values;
    }


    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_MOVIE_ID = Movie.ID_FIELD;
        public static final String COLUMN_NAME_TITLE = Movie.TITLE_FIELD;
        public static final String COLUMN_NAME_OVERVIEW = Movie.OVERVIEW_FIELD;
        public static final String COLUMN_NAME_POSTER_PATH = Movie.POSTER_PATH_FIELD;
        public static final String COLUMN_NAME_VOTE_AVG = Movie.VOTE_AVG_FIELD;
        public static final String COLUMN_NAME_VOTE_COUNT = Movie.VOTE_COUNT_FIELD;
        public static final String COLUMN_NAME_RELEASE_DATE = Movie.RELEASE_DATE_FIELD;
        public static final String COLUMN_NAME_FAVORITE = Movie.FAVORITE_FIELD;
        public static final String COLUMN_NAME_POPULARITY = Movie.POPULARITY_FIELD;
    }

}

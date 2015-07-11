package robindarby.com.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by darby on 7/11/15.
 */
public class Movie implements Serializable {

    private static final long serialVersionUID = 6684019190445541112L;

    private static final String TAG = "MOVIE_MODEL";

    private static final String ID_FIELD = "id";
    private static final String ORIGNAL_LANG_FIELD = "original_language";
    private static final String ORIGNAL_TITLE_FIELD = "orignal_title";
    private static final String OVERVIEW_FIELD = "overview";
    private static final String RELEASE_DATE_FIELD = "release_date";
    private static final String BACKDROP_PATH_FIELD = "backdrop_path";
    private static final String ADULT_FIELD = "adult";
    private static final String GENRE_IDS_FIELD = "genre_ids";
    private static final String POSTER_PATH_FIELD = "poster_path";
    private static final String POPULARITY_FIELD = "popularity";
    private static final String TITLE_FIELD = "title";
    private static final String VIDEO_FIELD = "video";
    private static final String VOTE_AVG_FIELD = "vote_average";
    private static final String VOTE_COUNT_FIELD = "vote_count";

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

}

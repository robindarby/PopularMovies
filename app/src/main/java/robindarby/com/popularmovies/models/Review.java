package robindarby.com.popularmovies.models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by darby on 7/16/15.
 */
public class Review implements Serializable {

    private static final long serialVersionUID = 6684019140225541112L;

    private final static String TAG = "REVIEW_MODEL";

    private final String ID_FIELD = "id";
    private final String AUTHOR_FIELD = "author";
    private final String CONTENT_FIELD = "content";
    private final String URL_FIELD = "url";

    private String id;
    private String author;
    private String content;
    private String url;

    public Review(JSONObject json) throws Exception {
        setId(json.getString(ID_FIELD));
        setAuthor(json.getString(AUTHOR_FIELD));
        setContent(json.getString(CONTENT_FIELD));
        setUrl(json.getString(URL_FIELD));
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

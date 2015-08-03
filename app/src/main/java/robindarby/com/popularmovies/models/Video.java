package robindarby.com.popularmovies.models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by darby on 7/16/15.
 */
public class Video implements Serializable {

    private static final long serialVersionUID = 6684019140445541112L;

    private final static String TAG = "VID_MODEL";

    private final String ID_FIELD = "key";
    private String id;

    public Video(JSONObject json) throws Exception {
        setId(json.getString(ID_FIELD));
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

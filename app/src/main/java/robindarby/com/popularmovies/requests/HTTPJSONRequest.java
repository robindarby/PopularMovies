package robindarby.com.popularmovies.requests;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import robindarby.com.popularmovies.R;

/**
 * Created by darby on 7/10/15.
 */
public class HTTPJSONRequest {

    private static final String TAG = "HTTP_REQUEST";

    private final static int DEFAULT_CONNECTION_TIMEOUT = 50000;
    private final static int DEFAULT_SOCKET_TIMEOUT = 60000;


    public Context mContext;

    private ContentValues requestParameters = new ContentValues();
    private String mUrlStr;

    public HTTPJSONRequest(Context context) {
        this.mContext = context;
    }

    public void setRequestParameter(String field, String value) {
        this.requestParameters.put(field, value);
    }

    public JSONObject makeRequest() throws Exception {

        String reqUrlStr = getUrl();
        Log.d(TAG, "Making request: " + reqUrlStr);
        URL url = new URL(reqUrlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200) {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String response = IOUtils.toString(in, "UTF-8");
            Log.d(TAG, "Got response: " + response);
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject;
        }
        else {
            Log.e(TAG, "None 200 response for: " + reqUrlStr);
        }

        return null;
    }

    public String getUrl() {
        String query = "";
        if (requestParameters != null) {
            ArrayList<String> parts = new ArrayList<String>();
            for(String key : requestParameters.keySet()) {
                String value = (String) requestParameters.get(key);
                parts.add(key + "=" + value);
            }
            query = StringUtils.join(parts.toArray(), "&");
        }
        return this.mUrlStr + "?" + query;
    }

    public void setUrl(String url) {
        this.mUrlStr = url;
    }

}

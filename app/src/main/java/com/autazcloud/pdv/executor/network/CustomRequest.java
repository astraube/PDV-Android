package com.autazcloud.pdv.executor.network;

/**
 * Created by andre on 29/07/2015.
 */

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class CustomRequest extends Request<JSONArray> {

    private Listener<JSONArray> listener;
    private Map<String, String> params;

    public CustomRequest(int method, String url, Map<String, String> params,
                         Listener<JSONArray> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            //MainActivity.printDebug("CustomRequest ---> response: " + response.data);
            //MainActivity.printDebug("CustomRequest ---> headers: " + response.headers);
            //MainActivity.printDebug("CustomRequest ---> jsonString: " + jsonString);
            
            //Log.i("--->CustomRequest", "response: " + response.toString());
            //Log.i("--->CustomRequest", "response.data: " + response.data);
            //Log.i("--->CustomRequest", "response.headers: " + response.headers);
            //Log.i("--->CustomRequest", "jsonString: " + jsonString);

            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        listener.onResponse(response);
    }
}
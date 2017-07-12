package com.autazcloud.pdv.executor.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.autazcloud.pdv.domain.constants.Constants;
import com.autazcloud.pdv.domain.constants.Metadata;
import com.autazcloud.pdv.domain.interfaces.Transaction;
import com.autazcloud.pdv.domain.models.Device;
import com.autazcloud.pdv.domain.models.DeviceSend;
import com.autazcloud.pdv.domain.models.WrapObjToNetwork;

import org.json.JSONArray;

import java.util.HashMap;

/**
 * Created by andre on 29/07/2015.
 */
public class NetworkConnection {

    private final String TAG = NetworkConnection.class.getSimpleName();
    private static NetworkConnection instance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    public NetworkConnection(Context c) {
        this.mContext = c;
        this.mRequestQueue = getRequestQueue();
    }
    
    public static synchronized NetworkConnection getInstance( Context c ){
        if( instance == null ){
            instance = new NetworkConnection( c.getApplicationContext() );
        }
        return( instance );
    }

    public RequestQueue getRequestQueue(){
        if( mRequestQueue == null ){
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return(mRequestQueue);
    }
    
    public void cancelPendingRequests(Object tag) {
       if (mRequestQueue != null) {
          mRequestQueue.cancelAll(tag);
       }
    }

    public <T> void addRequestQueue( Request<T> req, String tag){
    	// set the default tag if tag is empty
    	req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    
    public <T> void addRequestQueue( Request<T> req){
    	// set the default tag if tag is empty
    	req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void execute( WrapObjToNetwork object, String tag, final String host) {
    	execute(null, object, tag, host);
    }
    
    public void execute( final Transaction transaction, String tag, final String host) {
    	execute(transaction, null, tag, host);
    }
    
    public void execute( final Transaction transaction, WrapObjToNetwork object, String tag, final String host) {
    	WrapObjToNetwork obj;
    	
    	if( transaction != null ){
        	obj = transaction.doBefore(); // Recupera parametros
        } else {
        	obj = object;
        }
    	
        if( obj == null ) {
            // TODO mostrar mensagem
            return;
        }
        
        HashMap<String, String> params = new HashMap<>();
        
        try {
            params.put(Constants.METHOD, obj.getMethod());
            
	        if(obj.getObj() instanceof Device) {
	        	Device dev = (Device) obj.getObj();
	        	
	        	if (!TextUtils.isEmpty(dev.getSerialNumber()))
	        		params.put(Metadata.DEVICE_SERIAL_NUMBER, dev.getSerialNumber());
	        	
	        	if (!TextUtils.isEmpty(dev.getToken()))
	        			params.put(Metadata.DEVICE_TOKEN, dev.getToken());
			} else if(obj.getObj() instanceof DeviceSend) {
	        	DeviceSend infos = (DeviceSend) obj.getObj();
	        	
                params.put(Constants.METHOD, "PUT");
                params.put(Constants.METHOD_API, "PUT");

                params.put(Metadata.LATITUDE, Double.toString(infos.getLatitude()));
                params.put(Metadata.LONGITUDE, Double.toString(infos.getLongitude()));
                params.put(Metadata.DEVICE_SERIAL_NUMBER, infos.getDevice().getSerialNumber());
                params.put(Metadata.METADATA, infos.getMetaJson());
			}
        }
		catch(Exception | StackOverflowError e){ e.printStackTrace(); }
        
        
        //Log.e(TAG, "HOST: " + host);
        //Log.e(TAG, "params: " + params);
        
        //MainActivity.printDebug("HOST: " + host);
        //MainActivity.printDebug("params: " + params);
        
        
        // Identifica o metodo
        //int method = identifyMethod(obj.getMethod());
        
        CustomRequest request = new CustomRequest(Request.Method.POST,
                host,
                params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                    	if( transaction != null ){
                    		transaction.doAfter(response);
                    	}
                        //Log.i(TAG, "response: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                    	Log.e(TAG, "VolleyError");
                    	if( transaction != null ){
                    		transaction.doAfter(null);
                    	}
                        e.printStackTrace();
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequestQueue(request, tag);
    }
    
    /**
     * Identifica o metodo de requisi��o
     * @param method
     * @return
     */
    public int identifyMethod(String method) {
    	int m = 0;
        switch (method) {
	        case "POST":
	        	m = Request.Method.POST;
	        case "PUT":
	        	m = Request.Method.PUT;
	        case "GET":
	        	m = Request.Method.GET;
        }
        return m;
    }
}

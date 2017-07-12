package com.autazcloud.pdv.domain.models;

import android.util.Log;

import java.io.IOException;
import java.io.Serializable;

/**
 * Utilizada para requisi��es com volley.
 * � apenas o objeto de envio de requisi��es
 * 
 * Created by andre on 29/07/2015.
 * @author andre straube
 * @version 1.0
 */
public class WrapObjToNetwork implements Serializable {
	
	private static final long serialVersionUID = 445236569887978L;
    private Object obj;
    private String method;

    public WrapObjToNetwork(Object object, String method) {
        this.obj = object;
        this.method = method;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    	Log.i("WrapObjToNetwork", "writeObject");
    }
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    	Log.i("WrapObjToNetwork", "readObject");
    }
    
    public String getMethod() { return method; }
	public void setMethod(String method) { this.method = method; }
	
	public Object getObj() { return obj; }
	public void setObj(String obj) { this.obj = obj; }
}

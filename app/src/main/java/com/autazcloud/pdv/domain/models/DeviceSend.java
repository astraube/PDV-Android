package com.autazcloud.pdv.domain.models;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Dados para enviar ao servidor, como relat�rio do Aparelho
 * Created by andre on 14/10/2015.
 */
public class DeviceSend {

    private double latitude;
    private double longitude;
    private Map<String, String> metadata;
    private Device mDevice;

    private Context mContext;

    public DeviceSend (Context context, Device device) {
        this.mContext = context;
        metadata = new HashMap<String, String>();
        mDevice = device;
    }
    
    public Map<String, String> getMetadata() { return metadata; }
    public void addMetadata(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value))
            metadata.put(key, value);
    }
    public String getMetaJson() {
        Gson gson = new Gson();
        String json = gson.toJson(metadata);
        return json;
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public Device getDevice() { return mDevice; }
    public void setDevice(Device device) { this.mDevice = device; }
}

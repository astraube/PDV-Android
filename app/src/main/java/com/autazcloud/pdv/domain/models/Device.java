package com.autazcloud.pdv.domain.models;

import android.content.Context;
import android.text.TextUtils;

import com.autazcloud.pdv.domain.constants.Metadata;
import com.autazcloud.pdv.helpers.IDManagement;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Device {

	private int ID;
	private String token;
	private String serialNumber;
	private String model;
	private String brand;
    private Map<String, String> metadata = null;

    private Context mContext;
    
    public Device (Context context) {
        this.mContext = context;
        this.serialNumber = IDManagement.getDeviceSerialNumber(); 
        this.model = IDManagement.getDeviceModel(); 
        this.brand = IDManagement.getDeviceBrand();
        
        addMetadata(Metadata.DEVICE_PHONE_NUMBER, IDManagement.getPhoneNumber());
        addMetadata(Metadata.DEVICE_BATTERY_LEVEL, IDManagement.getBatteryLevel() + "%");
    }

    public int getId() { return ID; }
    public void setId(int id) { this.ID = id; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    
    public String getModeln() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public Map<String, String> getMetadata() { return metadata; }
    public void addMetadata(String key, String value) {
    	if (metadata == null)
    		metadata = new HashMap<String, String>();
    	
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value))
            metadata.put(key, value);
    }
    public String getMetaJson() {
    	if (metadata == null)
    		return "";
    	
        Gson gson = new Gson();
        String json = gson.toJson(metadata);
        return json;
    }
    
}

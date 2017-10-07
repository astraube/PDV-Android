package br.com.i9algo.autaz.pdv.domain.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import br.com.i9algo.autaz.pdv.domain.constants.Metadata;
import br.com.i9algo.autaz.pdv.helpers.DeviceUuidFactory;
import br.com.i9algo.autaz.pdv.helpers.IDManagement;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Device extends RealmObject implements Parcelable {

    public static final double CURRENT_VERSION = 0.1; // Apenas para conhecimento de versao


    @Since(0.1)
    @SerializedName("public_token")
    @Expose
    @Index
    @PrimaryKey
    private String publicToken;

    @Since(0.1)
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @Since(0.1)
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @Since(0.1)
    @SerializedName("name")
    @Expose
    private String name;

    @Since(0.1)
    @SerializedName("serial_number")
    @Expose
    private String serialNumber;

    @Since(0.1)
    @SerializedName("uuid")
    @Expose
    private String uuid;

    @Since(0.1)
    @SerializedName("imei")
    @Expose
    private String imei;

    @Since(0.1)
    @Ignore
    @SerializedName("data")
    @Expose
    private Map<String, String> data = null;


    public Device () {
        initMetadata();
    }
    public Device(Device device) {
        super();
        this.publicToken = device.getPublicToken();
        this.createdAt = device.getCreatedAt();
        this.updatedAt = device.getUpdatedAt();
        this.name = device.getName();
        this.uuid = device.getUuid();
        this.serialNumber = device.getSerialNumber();
        this.imei = device.getImei();
    }

    public Device (Context context) {
        if (StringUtils.isEmpty(this.uuid))
            this.setUuid((new DeviceUuidFactory(context)).getDeviceUuid().toString());

        if (StringUtils.isEmpty(this.serialNumber))
            this.setSerialNumber(IDManagement.getDeviceSerialNumber());

        if (StringUtils.isEmpty(this.imei))
            this.setImei(IDManagement.getDeviceImei());

        initMetadata();
    }

    public static String getRouteKeyName()
    {
        return "publicToken";
    }


    public String getPublicToken() {
        return publicToken;
    }
    /*private void setPublicToken(String publicToken) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.publicToken = publicToken;

        _realm.commitTransaction();
    }*/

    public String getCreatedAt() {
        return createdAt;
    }
    /*private void setCreatedAt(String createdAt) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.createdAt = createdAt;

        _realm.commitTransaction();
    }*/

    public String getUpdatedAt() {
        return updatedAt;
    }
    /*private void setUpdatedAt(String updatedAt) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.updatedAt = updatedAt;

        _realm.commitTransaction();
    }*/

    public String getName() {
        return name;
    }
    public void setName(String name) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.name = name;

        _realm.commitTransaction();
    }

    public String getUuid() { return uuid; }
    private void setUuid(String uuid) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.uuid = uuid;

        _realm.commitTransaction();
    }

    public String getSerialNumber() {
        return serialNumber;
    }
    private void setSerialNumber(String serialNumber) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.serialNumber = serialNumber;

        _realm.commitTransaction();

        if (StringUtils.isEmpty(name))
            this.setName(serialNumber);
    }

    public String getImei() { return imei; }
    private void setImei(String imei) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.imei = imei;

        _realm.commitTransaction();
    }


    public void initMetadata() {
        addMetadata(Metadata.DEVICE_MODEL, IDManagement.getDeviceModel());
        addMetadata(Metadata.DEVICE_BRAND, IDManagement.getDeviceBrand());
        addMetadata(Metadata.DEVICE_PHONE_NUMBER, IDManagement.getPhoneNumber());
        //addMetadata(Metadata.DEVICE_BATTERY_LEVEL, IDManagement.getBatteryLevel() + "%");
    }
    public Map<String, String> getMetadata() { return data; }
    public void addMetadata(String key, String value) {
        //Realm _realm = Realm.getDefaultInstance();
        //_realm.beginTransaction();

    	if (data == null)
            data = new HashMap<String, String>();
    	
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value))
            data.put(key, value);

        //_realm.commitTransaction();
    }
    public String getMetaJson() {
        if (data == null)
            return "";

        Gson gson = new Gson();
        String json = gson.toJson(data);
        return json;
    }


    /// PARCELABLE
    protected Device(Parcel parcel) {
        //setPublicToken(parcel.readString());
        //setUpdatedAt(parcel.readString());
        //setCreatedAt(parcel.readString());
        setName(parcel.readString());
        setSerialNumber(parcel.readString());
        setUuid(parcel.readString());
        setImei(parcel.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getPublicToken());
        dest.writeString(getUpdatedAt());
        dest.writeString(getCreatedAt());
        dest.writeString(getName());
        dest.writeString(getSerialNumber());
        dest.writeString(getUuid());
        dest.writeString(getImei());
    }
    public static final Parcelable.Creator<Device> CREATOR = new Parcelable.Creator<Device>() {
        @SuppressWarnings({
                "unchecked"
        })
        @Override
        public Device createFromParcel(Parcel source) {
            return new Device(source);
        }
        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };
    
}

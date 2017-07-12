package com.autazcloud.pdv.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * @author andre straube
 * @version 2.0 - 15/12/2015
 * @user: <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
 */
@SuppressLint("InlinedApi")
public class IDManagement {

    private static Context _context;
    private static TelephonyManager _telMgr;
    private static DeviceUuidFactory _deviceUid;

    public static void init(Context context) {
        IDManagement._context = context;
        IDManagement._telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        IDManagement._deviceUid = new DeviceUuidFactory(context);
    }

    public static UUID getDeviceUuid () {
        return IDManagement._deviceUid.getDeviceUuid();
    }

    public static String getDeviceImei () {
        String result = Settings.Secure.ANDROID_ID;
        try {
            if (ActivityCompat.checkSelfPermission(IDManagement._context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            if (isSIMAvailable() && hasTelephony()) {
            /*
            String s =  telMgr.getDeviceId()                + "\n TelephonyManager.getDeviceId - " +
                        telMgr.getDeviceSoftwareVersion()       + "\n TelephonyManager.getDeviceSoftwareVersion - " +
                        telMgr.getGroupIdLevel1()               + "\n TelephonyManager.getGroupIdLevel1 - " +
                        telMgr.getLine1Number()                 + "\n TelephonyManager.getLine1Number - " +
                        telMgr.getMmsUAProfUrl()                + "\n TelephonyManager.getMmsUAProfUrl - " +
                        telMgr.getMmsUserAgent()                + "\n TelephonyManager.getMmsUserAgent - " +
                        telMgr.getNetworkCountryIso()           + "\n TelephonyManager.getNetworkCountryIso - " +
                        telMgr.getNetworkOperator()             + "\n TelephonyManager.getNetworkOperator - " +
                        telMgr.getNetworkOperatorName()         + "\n TelephonyManager.getNetworkOperatorName - " +
                        telMgr.getSimCountryIso()               + "\n TelephonyManager.getSimCountryIso - " +
                        telMgr.getSimOperator()                 + "\n TelephonyManager.getSimOperator - " +
                        telMgr.getSimSerialNumber()             + "\n TelephonyManager.getSimSerialNumber - " +
                        telMgr.getVoiceMailNumber()             + "\n TelephonyManager.getVoiceMailNumber - " +
                        telMgr.getVoiceMailAlphaTag()           + "\n TelephonyManager.getVoiceMailAlphaTag - " +
                        telMgr.getDeviceSoftwareVersion()       + "\n TelephonyManager.getDeviceSoftwareVersion - " +
                        telMgr.getSimState()                    + "\n TelephonyManager.getSimState";

                */
                result = IDManagement._telMgr.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return result;
    }

	public static float getBatteryLevel() {
        Intent batteryIntent = IDManagement._context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float)level / (float)scale) * 100.0f;
    }

	public static String getDevice() {
		return Build.DEVICE;
	}

    /**
     * Tenta recupar o SERIAL NUMBER do deive de dois metodos.
     * Caso nao consiga pegar o SERIAL retorna NULL.
     * A API WEB verifica se é NULL, e cria um SERIAL "VIRTUAL" para o device
     * @return
     */
    public static String getDeviceSerialNumber() {
        String serial = Build.SERIAL;

        if (serial == Build.UNKNOWN) {
            serial = Settings.Secure.getString(IDManagement._context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        if ((serial != Build.UNKNOWN || serial != "newer_value")) {
            return serial;
        } else {
            try {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            } catch (Exception ignored) {
            }
        }
        serial = (serial != Build.UNKNOWN || serial != "newer_value") ? serial : null;
        return serial;
    }
	
	public static String getDeviceModel() { return Build.MODEL; }

    public static String getDeviceBrand() { return Build.BRAND; }

    public static String getAllDeviceBuild() {
        String s =  Build.MODEL             + "\n Build.MODEL - " +
                    Build.BOARD             + "\n Build.BOARD - " +
                    Build.BOOTLOADER        + "\n Build.BOOTLOADER - " +
                    Build.BRAND             + "\n Build.BRAND - " +
                    Build.DISPLAY           + "\n Build.DISPLAY - " +
                    Build.FINGERPRINT       + "\n Build.FINGERPRINT - " +
                    Build.getRadioVersion() + "\n Build.getRadioVersion() - " +
                    Build.HARDWARE          + "\n Build.HARDWARE - " +
                    Build.HOST              + "\n Build.HOST - " +
                    Build.ID                + "\n Build.ID - " +
                    Build.MANUFACTURER      + "\n Build.MANUFACTURER - " +
                    Build.PRODUCT           + "\n Build.PRODUCT - " +
                    Build.TAGS              + "\n Build.TAGS - " +
                    Build.TYPE              + "\n Build.TYPE - " +
                    Build.USER              + "\n Build.USER";
        return s;
    }
	
	public static String getPhoneNumber() {
        String result = Settings.Secure.ANDROID_ID;
        try {
            if(isSIMAvailable() && hasTelephony()){
                result = IDManagement._telMgr.getLine1Number();
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return result;

	}
	
	public static List<CellInfo> getAllCellInfo() {
        if (Build.VERSION.SDK_INT < 16)
            return null;

		return IDManagement._telMgr.getAllCellInfo();
	}

    public static boolean isSIMAvailable() {
        int simState = IDManagement._telMgr.getSimState();

        switch (simState) {
	        case TelephonyManager.SIM_STATE_ABSENT:
	            return false;
	        case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
	            return false;
	        case TelephonyManager.SIM_STATE_PIN_REQUIRED:
	            return false;
	        case TelephonyManager.SIM_STATE_PUK_REQUIRED:
	            return false;
	        case TelephonyManager.SIM_STATE_READY:
	            return true;
	        case TelephonyManager.SIM_STATE_UNKNOWN:
	            return false;
	        default:
	            return false;
        }
    }

    public static boolean hasTelephony()
    {
        TelephonyManager tm = IDManagement._telMgr;
        if (tm == null)
            return false;

        //devices below are phones only
        if (Build.VERSION.SDK_INT < 5)
            return true;

        PackageManager pm = IDManagement._context.getPackageManager();

        if (pm == null)
            return false;

        boolean retval = false;
        try
        {
            Class<?> [] parameters = new Class[1];
            parameters[0] = String.class;
            Method method = pm.getClass().getMethod("hasSystemFeature", parameters);
            Object [] parm = new Object[1];
            parm[0] = "android.hardware.telephony";
            Object retValue = method.invoke(pm, parm);
            if (retValue instanceof Boolean)
                retval = ((Boolean) retValue).booleanValue();
            else
                retval = false;
        }
        catch (Exception e)
        {
            retval = false;
        }

        return retval;
    }

}
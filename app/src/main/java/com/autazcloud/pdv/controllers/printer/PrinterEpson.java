package com.autazcloud.pdv.controllers.printer;

import android.content.Context;
import android.util.Log;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.helpers.ShowMsg;
import com.epson.eposprint.Builder;
import com.epson.eposprint.EposException;
import com.epson.eposprint.Print;


public class PrinterEpson {

	private static Print instancePrint = null;

	public static int mDeviceType = Print.DEVTYPE_USB;
	public static String mDeviceAddress = null;
	public static String mDeviceName = "TM-T20";
	
	public static int lang = Builder.MODEL_ANK;
	public static final int SEND_TIMEOUT = 10 * 1000;
	
	/**
	 * @author andre straube
	 * @param context
	 * @param deviceType Print.DEVTYPE_USB
	 * @param deviceAddress
	 * @param statusMonitor true
	 * @param interval 1000
	 */
	public static void openPrinter(Context context, int deviceType, String deviceAddress, boolean statusMonitor, int interval) {
		mDeviceType = deviceType;
		mDeviceAddress = deviceAddress;
		
		int enabled = Print.FALSE;
		if (statusMonitor)
			enabled = Print.TRUE;
		
		try{
			getPrinter(context).openPrinter(deviceType, deviceAddress, enabled, interval, Print.PARAM_DEFAULT);
        }catch(Exception e) {
            ShowMsg.showException(e, context.getString(R.string.handlingmsg_ex_connect), context);
            return;
        }
	}
	
	public static Print getPrinter(Context context) {
		if (mDeviceAddress == null)
			ShowMsg.show(context.getString(R.string.handlingmsg_err_not_config), context);
		
		try {
			instancePrint = new Print(context);
			Log.v("PrinterEpson", "mDeviceAddress: " + mDeviceAddress);
			instancePrint.openPrinter(mDeviceType, mDeviceAddress, Print.FALSE, 1000, Print.PARAM_DEFAULT);
		} catch (EposException e) {
			e.printStackTrace();
		}
		return instancePrint;
	}
}

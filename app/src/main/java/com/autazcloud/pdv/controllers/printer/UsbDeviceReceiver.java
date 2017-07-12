package com.autazcloud.pdv.controllers.printer;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class UsbDeviceReceiver extends BroadcastReceiver {
	   
	 @SuppressLint("ShowToast")
	@Override
	 public void onReceive(Context context, Intent i) {
	 	Log.i("UsbDeviceReceiver", "Impressora");
	 	Toast.makeText(context, "A impressora foi desconectada.", Toast.LENGTH_LONG);
	 }
}
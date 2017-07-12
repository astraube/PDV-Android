package com.autazcloud.pdv.executor.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


/**
 * <p>Thread executada em forma de pilha</p>
 * 
 * <p>Usage:
 * <b>Iniciar Service</b>
 * Intent i = new Intent("SERVICE_INTENT");
 *
 * <b>Normal Start Service</b>
 * startService(i);
 *
 * <b>Start the service, keeping the device awake while it is launching.</b>
 * startWakefulService(i);
 *
 * <b>Stop Service</b>
 * Intent i = new Intent("SERVICE_INTENT");
 * i.putExtra(ServiceIntent.TURN_OFF, 1);
 * startService(i);
 *
 * OU
 *
 * Intent i = new Intent(getActivity(), ServiceIntent.class);
 * i.setData(Uri.parse(dataUrl));
 * startService(i);
 * </p>
 * 
 * @author andre
 *
 */
public class ServiceIntent extends IntentService {


	public static final String SERVICE_INTENT = "SERVICE_INTENT";
	public static final String TURN_OFF = "TURN_OFF";

	private String TAG = "";
	private int count;
	private boolean ativo;
	private boolean stopAll;
	
	public ServiceIntent() {
		super(SERVICE_INTENT);
		TAG = getClass().getSimpleName();
		
		stopAll = true;
		ativo = true;
		count = 0;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle b = intent.getExtras();
		if (b != null) {
			int desligar = b.getInt(TURN_OFF);
			if (desligar == 1) {
				stopAll = false;
			}
		}
		Log.i(TAG, "onStartCommand flags: " + flags + " - startId: " + startId);
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent workIntent) {
		// Gets data from the incoming Intent
		String dataString = workIntent.getDataString();
		Log.i(TAG, "onHandleIntent: " + dataString);

		while (stopAll && ativo && count < 20) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count++;
			Log.i(TAG, "onHandleIntent count: " + count);
		}
		ativo = true;
		count = 0;
	}

}

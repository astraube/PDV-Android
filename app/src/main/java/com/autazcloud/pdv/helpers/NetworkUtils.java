package com.autazcloud.pdv.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	
	/**
	 * <uses-permission android:name="android.permission.INTERNET" />
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * 
	 * Verifica a conex�o do device.
	 * Pode estar conectado via wifi ou 3G
	 * @param context
	 * @return boolean
	 * @version 2.0 - 01/07/2015
	 */
	public static boolean isOnline(Context context)
	{
		final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);//Pego a conectividade do contexto o qual o metodo foi chamado
		final NetworkInfo netInfo = cm.getActiveNetworkInfo();//Crio o objeto netInfo que recebe as informacoes da NEtwork
		return (netInfo != null && netInfo.isConnected());
    }
}

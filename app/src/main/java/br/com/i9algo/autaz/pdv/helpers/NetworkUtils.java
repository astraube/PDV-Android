package br.com.i9algo.autaz.pdv.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtils {

	private static final String TAG = NetworkUtils.class.getSimpleName();

	/**
	 * <uses-permission android:name="android.permission.INTERNET" />
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * 
	 * Verifica a conexao do device.
	 * Pode estar conectado via wifi ou 3G
	 * @param context
	 * @return boolean
	 * @version 2.0 - 01/07/2015
	 */
	public static boolean isOnline(Context context)
	{
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (cm != null) {
				Log.d(TAG, "^ isOnline()=true");
				return cm.getActiveNetworkInfo().isConnected();
			} else {
				Log.d(TAG, "^ isOnline()=false");
				return false;
			}

		} catch (Exception e) {
			Log.e(TAG, "^ isOnline()=false", e);
			return false;
		}
    }
}

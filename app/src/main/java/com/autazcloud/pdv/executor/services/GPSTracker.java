package com.autazcloud.pdv.executor.services;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

/**
 * Created by andre on 15/10/2015.
 */
public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // flag para o status do GPS
    boolean isGPSEnabled = false;

    // flag para o status da rede
    boolean isNetworkEnabled = false;

    // flag para o status do GPS
    boolean canGetLocation = false;

    Location location; // localizacao
    double latitude; // latitude
    double longitude; // longitude

    private static GPSTracker mInstance = null;

    // A distancia minima, em metros, para mudar as atualizacaes
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // O tempo minimo, em milissegundos, entre as atualizacaes
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 5; // 1 minute

    // Declarando um Location Manager
    protected LocationManager locationManager;

    public static GPSTracker getInstance (Context context) {
        if (mInstance != null)
            return (mInstance);

        return (new GPSTracker(context));
    }

    public GPSTracker(Context context) {
        this.mContext = context;
        mInstance = this;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // pegando o status do GPS
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // pegando o status da rede
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // nenhum provedor de rede esta habilitado
            } else {
                this.canGetLocation = true;
                // Primeira obtencao da localizacao pelo provedor de rede
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    //Log.d("Network", "Network");

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // se GPS habilitado pega lat/long usando os servicos do GPS
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        //Log.d("GPS Enabled", "GPS Enabled");

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Para de usar o listener do GPS
     * Chamando esse metodo o GPS vai parar em seu aplicativo
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Metodo para obter a latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Metodo para pegar a longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Metodo para checar se o GPS/Wi-Fi esta habilitado
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Metodo para mostrar as configuracoes me um AlertDialog
     * Pressionando Configuracoes o botao vai mostrar Opcoes de Configuracao
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Definindo Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Definindo Dialog Message
        alertDialog.setMessage("GPS nao esta habilitado. Voce deseja ir em configuracoes?");

        // Ao pressionar o botao Configuracoes
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // Ao pressionar o botao cancelar
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Mostrando Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
package br.com.i9algo.autaz.pdv.data.remote.repositoryes;

import android.content.Context;

import br.com.i9algo.autaz.pdv.domain.constants.Metadata;
import br.com.i9algo.autaz.pdv.domain.models.Device;
import br.com.i9algo.autaz.pdv.domain.models.DeviceSend;
import br.com.i9algo.autaz.pdv.executor.services.GPSTracker;

/**
 * Created by aStraube on 13/07/2017.
 */

public class DeviceRepository {

    public synchronized void syncInfos(Context mContext) {
        Device device = new Device(mContext);
        DeviceSend infos = new DeviceSend(mContext, device);

        //infos.addMetadata(Metadata.ACTION_SEND, action.toString());

        GPSTracker gps = GPSTracker.getInstance(mContext);
        // checa se o GPS esta habilitado
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            infos.setLatitude(latitude);
            infos.setLongitude(longitude);
        }
        infos.addMetadata(Metadata.DEVICE_BRAND, infos.getDevice().getBrand());
        infos.addMetadata(Metadata.DEVICE_MODEL, infos.getDevice().getModeln());

        if (device.getMetadata() != null) {
            for (String key : device.getMetadata().keySet()) {
                String value = device.getMetadata().get(key);
                infos.addMetadata(key, value);
            }
        }

        //WrapObjToNetwork obj = new WrapObjToNetwork(infos, "geo");
        //WrapRequestToNetwork o = new WrapRequestToNetwork(infos, "geo", Constants.SERVER_GEO);
        //NetworkConnection.getInstance(mContext).execute(obj, SenderDeviceInfos.class.getName(), ServerConstants.SERVER_GEO);
    }
}

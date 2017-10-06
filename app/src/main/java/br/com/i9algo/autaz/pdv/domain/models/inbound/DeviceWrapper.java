package br.com.i9algo.autaz.pdv.domain.models.inbound;


import br.com.i9algo.autaz.pdv.domain.models.Device;

/**
 * Created by aStraube on 30/06/2017.
 */

public class DeviceWrapper {

    public Object error;
    public Object success;
    public Device data;

    public Device getModel() { return data; }
    public void setModel(Device data) { this.data = data; }
}

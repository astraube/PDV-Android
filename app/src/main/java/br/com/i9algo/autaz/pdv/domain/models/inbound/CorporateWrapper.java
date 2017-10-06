package br.com.i9algo.autaz.pdv.domain.models.inbound;


import br.com.i9algo.autaz.pdv.domain.models.Corporate;

/**
 * Created by aStraube on 30/06/2017.
 */

public class CorporateWrapper {

    public Object error;
    public Object success;
    public Corporate data;

    public Corporate getModel() { return data; }
    public void setModel(Corporate data) { this.data = data; }
}

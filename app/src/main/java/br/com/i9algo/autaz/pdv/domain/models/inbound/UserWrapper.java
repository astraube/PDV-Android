package br.com.i9algo.autaz.pdv.domain.models.inbound;

import br.com.i9algo.autaz.pdv.domain.models.User;

public class UserWrapper {

    public Object error;
    public Object success;
    public User data;

    public User getModel() { return data; }
    public void setModel(User data) { this.data = data; }
}

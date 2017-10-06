package br.com.i9algo.autaz.pdv.domain.models.impl;

import br.com.i9algo.autaz.pdv.domain.models.User;
import io.realm.Realm;

/**
 * Created by aStraube on 20/09/2017.
 */

public class UserImpl {

    private User _model;

    public UserImpl (User model) {
        this._model = model;
    }

    public void setModel(User model) { this._model = model; }
    public User getModel() { return this._model; }


    public void setName(String name) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setName(name);

        _realm.commitTransaction();
    }

    public void setEmail(String email) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setEmail(email);

        _realm.commitTransaction();
    }

    /*public void setValues(Values values) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setValues(values);

        _realm.commitTransaction();
    }*/
}

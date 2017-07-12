package com.autazcloud.pdv.domain.interfaces;

import com.autazcloud.pdv.domain.models.WrapObjToNetwork;

import org.json.JSONArray;

/**
 * Created by andre on 29/07/2015.
 */
public interface Transaction {
    public WrapObjToNetwork doBefore();
    public void doAfter(JSONArray jsonArray);
}

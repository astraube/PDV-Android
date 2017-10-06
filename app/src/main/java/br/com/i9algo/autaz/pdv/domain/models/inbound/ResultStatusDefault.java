package br.com.i9algo.autaz.pdv.domain.models.inbound;


import com.google.gson.Gson;

/**
 * Created by aStraube on 30/06/2017.
 */

public class ResultStatusDefault {

    public Throwable error = null;
    public String title = null;
    public String message = null;
    public String code = null;

    public static ResultStatusDefault fromJson(String json){
        Gson gson = new Gson();
        ResultStatusDefault m = gson.fromJson(json, ResultStatusDefault.class);
        return m;
    }
}

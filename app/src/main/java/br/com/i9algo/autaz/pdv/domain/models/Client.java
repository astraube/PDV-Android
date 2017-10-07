package br.com.i9algo.autaz.pdv.domain.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import br.com.i9algo.autaz.pdv.domain.models.impl.ClientImpl;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class Client extends RealmObject {

    public static final double CURRENT_VERSION = 0.1; // Apenas para conhecimento de versao

    @Ignore private volatile String stringSearch;
    @Ignore private volatile ClientImpl modelImpl; // Implementacao da model


    @SerializedName("public_token")
    @Expose
    private String publicToken;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("name")
    @Expose
    private String name = "";

    @SerializedName("cpf_cnpj")
    @Expose
    private String cpfCnpj = "";

    @SerializedName("email")
    @Expose
    private String email = "";

    @SerializedName("phone")
    @Expose
    private String phone = "";

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;


    public Client() {
        this.modelImpl = new ClientImpl(this);
    }



    public static String getRouteKeyName()
    {
        return "publicToken";
    }

    public String getPublicToken() {
        return publicToken;
    }
    /*public void setPublicToken(String publicToken) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.publicToken = publicToken;

        _realm.commitTransaction();
    }*/

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.code = code;

        _realm.commitTransaction();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.name = name;

        _realm.commitTransaction();
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }
    public void setCpfCnpj(String cpfCnpj) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.cpfCnpj = cpfCnpj;

        _realm.commitTransaction();
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.email = email;

        _realm.commitTransaction();
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.phone = phone;

        _realm.commitTransaction();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
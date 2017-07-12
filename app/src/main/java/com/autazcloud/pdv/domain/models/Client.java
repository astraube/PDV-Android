package com.autazcloud.pdv.domain.models;

import com.autazcloud.pdv.domain.models.impl.ClientImpl;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
    private String name;

    @SerializedName("cpf_cnpj")
    @Expose
    private String cpfCnpj;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;


    public Client() {
        this.modelImpl = new ClientImpl(this);
    }

    public String getPublicToken() {
        return publicToken;
    }
    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }
    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
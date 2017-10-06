package br.com.i9algo.autaz.pdv.domain.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;

public class Corporate extends RealmObject /*implements Parcelable*/
{

    @SerializedName("public_token")
    @Expose
    private String publicToken;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("cnpj_cpf")
    @Expose
    private String cnpjCpf;

    @SerializedName("razao_social")
    @Expose
    private String razaoSocial;

    @SerializedName("nome_fantasia")
    @Expose
    private String nomeFantasia;

    @SerializedName("description")
    @Expose
    private String description;


    /*@SerializedName("values")
    @Expose
    private Values values;*/


    public Corporate() {
    }
    public Corporate(Corporate corp) {
        super();
        this.publicToken = corp.getPublicToken();
        this.createdAt = corp.getCreatedAt();
        this.updatedAt = corp.getUpdatedAt();
        this.cnpjCpf = corp.getCnpjCpf();
        this.razaoSocial = corp.getRazaoSocial();
        this.nomeFantasia = corp.getNomeFantasia();
        this.description = corp.getDescription();
    }

    public String getPublicToken() {
        return publicToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }
    public void setNomeFantasia(String nomeFantasia) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.nomeFantasia = nomeFantasia;

        _realm.commitTransaction();
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.description = description;

        _realm.commitTransaction();
    }



    /*protected Corporate(Parcel in) {
        this.publicToken = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
        this.cnpjCpf = ((String) in.readValue((String.class.getClassLoader())));
        this.razaoSocial = ((String) in.readValue((String.class.getClassLoader())));
        this.nomeFantasia = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.typeId = ((Object) in.readValue((Object.class.getClassLoader())));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(publicToken);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
        dest.writeValue(cnpjCpf);
        dest.writeValue(razaoSocial);
        dest.writeValue(nomeFantasia);
        dest.writeValue(description);
        dest.writeValue(typeId);
    }

    public final static Parcelable.Creator<Corporate> CREATOR = new Creator<Corporate>() {
        @SuppressWarnings({
                "unchecked"
        })
        @Override
        public Corporate createFromParcel(Parcel in) {
            return new Corporate(in);
        }
        @Override
        public Corporate[] newArray(int size) {
            return (new Corporate[size]);
        }
    };*/
}
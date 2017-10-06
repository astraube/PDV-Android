package br.com.i9algo.autaz.pdv.domain.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import br.com.i9algo.autaz.pdv.domain.models.impl.UserImpl;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by aStraube on 20/09/2017.
 */

public class User extends RealmObject {

    public static final double CURRENT_VERSION = 0.1; // Apenas para conhecimento da versao atual da model na API WEB

    @Ignore private volatile UserImpl modelImpl; // Implementacao da model

    //@Ignore
    //private int sessionId;

    @Since(0.1)
    @SerializedName("public_token")
    @Expose
    @PrimaryKey
    private String publicToken;

    @Since(0.1)
    @SerializedName("api_token")
    @Expose
    private String apiToken;

    @Since(0.1)
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @Since(0.1)
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @Since(0.1)
    @SerializedName("name")
    @Expose
    private String name;

    @Since(0.1)
    @SerializedName("email")
    @Expose
    private String email;

    @Since(0.1)
    @SerializedName("activated")
    @Expose
    private Integer activated;

    @Since(0.1)
    @SerializedName("account")
    @Expose
    private Account account;

    /*@Since(0.1)
    @SerializedName("values")
    @Expose
    private Values values;*/

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
        this.modelImpl = new UserImpl(this);
    }

    public User(User user) {
        super();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.publicToken = user.getPublicToken();
        this.apiToken = user.getApiToken();
        this.name = user.getName();
        this.email = user.getEmail();
        this.activated = user.getActivated();
        this.account = user.getAccount();
        //this.values = user.getValues();
    }

    public static String getRouteKeyName()
    {
        return "publicToken";
    }

    /**
     * Implementacao da model
     * @return
     */
    public UserImpl getModelImpl() {
        if (this.modelImpl == null)
            this.modelImpl = new UserImpl(this);

        return this.modelImpl;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getPublicToken() {
        return publicToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public boolean hasLongName() {
        return name.length() > 7;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getActivated() {
        return activated;
    }

    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }

    /*public Values getValues() {
        return values;
    }
    public void setValues(Values values) {
        this.values = values;
    }*/
}

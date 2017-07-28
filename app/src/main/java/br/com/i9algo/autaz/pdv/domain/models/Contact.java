package br.com.i9algo.autaz.pdv.domain.models;

import android.text.TextUtils;

import br.com.i9algo.autaz.pdv.domain.enums.ContactTypes;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Contact {

    private String userToken;
    private ContactTypes type;
    private String subject;
    private String body;
    private Date dateSend;
    private Map<String, String> metadata = null;
    
    public Contact () {
    	dateSend = new Date();
    }

	public String getUserToken() { return userToken; }
	public void setUserToken(String userToken) { this.userToken = userToken; }

	public ContactTypes getType() { return type; }
	public void setType(ContactTypes type) { this.type = type; }

	public String getSubject() { return subject; }
	public void setSubject(String subject) { this.subject = subject; }

	public String getBody() { return body; }
	public void setBody(String body) { this.body = body; }

	public Date getDateSend() { return dateSend; }
	public void setDateSend(Date dateSend) { this.dateSend = dateSend; }
    
	public Map<String, String> getMetadata() { return metadata; }
    public void addMetadata(String key, String value) {
    	if (metadata == null)
    		metadata = new HashMap<String, String>();
    	
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value))
            metadata.put(key, value);
    }
    public String getMetaJson() {
    	if (metadata == null)
    		return "";
    	
        Gson gson = new Gson();
        String json = gson.toJson(metadata);
        return json;
    }
}

package br.com.i9algo.autaz.pdv.domain.models;

import org.json.JSONException;
import org.json.JSONObject;

public class AppData {

	private String mSoftwareName;
	private String mSoftwareSetupName;
	private double mSoftwareVersion;
	private String mSoftwareVersionDate;
	
	
	public AppData (String json) {
		try {
			JSONObject jo = new JSONObject(json);
			
			mSoftwareName = jo.getString("softwareName");
			mSoftwareSetupName = jo.getString("softwareSetupName");
			mSoftwareVersion = jo.getDouble("softwareVersion");
			mSoftwareVersionDate = jo.getString("softwareVersionDate");

			//Log.v("------------------->", mSoftwareName);
			//Log.v("------------------->", mSoftwareVersionDate);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public AppData (JSONObject json) {
		try {
			
			mSoftwareName = json.getString("softwareName");
			mSoftwareSetupName = json.getString("softwareSetupName");
			mSoftwareVersion = json.getDouble("softwareVersion");
			mSoftwareVersionDate = json.getString("softwareVersionDate");
			
			//Log.v("------------------->", mSoftwareName);
			//Log.v("------------------->", mSoftwareVersionDate);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getSoftwareName() { return mSoftwareName; }
	public void setSoftwareName(String softwareName) { this.mSoftwareName = softwareName; }
	

	public String getSoftwareSetupName() { return mSoftwareSetupName; }
	public void setSoftwareSetupName(String softwareSetupName) { this.mSoftwareSetupName = softwareSetupName; }
	
	public double getSoftwareVersion() { return mSoftwareVersion; }
	public void setSoftwareVersion(double softwareVersion) { this.mSoftwareVersion = softwareVersion; }
	
	public String getSoftwareVersionDate() { return mSoftwareVersionDate; }
	public void setSoftwareVersionDate(String softwareVersionDate) { this.mSoftwareVersionDate = softwareVersionDate; }
}

package com.autazcloud.pdv.domain.models;


public class LayoutModel {

	public int mID = 0;
	public String mName = "";
	public String mDescription = "";
	public int mImage = 0;
	
	public int getId() { return mID; }
	public void setId(int id) { this.mID = id; }
	
	public String getName() { return mName; }
	public void setName(String name) { this.mName = name; }
	
	public String getDescription() { return mDescription; }
	public void setDescription(String description) { this.mDescription = description; }
	
	public int getImage() { return mImage; }
	public void setImage(int image) { this.mImage = image; }
}

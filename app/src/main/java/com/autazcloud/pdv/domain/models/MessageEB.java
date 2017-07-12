package com.autazcloud.pdv.domain.models;

import com.autazcloud.pdv.domain.enums.ActionsEnum;

/**
 * Model Message para EventBus
 * @author andre straube
 *
 */
public class MessageEB {
	
	private ActionsEnum mAction;
	private String message;
	private String classListener;
	
	
	public ActionsEnum getAction() { return mAction; }
	public void setAction(ActionsEnum action) { this.mAction = action; }
	
	public String getMessage() { return message; }
	public void setMessage(String text) { this.message = text; }
	
	public String getClassListener() { return classListener; }
	public void setClassListener(String classTester) { this.classListener = classTester; }
	public void setClassListener(Class<?> classListener) { this.classListener = classListener.getName(); }
	
}

package com.autazcloud.pdv.domain.models;

/**
 * Utilizado para requisi��es web.
 * 
 * @author andre straube
 * @version 1.0
 *
 */
public class WrapRequestToNetwork extends WrapObjToNetwork {
	
	private static final long serialVersionUID = 1L;
	private String url;
	
	public WrapRequestToNetwork(Object obj, String method, String url) {
		super(obj, method);
		this.url = url;
	}
	
	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }
}

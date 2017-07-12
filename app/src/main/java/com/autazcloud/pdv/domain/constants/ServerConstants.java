package com.autazcloud.pdv.domain.constants;

import com.autazcloud.pdv.BuildConfig;

public class ServerConstants {

	public static final String SERVICE_ENDPOINT_URL_V1 = "http://" + BuildConfig.SERVER_HOST + "/api/v1/";

	public static final String SERVER_AUTH	  			= SERVICE_ENDPOINT_URL_V1 + "/authorization";
	public static final String SERVER_APP			  	= SERVICE_ENDPOINT_URL_V1 + "/app";
	public static final String SERVER_APP_DOWNLOAD	  	= SERVER_APP  + "/app/download";
	public static final String SERVER_CONTACT 			= SERVICE_ENDPOINT_URL_V1 + "/app/contact";
	public static final String SERVER_ACCOUNT_MANAGER 	= SERVICE_ENDPOINT_URL_V1 + "/account";
	public static final String SERVER_USERS			 	= SERVICE_ENDPOINT_URL_V1 + "/users";
	public static final String SERVER_CORPORATE			= SERVICE_ENDPOINT_URL_V1 + "/corporate";
	public static final String SERVER_STOCK 			= SERVICE_ENDPOINT_URL_V1 + "/stock";
	public static final String SERVER_SALES		 		= SERVICE_ENDPOINT_URL_V1 + "/sales";
	public static final String SERVER_GEO 				= SERVICE_ENDPOINT_URL_V1 + "/geo";
}

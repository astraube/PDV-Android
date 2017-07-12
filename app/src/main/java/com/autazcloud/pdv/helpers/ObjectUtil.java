package com.autazcloud.pdv.helpers;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectUtil {
	
	public static Object cloneObject(Object obj){
        try{
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(obj));
            }
            return clone;
        }catch(Exception e){
            return null;
        }
    }
	
	public static String ObjectToJson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
		
		/*
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}*/
	}
	
	public static <T> Object JsonToObject(JSONObject jsonObject, Class<T> valueType) {
		return ObjectUtil.JsonToObject(jsonObject.toString(), valueType);
	}
	public static <T> Object JsonToObject(String jsonObject, Class<T> valueType) {
		T obj = null;
		
		Gson gson = new Gson();
		obj = gson.fromJson(jsonObject, valueType);
		
		/*
		ObjectMapper mapper = new ObjectMapper();
		try {
			obj =  mapper.readValue(jsonObject, valueType);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return obj;
	}
	
	/**
	 * Retorna uma String com os Valores de atributos
	 * @return String
	 */
	public static String ObjectToStringAttributes(Object obj) {
		StringBuilder sb = new StringBuilder();
		sb.append(obj.getClass().getName());
		sb.append(": ");
		for (Field f : obj.getClass().getDeclaredFields()) {
			sb.append(f.getName());
			sb.append("=");
			try {
				sb.append(f.get(obj));
			} catch (IllegalAccessException | IllegalArgumentException e) {
				e.printStackTrace();
			}
			sb.append(", ");
		}
		return sb.toString();
	}
	
	/**
	 * Retorna uma String com os Valores de Geters
	 * @return String
	 */
	public static String ObjectToStringGetters(Object obj) {
	    StringBuilder sb = new StringBuilder();
	    try {
	        Class<?> c = Class.forName(obj.getClass().getName());
	        Method m[] = c.getDeclaredMethods();

	        Object oo;
	        for (int i = 0; i < m.length; i++)
	            if (m[i].getName().startsWith("get")) {
	                oo = m[i].invoke(obj, null);
	                sb.append(m[i].getName().substring(3) + "=" + String.valueOf(oo));
	                sb.append(", ");
	            }
	    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
	    	e.printStackTrace();
	    }
	    return sb.toString();
	}
}

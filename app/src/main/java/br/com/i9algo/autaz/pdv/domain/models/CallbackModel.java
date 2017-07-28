package br.com.i9algo.autaz.pdv.domain.models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CallbackModel {

	private Object listener = null;
	private String methodName = null;
	
	/**
	 * .models.CallbackModel
	 * 
	 * @param l Object listener
	 * @param s Method callback string
	 */
	public CallbackModel (Object l, String s) {
		this.listener = l;
		this.methodName = s;
	}
	
	public void invoque () {
		try {
			Method method;
			method = listener.getClass().getMethod(methodName, null);
			method.invoke(listener, null);
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}

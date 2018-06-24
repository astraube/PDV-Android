package br.com.i9algo.autaz.pdv.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Deprecated
public class OnCallbackEvent {

	private Object listener = null;
	private String methodName = null;
	
	/**
	 * .models.OnCallbackEvent
	 * 
	 * @param l Object listener
	 * @param s Method callback string
	 */
	public OnCallbackEvent(Object l, String s) {
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

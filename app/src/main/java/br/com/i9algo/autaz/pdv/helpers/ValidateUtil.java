package br.com.i9algo.autaz.pdv.helpers;

import android.annotation.TargetApi;
import android.os.Build;

public class ValidateUtil {

	@TargetApi(Build.VERSION_CODES.FROYO)
	public final static boolean isEmailValid(CharSequence target) {
	    if (target == null) 
	        return false;

	    return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}
	
	@TargetApi(Build.VERSION_CODES.FROYO)
	public final static boolean isPhoneValid(CharSequence target) {
	    if (target == null) 
	        return false;

	    return android.util.Patterns.PHONE.matcher(target).matches();
	}
}

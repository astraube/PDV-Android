package com.autazcloud.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.domain.models.CallbackModel;

public class DialogUtil {
	
	private static final String TAG = "DialogUtil";
	
	public static final void showMessageDialog(final Activity activity, final int title, final int message, int icon, final CallbackModel callBack, boolean cancelable)
	{
		showMessageDialog(activity, activity.getString(title), activity.getString(message), icon, callBack, cancelable);
	}
	public static final void showMessageDialog(final Activity activity, final String title, final String message, int icon, final CallbackModel callBack, boolean cancelable)
    {
		try {
	        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	        
	        icon = (icon == 0) ? R.drawable.ic_action_about : icon;
	        
	        builder.setIcon(icon)
	        .setTitle(title)
	        .setMessage(message)
	        .setCancelable(cancelable)
	        .setOnCancelListener(
	    		new AlertDialog.OnCancelListener() {
		    			@Override
		    			public void onCancel(DialogInterface dialog) {
		    				if (callBack != null) {
		                		callBack.invoque();
							}
		                    dialog.dismiss();
		    			}
	    		})
	        .setPositiveButton(android.R.string.ok,
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	if (callBack != null) {
	                		callBack.invoque();
						}
	                    dialog.dismiss();
	                }
	            }
	        );
	        AlertDialog alert = builder.create();
	        alert.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter;
	        alert.show();
	        
		} catch (Exception ex) {
			Log.e(TAG, "showErrorDialog - erro ao criar Dialog para - " + activity.getClass().toString());
			Log.e(TAG, ex.getMessage());
		}
    }
	
	public static final void showActionDialog(final Activity activity, String title, String message, int btPositive, int btNegative, int icon, final CallbackModel callbackModel, boolean cancelable)
    {
		try {
	        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	        
	        builder.setIcon(icon)
	        .setTitle(title)
	        .setMessage(message)
	        .setCancelable(cancelable)
	        .setPositiveButton(btPositive,
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	if (callbackModel != null) {
	                		callbackModel.invoque();
						}
	                    dialog.dismiss();
	                }
	            }
	        )
	        .setNegativeButton(btNegative,
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                	dialog.cancel();
		                }
		            }
		        );
	        AlertDialog alert = builder.create();
	        alert.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter;
	        alert.show();
	        
		} catch (Exception ex) {
			Log.e(TAG, "showErrorDialog - erro ao criar Dialog para - " + activity.getClass().toString());
			Log.e(TAG, ex.getMessage());
		}
    }
	
	public static final void showActionDialog(final Activity activity, int title, int message, final CallbackModel callbackModel, boolean cancelable)
	{
		showActionDialog(activity, activity.getString(title), activity.getString(message), R.string.action_yes, R.string.action_no, R.drawable.ic_action_about, callbackModel, cancelable);
	}
	
	public static final void showActionDialog(final Activity activity, String title, String message, final CallbackModel callbackModel, boolean cancelable)
	{
		showActionDialog(activity, title, message, R.string.action_yes, R.string.action_no, R.drawable.ic_action_about, callbackModel, cancelable);
	}
	
	public static final void showErrorDialog(final Activity context, String title, String message, final CallbackModel callbackModel)
    {
		try {
	        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        
	        builder.setIcon(R.drawable.ic_action_error)
	        .setMessage(message)
	        .setTitle(title)
	        .setCancelable(true)
	        .setOnCancelListener(
	    		new AlertDialog.OnCancelListener() {
		    			@Override
		    			public void onCancel(DialogInterface dialog) {
		                    dialog.dismiss();
		    			}
	    		})
			.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							if (callbackModel != null) {
								callbackModel.invoque();
							}
							dialog.dismiss();
						}
					});
	        AlertDialog alert = builder.create();
	        alert.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter;
	        alert.show();
	        
		} catch (Exception ex) {
			Log.e(TAG, "showErrorDialog - erro ao criar Dialog para - " + context.getClass().toString());
			Log.e(TAG, ex.getMessage());
		}
    }
}

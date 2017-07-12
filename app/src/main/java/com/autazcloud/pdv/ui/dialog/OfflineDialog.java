package com.autazcloud.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.domain.interfaces.ConfirmDialogInterface;

public class OfflineDialog extends AlertDialog.Builder {

	private Activity mActivity;
	private AlertDialog mDialog;
	private ConfirmDialogInterface mOwner;

	public OfflineDialog(Activity activity, ConfirmDialogInterface owner) {
		super(activity);
		
		mOwner = owner;
		
		setIcon(R.drawable.ic_action_warning);
        setTitle("Problemas na Conexão");
        setMessage("Desculpe, mas o equipamento está sem internet!");
        setCancelable(true);
        setPositiveButton("OK",
            new DialogInterface.OnClickListener() {
				@Override
                public void onClick(DialogInterface dialog, int whichButton) {
					if (mOwner != null)
						mOwner.onPositiveAction();
					
    				mDialog.dismiss();
                }
            }
        );
        setOnCancelListener(
            new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (mOwner != null)
						mOwner.onCancelAction();
					
    				mDialog.cancel();
				}
            }
        );
        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter; 
        this.mDialog.show();
	}
}
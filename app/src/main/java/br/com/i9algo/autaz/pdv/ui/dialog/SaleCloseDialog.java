package br.com.i9algo.autaz.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.data.local.PreferencesRepository;
import br.com.i9algo.autaz.pdv.domain.constants.Constants;

public class SaleCloseDialog extends AlertDialog.Builder {
	
	private AlertDialog mDialog;
	
	public SaleCloseDialog(final Activity activity) {
		super(activity);
		
		if (PreferencesRepository.isShowDialogSaleCleanClose()) {
			activity.onBackPressed();
			activity.getIntent().putExtra(Constants.EXTRA_CANCEL_SALE, false);
			return;
		}
		
		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		final LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_layout_sale_clean_close, null);
		
		final CheckBox cbShowDialog = (CheckBox) layout.findViewById(R.id.cbShowDialog);
		
		cbShowDialog.setChecked(PreferencesRepository.isShowDialogSaleCleanClose());
		cbShowDialog.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				PreferencesRepository.setShowDialogSaleCleanClose(!isChecked);
			}
		});
		setPositiveButton(R.string.action_yes,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	activity.getIntent().putExtra(Constants.EXTRA_CANCEL_SALE, true);
                	activity.onBackPressed();
    				mDialog.dismiss();
                }
            }
        );
		setNegativeButton(R.string.action_no,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	activity.getIntent().putExtra(Constants.EXTRA_CANCEL_SALE, false);
                	activity.onBackPressed();
    				mDialog.dismiss();
                }
            }
        );
		
		setIcon(R.mipmap.ic_launcher);
        setView(layout);
        setTitle(R.string.txt_cancel_sale_title);
        setCancelable(true);
        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter;
        this.mDialog.show();
	}
}

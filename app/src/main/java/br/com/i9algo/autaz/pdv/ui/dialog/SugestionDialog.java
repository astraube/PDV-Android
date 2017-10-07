package br.com.i9algo.autaz.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.enums.ContactTypes;
import br.com.i9algo.autaz.pdv.domain.models.Contact;

public class SugestionDialog extends MaterialDialog {

	public SugestionDialog(final Activity activity) {
		super(new MaterialDialog.Builder(activity));

		this.builder.autoDismiss(false);
		this.builder.iconRes(R.drawable.ic_insert_comment_black_36dp);
		this.builder.title(R.string.dialog_suggestion_title);
		this.builder.content(R.string.dialog_suggestion_content);
		this.builder.inputRangeRes(2, 400, R.color.red);
		this.builder.inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		this.builder.input(R.string.dialog_suggestion_hint, 0, new MaterialDialog.InputCallback() {
			@Override
			public void onInput(MaterialDialog dialog, CharSequence input) {
				//Log.v("SugestionDialog", "onInput - " + input);

				if (TextUtils.isEmpty(input)) {
					new AlertDialog.Builder(activity)
							.setTitle(getContext().getString(R.string.txt_form_incomplete))
							.setMessage(getContext().getString(R.string.txt_form_fill))
							.setCancelable(false)
							.setPositiveButton(getContext().getString(R.string.action_ok), new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).create().show();
					return;
				}

				Contact c = new Contact();
				c.setSubject("Sugestao Autaz PDV");
				c.setBody(input.toString());
				c.setType(ContactTypes.SUGGESTION);
				c.setUserToken("sdfkjh786t"); // TODO inserir token do usuario
				// TODO enviar sugestao para servidor

				dialog.dismiss();
			}
		});
		this.builder.show();
	}
}
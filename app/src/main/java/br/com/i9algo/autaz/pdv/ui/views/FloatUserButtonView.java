package br.com.i9algo.autaz.pdv.ui.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.data.local.PreferencesRepository;
import br.com.i9algo.autaz.pdv.domain.constants.AuthAttr;
import br.com.i9algo.autaz.pdv.ui.dialog.FloatMenuDialog;

public class FloatUserButtonView extends RelativeLayout {

	OnClickListener mImageOnClickListener =
		new OnClickListener() {
		@Override
		public void onClick(View v) {
			FloatMenuDialog cdd = new FloatMenuDialog((Activity)getContext(), FloatUserButtonView.this);
			cdd.show();
		}
	};

	public FloatUserButtonView(Context context) {
		this(context, null);
	}
	public FloatUserButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		instanciate();
	}
	@TargetApi(21)
	public FloatUserButtonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		instanciate();
	}
	
	private void instanciate() {
		/**
		 * Imagem Perfil
		 */
		int sizeImage = 60;

		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.ALIGN_PARENT_BOTTOM);

		ImageTextView img = new ImageTextView(getContext(), sizeImage);
		img.setLayoutParams(lp);
		//img.setGravity(Gravity.TOP|Gravity.RIGHT);
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.profile_avatar_4);
		img.setImageBitmapRounded(bmp);
		img.setOnClickListener(mImageOnClickListener);
		img.setText(PreferencesRepository.getValue(AuthAttr.USER_NAME));
		img.setBackgroundTextView(R.drawable.bg_corner_black_opaque, 10, 5);
		addView(img);
	}
}

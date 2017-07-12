package com.autazcloud.pdv.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.helpers.BitmapUtils;

public class ImageTextView extends LinearLayout {
	
	private TextView mTextView;
	private ImageView mImageView;
	private int mImageSize = 50;
	
	public ImageTextView(Context context, int sizeImage) {
		super(context);
		mImageSize = sizeImage;
		instanciate(null, 0);
	}
	
	public ImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		String text = attrs.getAttributeValue(null, "text");
		int resId = attrs.getAttributeResourceValue(null, "image_resource", 0);
		mImageSize = attrs.getAttributeIntValue(null, "image_size", 0);
		instanciate(text, resId);
	}
	
	private void instanciate(String text, int resId) {
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);
		//this.setGravity(Gravity.CENTER);

		// Get the image view
		this.mImageView = new ImageView(getContext());
		LayoutParams ilp = new LayoutParams(mImageSize, mImageSize);
		//ilp.weight = 1;
		ilp.gravity = Gravity.CENTER;
		this.mImageView.setLayoutParams(ilp);

		// Create the caption view
		this.mTextView = new TextView(getContext());
		LayoutParams clp = new LayoutParams(200, LayoutParams.WRAP_CONTENT);
		//clp.weight = 0;
		clp.gravity = Gravity.CENTER_HORIZONTAL;
		clp.topMargin = 10;
		this.mTextView.setLayoutParams(clp);
		this.mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
		this.mTextView.setTextAppearance(getContext(), R.style.textProductLarge);
		this.mTextView.setSingleLine();
		
		// Add views to the linear layout
		this.addView(this.mImageView);
		this.addView(this.mTextView);
		
		if (text != null)
			this.mTextView.setText(text);
		
		if (resId != 0)
			this.mImageView.setImageResource(resId);
	}
	
	public void setImageResource(int resid) {
		this.mImageView.setImageResource(resid);
	}
	
	public void setImageDrawable(Drawable drawable) {
		this.mImageView.setImageDrawable(drawable);
	}
	
	public void setImageBitmap(Bitmap bm) {
		this.mImageView.setImageBitmap(bm);
	}
	
	public void setImageBitmapRounded(Bitmap bm) {
		setImageBitmapRounded(bm, mImageSize);
	}
	
	public void setImageBitmapRounded(Bitmap bm, int radius) {
		Bitmap bitmap = BitmapUtils.getRoundBitmap(bm, radius);
		this.mImageView.setImageBitmap(bitmap);
	}
	
	public void setText(CharSequence text) {
		this.mTextView.setText(text);
	}
	
	public void setText(int resid) {
		this.mTextView.setText(resid);
	}
	
	public void setTextColor(int color) {
		this.mTextView.setTextColor(color);
	}
	
	public void setBackgroundTextView(int resid, int horizontalPadding, int verticalPadding) {
		this.mTextView.setBackgroundResource(resid);
		this.mTextView.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
		this.mTextView.setWidth(mImageSize);
	}
}

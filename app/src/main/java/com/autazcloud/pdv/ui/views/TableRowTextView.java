package com.autazcloud.pdv.ui.views;

import android.content.Context;
import android.os.Build;
import android.widget.Space;
import android.widget.TableRow;
import android.widget.TextView;

import com.autazcloud.pdv.R;


public class TableRowTextView extends TableRow {
	
	private TextView textView1, textView2;
	
	public TableRowTextView(Context context, String text1, String text2, int sizeSpacer) {
		super(context);
		
		int horPadd = context.getResources().getDimensionPixelSize(R.dimen.horizontal_padding_textView);
		int vertPadd = context.getResources().getDimensionPixelSize(R.dimen.vertical_padding_textView);
		
		textView1 = new TextView(context);
		textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		textView1.setPadding(horPadd, vertPadd, horPadd, vertPadd);
		if (Build.VERSION.SDK_INT < 23) {
			textView1.setTextAppearance(context, R.style.textProductLarge);
		} else {
			textView1.setTextAppearance(R.style.textProductLarge);
		}
		textView1.setTextColor(context.getResources().getColor(R.color.gray));
		textView1.setText(text1);
		
		textView2 = new TextView(context);
		textView2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		textView2.setPadding(horPadd, vertPadd, horPadd, vertPadd);
		if (Build.VERSION.SDK_INT < 23) {
			textView2.setTextAppearance(context, R.style.textProductLarge);
		} else {
			textView2.setTextAppearance(R.style.textProductLarge);
		}
		textView2.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.text_size_big));
		textView2.setText(text2);
		
		Space spacer = new Space(context);
		spacer.setLayoutParams(new LayoutParams(sizeSpacer, LayoutParams.WRAP_CONTENT));
		
		this.addView(textView1);
		this.addView(spacer);
		this.addView(textView2);
	}
	
	public void setText1(String text) {
		textView1.setText(text);
	}
	
	public void setText2(String text) {
		textView2.setText(text);
	}
}

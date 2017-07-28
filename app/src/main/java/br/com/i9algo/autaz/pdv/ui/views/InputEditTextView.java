package br.com.i9algo.autaz.pdv.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class InputEditTextView extends EditText {
	
	public InputEditTextView(Context context) {
		super(context);
		init();
	}
	public InputEditTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public InputEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	private final void init() {
		final Drawable[] imgs = getCompoundDrawables();
		final Drawable x = getResources().getDrawable(android.R.drawable.ic_menu_view);
		x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
		setCompoundDrawables(imgs[0], imgs[1], x, imgs[3]);
		
		setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if (motionEvent.getX()>(v.getWidth()-v.getPaddingRight()-x.getIntrinsicWidth())){
                        ((EditText)v).setText("");
                    }
                }
                return false;
            }
        });
	}
}

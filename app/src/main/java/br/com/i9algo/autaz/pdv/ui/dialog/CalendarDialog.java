package br.com.i9algo.autaz.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import br.com.i9algo.autaz.pdv.R;

import java.util.Calendar;
import java.util.Date;


public class CalendarDialog extends AlertDialog.Builder {
	
	private AlertDialog mDialog;
	private CalendarDialogInterface mOwner;
	
	
	Button.OnClickListener mOnClickCancel = 
		new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			mDialog.dismiss();
		}
	};
	
	CalendarView.OnDateChangeListener mOnDataChangeCalendarView = 
		new CalendarView.OnDateChangeListener() {
		@Override
		public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
			//Log.i("CalendarDialog", "--> onSelectedDayChange: " + year + " - " + month + " - " + dayOfMonth);
			Calendar c = (Calendar)Calendar.getInstance().clone();

			view.setSelected(true);
			
			if (mOwner != null)
				mOwner.onChangeDate(year, month, dayOfMonth);
			
			mDialog.dismiss();
		}
	};
	
	public CalendarDialog(Activity activity, CalendarDialogInterface owner, Calendar currentDate) {
		super(activity);
		mOwner = owner;
		
		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		final LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_layout_calendar, null);

		CalendarView calendarView = ((CalendarView)layout.findViewById(R.id.calendarView));

		((Button)layout.findViewById(R.id.btnCancel)).setOnClickListener(mOnClickCancel);

		calendarView.setDate(currentDate.getTimeInMillis());
		calendarView.setOnDateChangeListener(mOnDataChangeCalendarView);
		
		setIcon(R.drawable.ic_action_go_to_today);
        setView(layout);
        setTitle(getContext().getString(R.string.title_dialog_select_date));
        setCancelable(true);
        setOnCancelListener(new AlertDialog.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mDialog.dismiss();
			}
		});
        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter; 
        this.mDialog.show();
	}
	
	public interface CalendarDialogInterface {
		public void onChangeDate(Calendar initCalendar, Calendar finalCalendar);
		public void onChangeDate(int year, int month, int dayOfMonth);
	}
}

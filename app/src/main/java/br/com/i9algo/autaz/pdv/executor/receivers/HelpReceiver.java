package br.com.i9algo.autaz.pdv.executor.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

import br.com.i9algo.autaz.pdv.ui.views.HelpButtonView;

/**
 * <p>Botao do panico</p>
 */
public class HelpReceiver extends WakefulBroadcastReceiver {
	
	public static final int BROADCAST_REQUEST_CODE = 0;
	//public static final String INIT_HELP_TIME = "initHelpTime";
	//public static final String FINAL_HELP_TIME = "finalHelpTime";
	private static long FINAL_HELP_TIME = 0;
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	// stopSelf();
    	
    	Log.v("AlarmReceiver", "onReceive - enviar informacaes ao servidor");
    	//TODO
    	
    	Calendar calendar = Calendar.getInstance();
    	//long initDate = intent.getLongExtra(INIT_HELP_TIME, 0);
    	//long finalDate = intent.getLongExtra(FINAL_HELP_TIME, 0);
    	
    	if (calendar.getTimeInMillis() >= FINAL_HELP_TIME) {
    		stopHelp(context);
    		HelpButtonView.onStop();
    	}
    }
	
	public static void startHelp(Context context) {
    	long intervalo = 1000 * 5; // Intervalo de tempo em segundos. A cada 5 segundos envia informa��o ao servidor
    	
    	Calendar initCalendar = Calendar.getInstance();
    	//initCalendar.add(Calendar.SECOND, 5);
		
    	Calendar finalCalendar = Calendar.getInstance();
    	finalCalendar.add(Calendar.HOUR, 6);

		//Log.i("HelpButton", "Start Help" + initCalendar.getTime());
		//Log.i("HelpButton", "Final Help" + finalCalendar.getTime());
		
    	FINAL_HELP_TIME = finalCalendar.getTimeInMillis();
    	
    	AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context.getApplicationContext(), HelpReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, BROADCAST_REQUEST_CODE, intent, 0);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, initCalendar.getTimeInMillis(), intervalo, pi);
	}

	public static void stopHelp(Context context) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context.getApplicationContext(), HelpReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, BROADCAST_REQUEST_CODE, intent, 0);
		
		alarmManager.cancel(pi);
		
	}
}
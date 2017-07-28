package br.com.i9algo.autaz.pdv.executor.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.com.i9algo.autaz.pdv.domain.constants.Constants;

/**
 * <p>Executado ao ligar o equipamento</p>
 */
public class BootUpReceiver extends BroadcastReceiver {

    SampleAlarmReceiver alarm = new SampleAlarmReceiver();
	   
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.i("BootUpReceiver", "Scheduling Iniciando Aplicacao");
    	// stopSelf();
        if (intent.getAction().equals(Constants.BROADCAST_ACTION))
        {
            alarm.setAlarm(context);
        }
    }
}
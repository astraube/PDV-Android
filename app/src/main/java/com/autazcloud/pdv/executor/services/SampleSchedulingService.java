package com.autazcloud.pdv.executor.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.autazcloud.pdv.ui.base.CustomApplication;
import com.autazcloud.pdv.data.remote.service.ApiService;
import com.autazcloud.pdv.executor.receivers.SampleAlarmReceiver;
import com.autazcloud.pdv.data.remote.ProductsRepository;
import com.autazcloud.pdv.data.remote.subscribers.SubscriberInterface;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SampleSchedulingService extends IntentService implements SubscriberInterface {

    public SampleSchedulingService() {
        super("SchedulingService");
    }

    public static final String TAG = "SampleSchedulingService";
    public static final String TAG2 = "Scheduling";

    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i(TAG, TAG2 + " onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, TAG2 + " onHandleIntent");
        /*
        Bundle b = intent.getExtras();
        String dataString = intent.getDataString();
        Log.i(TAG, TAG2 + " onHandleIntent: " + b.getString("TESTE"));
        Log.i(TAG, TAG2 + " onHandleIntent: " + dataString);
        */

        ProductsRepository.onLoadProducts(this);

        Log.i(TAG, TAG2 + " onHandleIntent finish");

        //sendNotification("Processo finalizado...");

        // Release the wake lock provided by the BroadcastReceiver.
        SampleAlarmReceiver.completeWakefulIntent(intent);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public ApiService getApiService() {
        return ((CustomApplication)getApplication()).getApiService();
    }

    @Override
    public void onSubscriberCompleted() {

    }

    @Override
    public void onSubscriberError(Throwable e, final String title, final String msg) {
        Log.e(TAG, e.getMessage());
        Log.e(TAG, msg);
    }

    @Override
    public void onSubscriberNext(Object t) {

    }

    // Post a notification indicating whether a doodle was found.
    /*private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, SalesGridActivity.class), 0);

        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Alerta AutaZ")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg);
        }

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }*/
}

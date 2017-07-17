package com.autazcloud.pdv.controllers.printer;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.helpers.ShowMsg;
import com.autazcloud.pdv.ui.base.BaseActivity;
import com.epson.eposprint.BatteryStatusChangeEventListener;
import com.epson.eposprint.Print;
import com.epson.eposprint.StatusChangeEventListener;
import com.epson.epsonio.DevType;
import com.epson.epsonio.DeviceInfo;
import com.epson.epsonio.EpsonIoException;
import com.epson.epsonio.FilterOption;
import com.epson.epsonio.Finder;
import com.epson.epsonio.IoStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DiscoverPrinterActivity extends BaseActivity implements OnItemClickListener, Runnable, StatusChangeEventListener, BatteryStatusChangeEventListener {

    final static int DISCOVERY_INTERVAL = 500;

    ArrayList<HashMap<String, String>> printerList = null;
    SimpleAdapter printerListAdapter = null;
    ScheduledExecutorService scheduler;
    ScheduledFuture<?> future;
    Handler handler = new Handler();
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		fullScreen();
        setContentView(R.layout.activity_discover_printer);

        Print printer = PrinterEpson.getPrinter(this);
        if(printer != null){
            printer.setStatusChangeEventCallback(this);
            printer.setBatteryStatusChangeEventCallback(this);
        }

        //init printer list control
        printerList = new ArrayList<HashMap<String, String>>();
        printerListAdapter = new SimpleAdapter(this, printerList, R.layout.printer_list_at,
                new String[] { "PrinterName", "Type" },
                new int[] { R.id.PrinterName, R.id.Type });
        ListView list = (ListView)findViewById(R.id.listView_printerlist);
        list.setAdapter(printerListAdapter);
        list.setOnItemClickListener(this);
        

        //start find thread scheduler
        scheduler = Executors.newSingleThreadScheduledExecutor();

        //find start
        findStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //stop find
        if(future != null){
            future.cancel(false);
            while(!future.isDone()){
                try{
                    Thread.sleep(DISCOVERY_INTERVAL);
                }catch(Exception e){
                    break;
               }
            }
            future = null;
        }
        if(scheduler != null){
            scheduler.shutdown();
            scheduler = null;
        }
        //stop old finder
        while(true) {
            try{
                Finder.stop();
                break;
            }catch(EpsonIoException e){
                if(e.getStatus() != IoStatus.ERR_PROCESSING){
                    break;
                }
            }
        }
    }
    
    /*private void selectDevice(String printerName, String printerAddress, int printerType) {
    	PrinterEpson.mDeviceName = printerName;
    	PrinterEpson.openPrinter(this, printerType, printerAddress, true, 1000);
    	finish();
    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	HashMap<String, String> item  = printerList.get(position);

        Log.v(getClass().getSimpleName(), "Address: " + item.get("Address"));
        Log.v(getClass().getSimpleName(), "Address: " + item.keySet().toString());
        Log.v(getClass().getSimpleName(), "Address: " + item.values().toString());
    	
    	//PrinterEpson.openPrinter(this, Print.DEVTYPE_USB, item.get("Address"), true, 1000);
    	
    	PrinterEpson.mDeviceAddress = item.get("Address");
    	PrinterEpson.mDeviceType = Print.DEVTYPE_USB;
        
        finish();
    }

    @Override
    //find thread
    public synchronized void run() {
        class UpdateListThread extends Thread{
            DeviceInfo[] list;
            public UpdateListThread(DeviceInfo[] listDevices) {
                list = listDevices;
            }

            @Override
            public void run() {
                if(list == null){
                    if(printerList.size() > 0){
                        printerList.clear();
                        printerListAdapter.notifyDataSetChanged();
                    }
                }else if(list.length != printerList.size()){
                    printerList.clear();
                    String name = null;
                    String address = null;
                    for(int i=0; i<list.length; i++){
                        name = list[i].getPrinterName();
                        address = list[i].getDeviceName();
                        
                        HashMap<String, String> item = new HashMap<String, String>();
                        item.put("PrinterName", name);
                        item.put("Address", address);
                        item.put("Type", "USB");
                        printerList.add(item);
                    }
                    printerListAdapter.notifyDataSetChanged();
                }
            }
        }

        DeviceInfo[] deviceList = null;
        try{
            deviceList = Finder.getDeviceInfoList(FilterOption.PARAM_DEFAULT);
            handler.post(new UpdateListThread(deviceList));
        }catch(Exception e){
            return;
        }
    }

    //find start/restart
    private void findStart() {
        if(scheduler == null){
            return;
        }

        //stop old finder
        while(true) {
            try{
                Finder.stop();
                break;
            }catch(EpsonIoException e){
                if(e.getStatus() != IoStatus.ERR_PROCESSING){
                    break;
                }
            }
        }

        //stop find thread
        if(future != null){
            future.cancel(false);
            while(!future.isDone()){
                try{
                    Thread.sleep(DISCOVERY_INTERVAL);
                }catch(Exception e){
                    break;
                }
            }
            future = null;
        }

        //clear list
        printerList.clear();
        printerListAdapter.notifyDataSetChanged();

        try {
			Finder.start(this, DevType.USB, null);
		} catch (EpsonIoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //start thread
        future = scheduler.scheduleWithFixedDelay(this, 0, DISCOVERY_INTERVAL, TimeUnit.MILLISECONDS);
    }

	@Override
	public void onStatusChangeEvent(final String deviceName, final int status) {
		runOnUiThread(new Runnable() {
			@Override
			public synchronized void run() {
				ShowMsg.showStatusChangeEvent(deviceName, status, DiscoverPrinterActivity.this);
			}
		});
	}

	@Override
	public void onBatteryStatusChangeEvent(final String deviceName, final int battery) {
		runOnUiThread(new Runnable() {
			@Override
			public synchronized void run() {
				ShowMsg.showBatteryStatusChangeEvent(deviceName, battery, DiscoverPrinterActivity.this);
			}
		});
	}
}

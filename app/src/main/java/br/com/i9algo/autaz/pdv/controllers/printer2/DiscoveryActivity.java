package br.com.i9algo.autaz.pdv.controllers.printer2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;
import com.github.pierry.simpletoast.SimpleToast;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.constants.Constants;

public class DiscoveryActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Context mContext = null;
    private ArrayList<HashMap<String, String>> mPrinterList = null;
    private SimpleAdapter mPrinterListAdapter = null;
    private FilterOption mFilterOption = null;


    public static Intent createIntent(Context context) {
        return new Intent(context, DiscoveryActivity.class);
    }
    public static void startActivityResult(Activity activity) {
        activity.startActivityForResult(DiscoveryActivity.createIntent(activity), Constants.REQ_COD_DISCOVERY_PRINTER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printer_2_activity_discovery);

        mContext = this;

        showDialodSeriesPrinter();

        Button button = (Button)findViewById(R.id.btnRestart);
        button.setOnClickListener(this);

        mPrinterList = new ArrayList<HashMap<String, String>>();
        mPrinterListAdapter = new SimpleAdapter(this, mPrinterList, R.layout.list_at,
                                                new String[] { "PrinterName", "Target" },
                                                new int[] { R.id.PrinterName, R.id.Target });
        ListView list = (ListView)findViewById(R.id.lstReceiveData);
        list.setAdapter(mPrinterListAdapter);
        list.setOnItemClickListener(this);

        mFilterOption = new FilterOption();
        mFilterOption.setDeviceType(Discovery.TYPE_PRINTER);
        mFilterOption.setEpsonFilter(Discovery.FILTER_NAME);
        try {
            Discovery.start(this, mFilterOption, mDiscoveryListener);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "start", mContext);
        }
    }

    private void showDialodSeriesPrinter() {
        // TODO - determina que a impressora eh o modelo TM-T20
        // TODO - existe uma forma que quando a impressora eh plugada, recupera o nome do modelo
        // VER APP - https://github.com/alt236/USB-Device-Info---Android
        PrinterEpson.PRINTER_SERIES = 6;

        /*
        if (PrinterEpson.PRINTER_SERIES < 0) {

            String[] series = PrinterEpson.getSelectSeries(this);

            new MaterialDialog.Builder(this)
                    .iconRes(R.drawable.ic_print_black_48dp)
                    .title(R.string.printer_series_select)
                    .items(series)
                    .negativeText(R.string.action_cancel)
                    .cancelable(false)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            SimpleToast.info(DiscoveryActivity.this, DiscoveryActivity.this.getString(R.string.printer_series_selected, text.toString()));
                            PrinterEpson.PRINTER_SERIES = which;
                        }
                    })
                    .show();
        }
        */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        while (true) {
            try {
                Discovery.stop();
                break;
            }
            catch (Epos2Exception e) {
                if (e.getErrorStatus() != Epos2Exception.ERR_PROCESSING) {
                    break;
                }
            }
        }

        mFilterOption = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRestart:
                restartDiscovery();
                break;

            default:
                // Do nothing
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();

        HashMap<String, String> item  = mPrinterList.get(position);
        String target = item.get(PrinterEpson.TARGET);

        // TODO - Adaptacao
        PrinterEpson.PRINTER_TARGET = target;

        intent.putExtra(PrinterEpson.TARGET, target);
        setResult(RESULT_OK, intent);

        finish();
    }

    /*
    // Essa parte do codigo deve estar na Activity para o caso de recuperar a impressora selecionada atraves de 'onActivityResult'

    DiscoveryActivity.startActivityResult(this);

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            String target = data.getStringExtra(PrinterEpson.TARGET);
			if (target != null) {
				PrinterEpson.PRINTER_TARGET = target;
			}
        }
    }*/

    private void restartDiscovery() {
        while (true) {
            try {
                Discovery.stop();
                break;
            }
            catch (Epos2Exception e) {
                if (e.getErrorStatus() != Epos2Exception.ERR_PROCESSING) {
                    ShowMsg.showException(e, "stop", mContext);
                    return;
                }
            }
        }

        mPrinterList.clear();
        mPrinterListAdapter.notifyDataSetChanged();

        try {
            Discovery.start(this, mFilterOption, mDiscoveryListener);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "stop", mContext);
        }
    }

    private DiscoveryListener mDiscoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscovery(final DeviceInfo deviceInfo) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("PrinterName", deviceInfo.getDeviceName());
                    item.put("Target", deviceInfo.getTarget());
                    mPrinterList.add(item);
                    mPrinterListAdapter.notifyDataSetChanged();
                }
            });
        }
    };


}

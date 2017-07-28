package br.com.i9algo.autaz.pdv;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import br.com.i9algo.autaz.pdv.R;

@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class PreferenceActivity extends android.preference.PreferenceActivity /*implements SharedPreferences.OnSharedPreferenceChangeListener*/ {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        //SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //mPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onStart();
    }

    @Override
    protected void onPause() {
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onPause();
    }

    /*@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.w("SettingsActivity", "onSharedPreferenceChanged: " + key);
    }*/
}
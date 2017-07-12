package com.autazcloud.pdv;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_general);
		
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		mPrefs.registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		super.finish();
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

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.w("SettingsActivity", "onSharedPreferenceChanged: " + key);
	}
}

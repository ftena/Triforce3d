package com.tarlic.triforce3d;

import com.tarlic.triforce3d.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class TriforcePreferencesActivity extends PreferenceActivity {
  @SuppressWarnings("deprecation")
@Override
  protected void onCreate(Bundle savedInstanceState) {	  
	  super.onCreate(savedInstanceState);
    
    // Load the preferences from an XML resource
    addPreferencesFromResource(R.xml.preferences);

    /*
     * MODE_PRIVATE: the default mode, where the created file can only
     * be accessed by the calling application (or all applications sharing
     * the same user ID).
     */
    final SharedPreferences sharedPreferences = this.getSharedPreferences("com.tarlic.triforce3d.prefs",
    		Context.MODE_PRIVATE);
    
    /*
     * The list of preferences (see preferences.xml) has two list of values:
     * android:entries and android:entriesValues.
     */
    ListPreference listColorPreference = (ListPreference) findPreference("prefColor");
    ListPreference listRotationPreference = (ListPreference) findPreference("prefRotation");
    
    if(listColorPreference.getValue()==null) {
        /*
         *  To ensure we don't get a null value
         *  set first value by default
         */
        listColorPreference.setValueIndex(0);
    }
    
    if(listRotationPreference.getValue()==null) {
        /*
         *  To ensure we don't get a null value
         *  set first value by default
         */
    	listRotationPreference.setValueIndex(0);
    }
    
    /* 
     * Manage the color preference.
     */
    
    // Get array of colors from the resources.
    Resources res = getResources();
    final String[] colors = res.getStringArray(R.array.color);
    
    // Get the string for that color.
    String selectedColor = colors[Integer.parseInt(listColorPreference.getValue())];
    
    // Set a new summary in the application.
    listColorPreference.setSummary(selectedColor);
    
    // Listener if the user change the color
    listColorPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {        	
            String selectedColor = colors[Integer.parseInt(newValue.toString())];
        	
            preference.setSummary(selectedColor);
            
            Editor editor = sharedPreferences.edit();
            editor.putString("color", newValue.toString());
            editor.commit();
           
            return true;
        }
    });
    
    /* 
     * Manage the rotation preference.
     */
    
    // Get array of options from the resources.
    final String[] rotation = res.getStringArray(R.array.rotation);
    
    // Get the string for that color.
    String selectedRotationOption = rotation[Integer.parseInt(listRotationPreference.getValue())];
    
    // Set a new summary in the application.
    listRotationPreference.setSummary(selectedRotationOption);
    
    // Listener if the user change the color
    listRotationPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {        	
            String selectedRotationOption = rotation[Integer.parseInt(newValue.toString())];
        	
            preference.setSummary(selectedRotationOption);
            
            Editor editor = sharedPreferences.edit();
            editor.putString("rotation", newValue.toString());
            editor.commit();
           
            return true;
        }
    });
  
  }
} 

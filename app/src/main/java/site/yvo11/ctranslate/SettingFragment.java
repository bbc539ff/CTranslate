package site.yvo11.ctranslate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yvo11 on 2018/2/24.
 */

public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        CheckBoxPreference NotiCheckboxPref = (CheckBoxPreference) getPreferenceManager()
                .findPreference("setNotification");
        NotiCheckboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());
                
                SharedPreferences.Editor editor;
                editor = getActivity().getSharedPreferences("setting", MODE_PRIVATE).edit();
                editor.putBoolean("setNotification", checked);
                editor.apply();
                CheckBoxPreference startCheckboxPref = (CheckBoxPreference) getPreferenceManager()
                        .findPreference("setStart");
                if(checked == false){
                    startCheckboxPref.setSelectable(false);
                    startCheckboxPref.setChecked(false);
                }else {
                    startCheckboxPref.setSelectable(true);
                }
                return true;
            }
        });

        CheckBoxPreference ToastCheckboxPref = (CheckBoxPreference) getPreferenceManager()
                .findPreference("setToast");
        ToastCheckboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());

                SharedPreferences.Editor editor;
                editor = getActivity().getSharedPreferences("setting", MODE_PRIVATE).edit();
                editor.putBoolean("setToast", checked);
                editor.apply();
                return true;
            }
        });

        Preference hisClearPreference = getPreferenceManager().findPreference("HisClearPreference");
        hisClearPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences.Editor editor;
                editor = getActivity().getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                Toast.makeText(getActivity().getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        Preference preference = getPreferenceManager().findPreference("version");
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            long previous = 0;
            int add = 0;
            @Override
            public boolean onPreferenceClick(Preference preference) {
                long now = System.currentTimeMillis();
                if(now - previous < 500){
                    previous = now;
                    now = System.currentTimeMillis();
                    add++;

                } else {
                    add = 0;
                }
                if(add == 5){
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("setting", MODE_PRIVATE);
                    boolean checked = sharedPreferences.getBoolean("reciteMode", false);
                    SharedPreferences.Editor editor;
                    editor = getActivity().getSharedPreferences("setting", MODE_PRIVATE).edit();
                    editor.putBoolean("reciteMode", !checked);
                    editor.apply();
                    Toast.makeText(getActivity().getApplicationContext(), !checked+"", Toast.LENGTH_SHORT).show();
                    add = 0;
                }
                previous = now;
                return true;
            }
        });


    }
}

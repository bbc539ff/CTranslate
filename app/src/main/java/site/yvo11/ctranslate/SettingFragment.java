package site.yvo11.ctranslate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

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

    }
}

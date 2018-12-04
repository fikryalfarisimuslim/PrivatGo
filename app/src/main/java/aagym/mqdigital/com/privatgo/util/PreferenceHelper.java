package aagym.mqdigital.com.privatgo.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {
    private SharedPreferences customCachedPrefs;
    private Context context;
    private int homeThold;

    public PreferenceHelper(Context context) {
        super();
        this.context = context;
        customCachedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public Context getContex() {
        return context;
    }

    public SharedPreferences getCustomPref() {
        return customCachedPrefs;
    }

    public void setUserName(String division) {
        SharedPreferences.Editor mEditor = customCachedPrefs.edit();
        mEditor.putString("UserNameFirebase", String.valueOf(division));
        mEditor.apply();
    }

    public String getUserName() {
        return (customCachedPrefs.getString("UserNameFirebase", "0"));
    }
}

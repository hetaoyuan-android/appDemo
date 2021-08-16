package com.example.test.sp;

import android.content.Context;

import java.util.Set;

public class SharedPrefsUtils {
    private static SharedPrefsUtils instances;
    private SharedPreferences ssp;
    public static SharedPrefsUtils get(Context context, String spName, int mode){
        instances=new SharedPrefsUtils(context, spName,mode);
        return instances;
    }
    public static SharedPrefsUtils get(Context context, String spName) {
        instances=get(context, spName,Context.MODE_PRIVATE);
        return instances;
    }
    public static SharedPrefsUtils get(Context context) {
        instances=get(context, null,Context.MODE_PRIVATE);
        return instances;
    }
    public SharedPrefsUtils(Context context, String spName, int mode) {
        this.ssp = SharedPreferences.get(context, spName,mode);
    }
//========================= String
    public boolean putString(String key, String value) {
        if (value == null) {
            value = "";
        }
        SharedPreferences.Editor editor = ssp.edit();
        editor.putString(key, value);
        return editor.commit();
    }
    public String getString(String key, String defaultValue) {
        return ssp.getString(key, defaultValue);
    }

//========================= long
    public boolean putLong(String key, long value) {
        SharedPreferences.Editor editor = ssp.edit();
        editor.putLong(key, value);
        return editor.commit();
    }
    public long getLong(String key,long defaultvalue) {
        return ssp.getLong(key, defaultvalue);
    }
//========================= int
    public boolean putInt(String key, int value) {
    SharedPreferences.Editor editor = ssp.edit();
    editor.putInt(key, value);
    return editor.commit();
}
    public long getInt(String key,int defaultvalue) {
        return ssp.getInt(key, defaultvalue);
    }
//========================= boolean
    public boolean putBoolean(String key, boolean value) {
    SharedPreferences.Editor editor = ssp.edit();
    editor.putBoolean(key, value);
    return editor.commit();
}
    public boolean getBoolean(String key, boolean defaultvalue) {
        return ssp.getBoolean(key, defaultvalue);
    }
//========================= Float
    public boolean putFloat(String key, float value) {
    SharedPreferences.Editor editor = ssp.edit();
    editor.putFloat(key, value);
    return editor.commit();
}
    public float getFloat(String key, float defaultvalue) {
        return ssp.getFloat(key, defaultvalue);
    }
//========================= StringSet
    public boolean putStringSet(String key, Set<String> value) {
    SharedPreferences.Editor editor = ssp.edit();
    editor.putStringSet(key, value);
    return editor.commit();
}
    public Set<String> getStringSet(String key, Set<String> defaultvalue) {
        return ssp.getStringSet(key, defaultvalue);
    }

}

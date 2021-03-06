package com.example.test.sp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自动加密SharedPreference
 */
public class SharedPreferences implements android.content.SharedPreferences {

    private android.content.SharedPreferences mSharedPreferences;
    private static final String TAG = SharedPreferences.class.getName();
    private Context mContext;

    public static SharedPreferences get(Context context, String name, int mode){
        return new SharedPreferences( context, name,  mode);
    }
    public static SharedPreferences get(Context context, String name){
        return  get(context,name,Context.MODE_PRIVATE);
    }
    public static SharedPreferences get(Context context){
        return  get(context,null,Context.MODE_PRIVATE);
    }

    /**
     * constructor
     * @param context should be ApplicationContext not activity
     * @param name file name
     * @param mode context mode
     */
    public SharedPreferences(Context context, String name, int mode){
        mContext = context;
        if (TextUtils.isEmpty(name)){
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            mSharedPreferences =  context.getSharedPreferences(name, mode);
        }
    }

    @Override
    public Map<String, String> getAll() {
        final Map<String, ?> encryptMap = mSharedPreferences.getAll();
        final Map<String, String> decryptMap = new HashMap<>();
        for (Map.Entry<String, ?> entry : encryptMap.entrySet()){
            Object cipherText = entry.getValue();
            if (cipherText != null){
                decryptMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return decryptMap;
    }

    /**
     * encrypt function
     * @return cipherText base64
     */
    private String encryptPreference(String plainText){
        return EncryptUtil.getInstance(mContext).encrypt(plainText);
    }

    /**
     * decrypt function
     * @return plainText
     */
    private String decryptPreference(String cipherText){
        return EncryptUtil.getInstance(mContext).decrypt(cipherText);
    }

    @Override
    public String getString(String key, String defValue) {
        final String encryptValue = mSharedPreferences.getString(encryptPreference(key), null);
        return encryptValue == null ? defValue : decryptPreference(encryptValue);
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        final Set<String> encryptSet = mSharedPreferences.getStringSet(encryptPreference(key), null);
        if (encryptSet == null){
            return defValues;
        }
        final Set<String> decryptSet = new HashSet<>();
        for (String encryptValue : encryptSet){
            decryptSet.add(decryptPreference(encryptValue));
        }
        return decryptSet;
    }

    @Override
    public int getInt(String key, int defValue) {
        final String encryptValue = mSharedPreferences.getString(encryptPreference(key), null);
        if (encryptValue == null) {
            return defValue;
        }
        return Integer.parseInt(decryptPreference(encryptValue));
    }

    @Override
    public long getLong(String key, long defValue) {
        final String encryptValue = mSharedPreferences.getString(encryptPreference(key), null);
        if (encryptValue == null) {
            return defValue;
        }
        return Long.parseLong(decryptPreference(encryptValue));
    }

    @Override
    public float getFloat(String key, float defValue) {
        final String encryptValue = mSharedPreferences.getString(encryptPreference(key), null);
        if (encryptValue == null) {
            return defValue;
        }
        return Float.parseFloat(decryptPreference(encryptValue));
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        final String encryptValue = mSharedPreferences.getString(encryptPreference(key), null);
        if (encryptValue == null) {
            return defValue;
        }
        return Boolean.parseBoolean(decryptPreference(encryptValue));
    }

    @Override
    public boolean contains(String key) {
        return mSharedPreferences.contains(encryptPreference(key));
    }

    @Override
    public Editor edit() {
        return new Editor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    /**
     * 处理加密过渡
     */
    public void handleTransition(){
        Map<String, ?> oldMap = mSharedPreferences.getAll();
        Map<String, String> newMap = new HashMap<>();
        for (Map.Entry<String, ?> entry : oldMap.entrySet()){
            Log.i(TAG, "key:"+entry.getKey()+", value:"+ entry.getValue());
            newMap.put(encryptPreference(entry.getKey()), encryptPreference(entry.getValue().toString()));
        }
        android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear().commit();
        for (Map.Entry<String, String> entry : newMap.entrySet()){
            editor.putString(entry.getKey(), entry.getValue());
        }
        editor.commit();
    }

    /**
     * 自动加密Editor
     */
    public final class Editor implements android.content.SharedPreferences.Editor {

        private android.content.SharedPreferences.Editor mEditor;

        /**
         * constructor
         */
        private Editor(){
            mEditor = mSharedPreferences.edit();
        }

        @Override
        public android.content.SharedPreferences.Editor putString(String key, String value) {
            mEditor.putString(encryptPreference(key), encryptPreference(value));
            return this;
        }

        @Override
        public android.content.SharedPreferences.Editor putStringSet(String key, Set<String> values) {
            final Set<String> encryptSet = new HashSet<>();
            for (String value : values){
                encryptSet.add(encryptPreference(value));
            }
            mEditor.putStringSet(encryptPreference(key), encryptSet);
            return this;
        }

        @Override
        public android.content.SharedPreferences.Editor putInt(String key, int value) {
            mEditor.putString(encryptPreference(key), encryptPreference(Integer.toString(value)));
            return this;
        }

        @Override
        public android.content.SharedPreferences.Editor putLong(String key, long value) {
            mEditor.putString(encryptPreference(key), encryptPreference(Long.toString(value)));
            return this;
        }

        @Override
        public android.content.SharedPreferences.Editor putFloat(String key, float value) {
            mEditor.putString(encryptPreference(key), encryptPreference(Float.toString(value)));
            return this;
        }

        @Override
        public android.content.SharedPreferences.Editor putBoolean(String key, boolean value) {
            mEditor.putString(encryptPreference(key), encryptPreference(Boolean.toString(value)));
            return this;
        }

        @Override
        public android.content.SharedPreferences.Editor remove(String key) {
            mEditor.remove(encryptPreference(key));
            return this;
        }

        /**
         * Mark in the editor to remove all values from the preferences.
         * @return this
         */
        @Override
        public android.content.SharedPreferences.Editor clear() {
            mEditor.clear();
            return this;
        }

        /**
         * 提交数据到本地
         * @return Boolean 判断是否提交成功
         */
        @Override
        public boolean commit() {

            return mEditor.commit();
        }

        /**
         * Unlike commit(), which writes its preferences out to persistent storage synchronously,
         * apply() commits its changes to the in-memory SharedPreferences immediately but starts
         * an asynchronous commit to disk and you won't be notified of any failures.
         */
        @Override
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        public void apply() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                mEditor.apply();
            } else {
                commit();
            }
        }
    }
}

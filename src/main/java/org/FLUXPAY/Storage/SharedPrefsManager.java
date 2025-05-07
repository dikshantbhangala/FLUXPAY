package main.java.org.FLUXPAY.Storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {
    private static final String PREF_NAME = "FluxPayPrefs";
    private static final String KEY_USER_DATA = "user_data";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    
    private static SharedPrefsManager instance;
    private final SharedPreferences sharedPreferences;
    
    private SharedPrefsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public static synchronized SharedPrefsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsManager(context.getApplicationContext());
        }
        return instance;
    }
    
    public void saveUserData(String userData) {
        sharedPreferences.edit().putString(KEY_USER_DATA, userData).apply();
    }
    
    public String getUserData() {
        return sharedPreferences.getString(KEY_USER_DATA, null);
    }
    
    public void clearUserData() {
        sharedPreferences.edit().remove(KEY_USER_DATA).apply();
    }
    
    public void saveAuthToken(String token) {
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, token).apply();
    }
    
    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }
    
    public void clearAuthToken() {
        sharedPreferences.edit().remove(KEY_AUTH_TOKEN).apply();
    }
    
    public boolean isLoggedIn() {
        return getAuthToken() != null;
    }
}
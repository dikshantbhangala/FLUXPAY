package main.java.org.FLUXPAY.Networkservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    private static Context applicationContext;
    
    public static void init(Context context) {
        applicationContext = context.getApplicationContext();
    }
    
    public static boolean isNetworkAvailable() {
        if (applicationContext == null) {
            return false;
        }
        
        ConnectivityManager connectivityManager = (ConnectivityManager) 
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            
        if (connectivityManager == null) {
            return false;
        }
        
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
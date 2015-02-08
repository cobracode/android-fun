package ned.androidfun;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class IConnectivityManager {
    private static final String TAG = "IConnectivityManager";
    private ConnectivityManager cm = null;
    private Object iConnectivityManager = null;
    private Method setRadios = null;

    public IConnectivityManager(final Context context) {
        Log.v(TAG, "IConnectivityManager() begin");
        cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            // Get ConnectivityManager.mService
            final Field serviceField = cm.getClass().getDeclaredField("mService");
            serviceField.setAccessible(true);
            Log.v(TAG, "IConnectivityManager(): serviceField: " + serviceField);

            // Get object of whichever class mService is
            iConnectivityManager = serviceField.get(cm);
            Log.v(TAG, "IConnectivityManager(): iConnectivityManager: " + iConnectivityManager);

            // Set method
            setRadios = iConnectivityManager.getClass().getDeclaredMethod("setRadios", Boolean.TYPE);
            setRadios.setAccessible(true);
            Log.v(TAG, "IConnectivityManager(): setRadios: " + setRadios + "; is accessible? " + setRadios.isAccessible());
        }
        catch (final Exception e) {
            Log.e(TAG, "IConnectivityManager(): Error setting up cell radio controller: " + e);
            e.printStackTrace();
            iConnectivityManager = null;
            setRadios = null;
        }
        Log.v(TAG, "IConnectivityManager() end");
    }

    public void disableCell() {
        Log.v(TAG, "disableCell() begin");
        try {
            setRadios.invoke(iConnectivityManager, false);
            Logger.log(TAG, "disableCell(): radios disabled");
        }
        catch (final Exception e) {
            Log.e(TAG, "disableCell(): " + e);
            e.printStackTrace();
        }
        Log.v(TAG, "disableCell() end");
    }

    public void turnOnRadios() {
        Log.v(TAG, "turnOnRadios() begin");
        try {
            setRadios.invoke(iConnectivityManager, true);
            Logger.log(TAG, "turnOnRadios(): radios turned on");
        }
        catch (final Exception e) {
            Log.e(TAG, "turnOnRadios(): " + e);
            e.printStackTrace();
        }
        Log.v(TAG, "turnOnRadios() end");
    }

    public void printNetworkInfo() {
        Log.v(TAG, "printNetworkInfo() begin");
        for (final NetworkInfo info : cm.getAllNetworkInfo()) {
            Log.v(TAG, "printNetworkInfo(): network info: " + info.toString());
        }
        Log.v(TAG, "printNetworkInfo() end");
    }

    public void disableNetworks() {
        Log.v(TAG, "disableNetworks() begin");
        try {
            setRadios.invoke(iConnectivityManager, false);
        }
        catch (final Exception e) {
            Log.e(TAG, "disableNetworks(): Error setting airplane mode: " + e);
            e.printStackTrace();
        }
        Logger.log(TAG, "disableNetworks() end");
    }
}
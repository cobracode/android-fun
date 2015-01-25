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
        Logger.log(TAG, "IConnectivityManager() begin");
        cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            // Get ConnectivityManager.mService
            final Field serviceField = cm.getClass().getDeclaredField("mService");
            serviceField.setAccessible(true);
            Logger.log(TAG, "IConnectivityManager(): serviceField: " + serviceField);

            // Get object of whichever class mService is
            iConnectivityManager = serviceField.get(cm);
            Logger.log(TAG, "IConnectivityManager(): iConnectivityManager: " + iConnectivityManager);

            // Set method
            setRadios = iConnectivityManager.getClass().getDeclaredMethod("setRadios", Boolean.TYPE);
            setRadios.setAccessible(true);
            Logger.log(TAG, "IConnectivityManager(): setRadios: " + setRadios + "; is accessible? " + setRadios.isAccessible());
        }
        catch (final Exception e) {
            Log.e(TAG, "IConnectivityManager(): Error setting up cell radio controller: " + e);
            e.printStackTrace();
            iConnectivityManager = null;
            setRadios = null;
        }
        Logger.log(TAG, "IConnectivityManager() end");
    }

    public void disableCell() {
        Logger.log(TAG, "disableCell() begin");
        try {
            setRadios.invoke(iConnectivityManager, false);
            Logger.log(TAG, "disableCell(): radios disabled");
        }
        catch (final Exception e) {
            Log.e(TAG, "disableCell(): " + e);
            e.printStackTrace();
        }
        Logger.log(TAG, "disableCell() end");
    }

    public void turnOnRadios() {
        Logger.log(TAG, "turnOnRadios() begin");
        try {
            setRadios.invoke(iConnectivityManager, true);
            Logger.log(TAG, "turnOnRadios(): radios turned on");
        }
        catch (final Exception e) {
            Log.e(TAG, "turnOnRadios(): " + e);
            e.printStackTrace();
        }
        Logger.log(TAG, "turnOnRadios() end");
    }

    public void printNetworkInfo() {
        Logger.log(TAG, "printNetworkInfo() begin");
        for (final NetworkInfo info : cm.getAllNetworkInfo()) {
            Logger.log(TAG, "printNetworkInfo(): network info: " + info.toString());
        }
        Logger.log(TAG, "printNetworkInfo() end");
    }

    public void disableNetworks() {
        Logger.log(TAG, "disableNetworks() begin");
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
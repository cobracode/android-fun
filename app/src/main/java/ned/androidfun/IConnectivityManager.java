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
    private Method setRadio = null;
    private boolean cellRadioOn = false;

    public IConnectivityManager(final Context context) {
        final String method = "IConnectivityManager(): ";
        cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            // Get ConnectivityManager.mService
            final Field serviceField = cm.getClass().getDeclaredField("mService");
            serviceField.setAccessible(true);
            Log.v(TAG, method + "serviceField: " + serviceField);

            // Get object of whichever class mService is
            iConnectivityManager = serviceField.get(cm);
            Log.v(TAG, method + "iConnectivityManager: " + iConnectivityManager);

            //AndroidSource.printMethods(iConnectivityManager.getClass().getName());

            // Set method
            setRadios = iConnectivityManager.getClass().getDeclaredMethod("setRadios", Boolean.TYPE);
            setRadios.setAccessible(true);
            Log.v(TAG, method + "setRadios: " + setRadios + "; is accessible? " + setRadios.isAccessible());

            setRadio = iConnectivityManager.getClass().getDeclaredMethod("setRadio", Integer.TYPE, Boolean.TYPE);
            setRadio.setAccessible(true);
            Log.v(TAG, method + "setRadio: " + setRadio + "; is accessible? " + setRadio.isAccessible());
        } catch (final Exception e) {
            Log.e(TAG, method + "Error setting up cell radio controller: " + e);
            e.printStackTrace();
            iConnectivityManager = null;
            setRadios = null;
        }
    }

    public boolean isCellRadioOn() {
        final NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        cellRadioOn = (null != mobile && mobile.isAvailable());
        Log.v(TAG, "isCellRadioOn(): " + cellRadioOn);

        return cellRadioOn;
    }

    public void disableCell() {
        Log.v(TAG, "disableCell() begin");
        try {
            setRadio.invoke(iConnectivityManager, ConnectivityManager.TYPE_MOBILE, false);
            cellRadioOn = false;
            Logger.printSay("Cell disabled");
        }
        catch (final Exception e) {
            Log.e(TAG, "disableCell(): " + e);
            e.printStackTrace();
        }
        Log.v(TAG, "disableCell() end");
    }

    public void enableCell() {
        Log.v(TAG, "enableCell() begin");
        try {
            setRadio.invoke(iConnectivityManager, ConnectivityManager.TYPE_MOBILE, true);
            cellRadioOn = true;
            Logger.printSay("Cell enabled");
        }
        catch (final Exception e) {
            Log.e(TAG, "enableCell(): " + e);
            e.printStackTrace();
        }
        Log.v(TAG, "enableCell() end");
    }

    public void disableRadios() {
        Log.v(TAG, "disableRadios() begin");
        try {
            setRadios.invoke(iConnectivityManager, false);
            cellRadioOn = false;
            Logger.log(TAG, "disableRadios(): radios disabled");
        }
        catch (final Exception e) {
            Log.e(TAG, "disableRadios(): " + e);
            e.printStackTrace();
        }
        Log.v(TAG, "disableRadios() end");
    }

    public void enableRadios() {
        Log.v(TAG, "enableRadios() begin");
        try {
            setRadios.invoke(iConnectivityManager, true);
            cellRadioOn = true;
            Logger.log(TAG, "enableRadios(): radios turned on");
        }
        catch (final Exception e) {
            Log.e(TAG, "enableRadios(): " + e);
            e.printStackTrace();
        }
        Log.v(TAG, "enableRadios() end");
    }

    public void printNetworkInfo() {
        Log.v(TAG, "printNetworkInfo() begin");
        for (final NetworkInfo info : cm.getAllNetworkInfo()) {
            Log.v(TAG, "printNetworkInfo(): network info: " + info.toString());
        }
        Log.v(TAG, "printNetworkInfo() end");
    }
}
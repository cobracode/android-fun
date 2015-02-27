package ned.androidfun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class IConnectivityManager extends BroadcastReceiver {
    private static final String TAG = "IConnectivityManager";
    private ConnectivityManager cm = null;
    private Object iConnectivityManager = null;
    private Method setRadios = null;
    private Method setRadio = null;
    private boolean cellRadioOn = false;
    private boolean internetAvailable = false;

    public IConnectivityManager() {}

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

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.v(TAG, "onReceive(): -");
        Util.printAllBundleExtras(intent.getExtras());

        cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final int networkType = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, ConnectivityManager.TYPE_DUMMY);
        final String networkName = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO);
        //final boolean noConnection = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);

        Logger.log(TAG, "onReceive(): connected? " + isConnected() +
                "; type: " + networkTypeFromInt(networkType) + "; name: " + networkName);

        // Check for internet
        if (isConnected()) {
            new InternetChecker().execute();
        }
    }

    public boolean isConnected() {
        final NetworkInfo network = cm.getActiveNetworkInfo();

        if (null != network && network.isConnected()) {
            Log.d(TAG, "isConnected(): true");
            return true;
        } else {
            Log.d(TAG, "isConnected(): false");
            return false;
        }
    }

    private class InternetChecker extends AsyncTask<Void, Void, Boolean> {
        private static final String TAG = "InternetChecker";

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result = false;

            if (isConnected()) {
                final String address = Network.SITE_HELLO;
                try {
                    final HttpURLConnection connection = (HttpURLConnection)new URL(address).openConnection();
                    connection.setConnectTimeout(2000);

                    Log.d(TAG, "doInBackground(): Beginning check for internet");
                    connection.getContent();

                    // If we've made it here, it works
                    result = true;
                } catch (final MalformedURLException e) {
                    Log.e(TAG, "doInBackground(): Malformed URL");
                } catch (final IOException e) {
                    Log.w(TAG, "doInBackground(): " + e);
                } catch (final Exception e) {
                    Log.e(TAG, "doInBackground(): " + e);
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            internetAvailable = result;
            Log.i(TAG, "onPostExecute(): Internet available? " + result);
        }
    }

    public boolean isCellRadioOn() {
        final NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        cellRadioOn = (null != mobile && mobile.isAvailable());
        Log.d(TAG, "isCellRadioOn(): " + cellRadioOn);

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

    private String networkTypeFromInt(final int type) {
        String result = "UNKNOWN TYPE";

        switch (type) {
            case ConnectivityManager.TYPE_BLUETOOTH:
                result = "bluetooth";
                break;
            case ConnectivityManager.TYPE_ETHERNET:
                result = "ethernet";
                break;
            case ConnectivityManager.TYPE_MOBILE:
                result = "mobile";
                break;
            case ConnectivityManager.TYPE_MOBILE_DUN:
                result = "mobile dun";
                break;
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
                result = "mobile hipri";
                break;
            case ConnectivityManager.TYPE_MOBILE_MMS:
                result = "mobile mms";
                break;
            case ConnectivityManager.TYPE_MOBILE_SUPL:
                result = "mobile supl";
                break;
            case ConnectivityManager.TYPE_VPN:
                result = "vpn";
                break;
            case ConnectivityManager.TYPE_WIFI:
                result = "wifi";
                break;
            case ConnectivityManager.TYPE_WIMAX:
                result = "wimax";
                break;
            case ConnectivityManager.TYPE_DUMMY:
                result = "dummy";
                break;
        }

        return result;
    }
}
package ned.androidfun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class IConnectivityManager extends BroadcastReceiver {
    // Core
    private static final String TAG = "IConnectivityManager";
    private static ConnectivityManager cm = null;
    private static InternetListener appInternetListener = null;

    // State
    private static boolean cellRadioOn = false;
    private static boolean isConnected = false;
    private static boolean internetAvailable = false;
    private static InternetChecker internetChecker = new InternetChecker();

    // For IConnectivityManager specifically
    private static Object iConnectivityManager = null;
    private static Method setRadios = null;
    private static Method setRadio = null;

//    // Required for receiver specified in manifest XML
//    public IConnectivityManager() {
//        Log.v(TAG, "<Default Constructor> -");
//    }
//
//    public IConnectivityManager(final Context context) {
//        Log.v(TAG, "IConnectivityManager(): begin");
//        updateState(context);
//        initializeRadioMethods();
//        Log.v(TAG, "IConnectivityManager(): end");
//    }

    public static void initializeContext(final Context context) {
        // Internet Listener can only be cast from Context here;
        // the Context sent to onReceive is a restricted version. Will cause exception
        appInternetListener = (InternetListener)context;

        updateState(context);
        initializeRadioMethods();
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        updateState(context, intent);
    }

    public static boolean isCellRadioOn() {
        return cellRadioOn;
    }

    public static boolean isConnected() {
        return isConnected;
    }

    public static boolean internetAvailable() {
        return internetAvailable;
    }

    private static void initializeRadioMethods() {
        final String method = "IConnectivityManager(): ";

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
            setRadio = null;
            setRadios = null;
        }
    }

    private static class InternetChecker extends AsyncTask<Void, Void, Boolean> {
        private static final String TAG = "InternetChecker";

        @Override
        protected Boolean doInBackground(final Void... params) {
            boolean result = false;

            try {
                final HttpURLConnection connection = (HttpURLConnection)new URL(Network.SITE_HELLO).openConnection();
                connection.setConnectTimeout(2000);

                Log.d(TAG, "doInBackground(): Checking for internet");
                connection.getContent();

                // If we've made it here, it works
                result = true;
            } catch (final MalformedURLException e) {
                Log.e(TAG, "doInBackground(): Malformed URL: " + e);
            } catch (final ConnectException e) {
                Log.d(TAG, "doInBackground(): Couldn't connect: " + e);
            } catch (final IOException e) {
                Log.d(TAG, "doInBackground(): " + e);
            } catch (final Exception e) {
                Log.e(TAG, "doInBackground(): " + e);
            }

            return result;
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            if (result) {
                internetOn();
            } else {
                internetOff();
            }
        }
    }

    private static void internetOn() {
        internetAvailable = true;
        Log.i(TAG, "Internet Connected");

        // Inform application
        appInternetListener.internetOn();
    }


    private static void internetOff() {
        internetAvailable = false;
        Log.i(TAG, "Disconnected from internet");

        // Inform application
        appInternetListener.internetOff();
    }

    private static void updateState(final Context context) {
        cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        cellRadioOn = updateIsCellRadioOn();
        isConnected = updateIsConnected();

        if (isConnected) {
            // Only check if internet checker not already running
            if (!internetChecker.getStatus().equals(AsyncTask.Status.RUNNING)) {
                // Check for internet
                // Need to create a new InternetChecker as tasks can only be run once
                internetChecker = new InternetChecker();
                internetChecker.execute();
            }
        } else {
            internetOff();
        }

        Log.d(TAG, "updateState(): cell radio on? " + cellRadioOn +
            "; is connected? " + isConnected + "; internet? " + internetAvailable);
    }

    private void updateState(final Context context, final Intent intent) {
        updateState(context);

        Util.printAllBundleExtras(intent.getExtras());

        final int networkType = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, ConnectivityManager.TYPE_DUMMY);
        final String networkName = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO);
        //final boolean noConnection = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);
    }

    private static boolean updateIsCellRadioOn() {
        final NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (null != mobile && mobile.isAvailable());
    }

    private static boolean updateIsConnected() {
        boolean result = false;
        final NetworkInfo network = cm.getActiveNetworkInfo();

        if (null != network && network.isConnected()) {
            result = true;
        }

        return result;
    }

    public static void disableCell() {
        Log.v(TAG, "disableCell() begin");
        try {
            setRadio.invoke(iConnectivityManager, ConnectivityManager.TYPE_MOBILE, false);
            cellRadioOn = false;
            Logger.printSay("Cell disabled");
        } catch (final NullPointerException e) {
            Log.w(TAG, "disableCell(): radio controller(s) are null: " + e);
        } catch (final Exception e) {
            Log.e(TAG, "disableCell(): " + e);
            e.printStackTrace();
        }
        Log.v(TAG, "disableCell() end");
    }

    public static void enableCell() {
        Log.v(TAG, "enableCell() begin");
        try {
            setRadio.invoke(iConnectivityManager, ConnectivityManager.TYPE_MOBILE, true);
            cellRadioOn = true;
            Logger.printSay("Cell enabled");
        } catch (final NullPointerException e) {
            Log.w(TAG, "enableCell(): radio controller(s) are null: " + e);
        }
        catch (final Exception e) {
            Log.e(TAG, "enableCell(): " + e);
            e.printStackTrace();
        }
        Log.v(TAG, "enableCell() end");
    }

    public static void disableRadios() {
        Log.v(TAG, "disableRadios() begin");
        try {
            setRadios.invoke(iConnectivityManager, false);
            cellRadioOn = false;
            Logger.log(TAG, "disableRadios(): radios disabled");
        } catch (final NullPointerException e) {
            Log.w(TAG, "disableRadios(): radio controller(s) are null: " + e);
        }
        catch (final Exception e) {
            Log.e(TAG, "disableRadios(): " + e);
            e.printStackTrace();
        }
        Log.v(TAG, "disableRadios() end");
    }

    public static void enableRadios() {
        Log.v(TAG, "enableRadios() begin");
        try {
            setRadios.invoke(iConnectivityManager, true);
            cellRadioOn = true;
            Logger.log(TAG, "enableRadios(): radios turned on");
        } catch (final NullPointerException e) {
            Log.w(TAG, "enableRadios(): radio controller(s) are null: " + e);
        }
        catch (final Exception e) {
            Log.e(TAG, "enableRadios(): " + e);
            e.printStackTrace();
        }
        Log.v(TAG, "enableRadios() end");
    }

    public static void printNetworkInfo() {
        Log.v(TAG, "printNetworkInfo() begin");
        for (final NetworkInfo info : cm.getAllNetworkInfo()) {
            Log.v(TAG, "printNetworkInfo(): network info: " + info.toString());
        }
        Log.v(TAG, "printNetworkInfo() end");
    }

    private static String networkTypeFromInt(final int type) {
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
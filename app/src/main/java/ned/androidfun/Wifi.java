package ned.androidfun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

public class Wifi extends BroadcastReceiver {
    private static final String TAG = "Wifi";
    private WifiManager manager = null;
    private Context context = null;

    private boolean isEnabled = false;
    private boolean isConnected = false;
    private int state = WifiManager.WIFI_STATE_UNKNOWN;
    private static String connectedWifiNetwork = "unknown wifi network";
    private static final String STRING_NETWORK_DISCONNECTED = "no-wifi-connection";

    public Wifi () {}

    Wifi(final Context context) {
        Log.v(TAG, "Wifi() begin");
        updateState(context);
        Log.v(TAG, "Wifi() end");
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.v(TAG, "onReceive() begin");

        updateState(context);

        switch (intent.getAction()) {
            case WifiManager.ACTION_PICK_WIFI_NETWORK:
                wifiNetworkPicked(intent);
                break;
            case WifiManager.NETWORK_IDS_CHANGED_ACTION:
                networkIDsChanged(intent);
                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                networkStateChanged(intent);
                break;
            case WifiManager.RSSI_CHANGED_ACTION:
                signalStrengthChanged(intent);
                break;
            case WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION:
                supplicantConnectionStateChanged(intent);
                break;
            case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION:
                supplicantStateChanged(intent);
                break;
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                wifiStateChanged(intent);
                break;
            default:
                Log.w(TAG, "onReceive(): Unexpected intent received: " + intent.getAction());
        }

        Log.v(TAG, "onReceive() end");
    }

    public boolean disconnect() {
        Log.v(TAG, "disconnect() -");
        return manager.disconnect();
    }

    public String getIP() {
        return ipIntToString(manager.getConnectionInfo().getIpAddress());
    }

    public String getState() {
        return getWifiState(manager.getWifiState());
    }

    public void displayWifiInfo() {
        // Wifi State
        Logger.log(TAG, "displayWifiInfo(): Wifi state: " + getWifiState(manager.getWifiState()));

        // Current connection
        final WifiInfo currentConnection = manager.getConnectionInfo();

        if (-200 < currentConnection.getRssi()) {
            Logger.log(TAG, "displayWifiInfo(): Connected to " + currentConnection.toString());
        }
        else {
            Logger.log(TAG, "displayWifiInfo(): not connected to any wifi networks");
        }

        Logger.log(TAG, "displayWifiInfo(): DHCP: " + manager.getDhcpInfo().toString());
    }


    private void networkIDsChanged(final Intent intent) {
        Log.v(TAG, "networkIDsChanged() begin");
        Util.printAllBundleExtras(intent.getExtras());
        Log.v(TAG, "networkIDsChanged() end");
    }

    private void networkStateChanged(final Intent intent) {
        Log.v(TAG, "networkStateChanged() begin");
        Util.printAllBundleExtras(intent.getExtras());

        final NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

        switch (info.getState()) {
            case CONNECTED:
                connectedWifiNetwork = info.getExtraInfo();
                Logger.printSay("Connected to wifi network " + connectedWifiNetwork);
                sendHello();
                break;
            case DISCONNECTED:
                // Prevent saying this twice, as each disconnection generates 2 of these intents
                if (!connectedWifiNetwork.equals(STRING_NETWORK_DISCONNECTED)) {
                    Logger.printSay("Disconnected from wifi network " + connectedWifiNetwork);
                    connectedWifiNetwork = STRING_NETWORK_DISCONNECTED;
                }
                break;
        }

        if (info.getDetailedState().equals(NetworkInfo.DetailedState.FAILED)) {
            Logger.printSay("Authentication for wifi network failed for " + info.getExtraInfo());
        }

        Log.v(TAG, "networkStateChanged() end");
    }

    private void sendHello() {
        RequestQueue requests = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(
                "http://www.scienceofspirituality.info/files/hello",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Logger.printSay("Received Volley response");
                        Logger.log(TAG, "sendHello()::onResponse():: response = " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError volleyError) {
                        Logger.printSay("Error in request: " + volleyError);
                    }
                });

        requests.add(request);
    }

    private void wifiNetworkPicked(final Intent intent) {
        Log.v(TAG, "wifiNetworkPicked() begin");
        Util.printAllBundleExtras(intent.getExtras());
        Log.v(TAG, "wifiNetworkPicked() end");
    }

    private void signalStrengthChanged(final Intent intent) {
        Log.v(TAG, "signalStrengthChanged() begin");
        Util.printAllBundleExtras(intent.getExtras());
        Log.v(TAG, "signalStrengthChanged() end");
    }

    private void wifiStateChanged(final Intent intent) {
        Log.v(TAG, "wifiStateChanged() begin");
        Util.printAllBundleExtras(intent.getExtras());

        Parrot.say(getWifiState(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)));

        Log.v(TAG, "wifiStateChanged() end");
    }

    private void supplicantConnectionStateChanged(final Intent intent) {
        Log.v(TAG, "supplicantConnectionStateChanged() begin");
        Util.printAllBundleExtras(intent.getExtras());
        Log.v(TAG, "supplicantConnectionStateChanged() end");
    }

    private void supplicantStateChanged(final Intent intent) {
        Log.v(TAG, "supplicantStateChanged() begin");
        Util.printAllBundleExtras(intent.getExtras());

//        if (SupplicantState.DISCONNECTED == intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE)) {
//            Parrot.say("Disconnected from wifi network " + connectedWifiNetwork);
//            connectedWifiNetwork = "";
//        }

        Log.v(TAG, "supplicantStateChanged() end");
    }


    private static String ipIntToString(final int ip) {
        String ipString = "";
        int mutableIP = ip;

        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            mutableIP = Integer.reverseBytes(ip);
        }

        try {
            ipString = InetAddress.getByAddress(BigInteger.valueOf(mutableIP).toByteArray()).getHostAddress();
        }
        catch (final UnknownHostException uhe) {
            Log.w(TAG, "ipIntToString(): Unable to get host IP from IP int: " + uhe);
        }

        return ipString;
    }

    private void updateState(final Context context) {
        Log.v(TAG, "updateState() begin");

        // Update manager and other info
        manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        this.context = context;

        isEnabled = manager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
        Log.v(TAG, "updateState(): isEnabled = " + isEnabled);

        if (isEnabled) {
            isConnected = manager.getConnectionInfo().getSupplicantState().equals(SupplicantState.COMPLETED);
            Log.v(TAG, "updateState(): isConnected = " + isConnected);

            Log.v(TAG, "updateState(): SSID = " + manager.getConnectionInfo().getSSID());

//            if (isConnected) {
//                connectedWifiNetwork = manager.getConnectionInfo().getSSID();
//                Log.v(TAG, "updateState(): connectedWifiNetwork = " + connectedWifiNetwork);
//            }
//            else {
//                connectedWifiNetwork = STRING_NETWORK_DISCONNECTED;
//            }
        }
        else {
            isConnected = false;
            connectedWifiNetwork = STRING_NETWORK_DISCONNECTED;
        }

        Log.v(TAG, "updateState() end");
    }

    private String getWifiState(final int state) {
        String stringState = "UNKNOWN STATE";

        switch (state) {
            case WifiManager.WIFI_STATE_DISABLING:
                stringState = "disabling wifi";
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                stringState = "wifi disabled";
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                stringState = "wifi enabled";
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                stringState = "enabling wifi";
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                stringState = "unknown wifi state";
                break;
            default:
                break;
        }

        return stringState;
    }
}

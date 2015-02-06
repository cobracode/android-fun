package ned.androidfun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

public class Wifi extends BroadcastReceiver {
    private static final String TAG = "Wifi";
    private WifiManager manager = null;
    private boolean isEnabled = false;
    private boolean isConnected = false;
    private int state = WifiManager.WIFI_STATE_UNKNOWN;

    public Wifi () {}

    Wifi(final Context context) {
        Log.v(TAG, "Wifi() begin");
        manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        updateState();

        Log.v(TAG, "Wifi() end");
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.v(TAG, "onReceive() begin");

        switch (intent.getAction()) {
            case WifiManager.NETWORK_IDS_CHANGED_ACTION:
                networkIDsChanged(intent);
                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                networkStateChanged(intent);
                break;
            case WifiManager.ACTION_PICK_WIFI_NETWORK:
                wifiNetworkPicked(intent);
                break;
            case WifiManager.RSSI_CHANGED_ACTION:
                signalStrenthChanged(intent);
                break;
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                wifiStateChanged(intent);
                break;
            case WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION:
                supplicantConnectionStateChanged(intent);
                break;
            case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION:
                supplicantStateChanged(intent);
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
;
        for (final String key : intent.getExtras().keySet()) {
            Logger.log(TAG, "networkIDsChanged(): \"" + key + "\" = \"" + intent.getExtras().get(key).toString() + "\"");
        }

        Log.v(TAG, "networkIDsChanged() end");
    }

    private void networkStateChanged(final Intent intent) {
        Log.v(TAG, "networkStateChanged() begin");

        for (final String key : intent.getExtras().keySet()) {
            Logger.log(TAG, "networkStateChanged(): \"" + key + "\" = \"" + intent.getExtras().get(key).toString() + "\"");
        }

        Log.v(TAG, "networkStateChanged() end");
    }

    private void wifiNetworkPicked(final Intent intent) {
        Log.v(TAG, "wifiNetworkPicked() begin");

        for (final String key : intent.getExtras().keySet()) {
            Logger.log(TAG, "wifiNetworkPicked(): \"" + key + "\" = \"" + intent.getExtras().get(key).toString() + "\"");
        }

        Log.v(TAG, "wifiNetworkPicked() end");
    }

    private void signalStrenthChanged(final Intent intent) {
        Log.v(TAG, "signalStrenthChanged() begin");

        for (final String key : intent.getExtras().keySet()) {
            Logger.log(TAG, "signalStrenthChanged(): \"" + key + "\" = \"" + intent.getExtras().get(key).toString() + "\"");
        }


        Log.v(TAG, "signalStrenthChanged() end");
    }

    private void wifiStateChanged(final Intent intent) {
        Log.v(TAG, "wifiStateChanged() begin");

        for (final String key : intent.getExtras().keySet()) {
            Logger.log(TAG, "wifiStateChanged(): \"" + key + "\" = \"" + intent.getExtras().get(key).toString() + "\"");
        }

        Log.v(TAG, "wifiStateChanged() end");
    }

    private void supplicantConnectionStateChanged(final Intent intent) {
        Log.v(TAG, "supplicantConnectionStateChanged() begin");

        for (final String key : intent.getExtras().keySet()) {
            Logger.log(TAG, "supplicantConnectionStateChanged(): \"" + key + "\" = \"" + intent.getExtras().get(key).toString() + "\"");
        }

        Log.v(TAG, "supplicantConnectionStateChanged() end");
    }

    private void supplicantStateChanged(final Intent intent) {
        Log.v(TAG, "supplicantStateChanged() begin");

        for (final String key : intent.getExtras().keySet()) {
            Logger.log(TAG, "supplicantStateChanged(): \"" + key + "\" = \"" + intent.getExtras().get(key).toString() + "\"");
        }

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

    private void updateState() {
        Log.v(TAG, "updateState() begin");

        if (null != manager) {
            isEnabled = (manager.getWifiState() == WifiManager.WIFI_STATE_ENABLED);
            isConnected = (manager.getConnectionInfo().getRssi() > -200);
        }
        else {
            Log.w(TAG, "updateState(): manager is NULL");
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

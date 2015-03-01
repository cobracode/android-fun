package ned.androidfun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class Wifi extends BroadcastReceiver {
    // Core
    private static final String TAG = "Wifi";
    private WifiManager manager = null;
    private Context context = null;

    // State
    private boolean isEnabled = false;
    private boolean isConnected = false;
    private int state = WifiManager.WIFI_STATE_UNKNOWN;

    // Constants
    private static String connectedWifiNetwork = "unknown wifi network";
    private static final String STRING_NETWORK_DISCONNECTED = "no-wifi-connection";

    // Required for receiver in manifest XML
    public Wifi () {
        Log.v(TAG, "<Default Constructor> -");
    }

    Wifi(final Context context) {
        Log.v(TAG, "Wifi() -");
        updateState(context);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
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
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void enable() {
        Log.i(TAG, "Enabling wifi");
        manager.setWifiEnabled(true);
        isEnabled = true;
    }

    public void disable() {
        Log.i(TAG, "Disabling wifi");
        manager.setWifiEnabled(false);
        isEnabled = false;
    }

    public boolean disconnect() {
        Log.d(TAG, "disconnect(): disconnecting");
        return manager.disconnect();
    }

    public String getIP() {
        return Util.ipIntToString(manager.getConnectionInfo().getIpAddress());
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
                final String updateConnectedWifi = "Connected to wifi network " + connectedWifiNetwork;
                Network.getHello();

                Logger.printSay(updateConnectedWifi);
                Network.sendToSite(updateConnectedWifi);
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

    private void wifiNetworkPicked(final Intent intent) {
        Log.v(TAG, "wifiNetworkPicked() begin");
        Util.printAllBundleExtras(intent.getExtras());
        Log.v(TAG, "wifiNetworkPicked() end");
    }

    private void signalStrengthChanged(final Intent intent) {
        Util.printAllBundleExtras(intent.getExtras());
        Log.d(TAG, "signalStrengthChanged(): " + intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -200));
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
        Log.v(TAG, "supplicantStateChanged() end");
    }

    private void updateState(final Context context) {
        Log.v(TAG, "updateState(): Updating wifi state");

        // Update manager and other info
        manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        this.context = context;

        isEnabled = manager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;

        if (isEnabled) {
            isConnected = manager.getConnectionInfo().getSupplicantState().equals(SupplicantState.COMPLETED);
        }
        else {
            isConnected = false;
            connectedWifiNetwork = STRING_NETWORK_DISCONNECTED;
        }

        Log.d(TAG, "updateState(): enabled? " + isEnabled + "; connected? " + isConnected + "; network: " + connectedWifiNetwork);
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
                Log.w(TAG, "getWifiState(): Unknown state: " + state);
                break;
        }

        return stringState;
    }
}

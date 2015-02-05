package ned.androidfun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.List;

public class Wifi extends BroadcastReceiver {
    private static final String TAG = "Wifi";
    private WifiManager manager = null;
    private boolean isEnabled = false;
    private boolean isConnected = false;

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

        final int newState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);

        String ip = "";

        if (WifiManager.WIFI_STATE_ENABLED == newState) {
            ip = getIP();
        }
        Parrot.say(getWifiState(newState) + " ip address is " + ip);

        Log.v(TAG, "onReceive() end");
    }

    public boolean disconnect() {
        Log.v(TAG, "disconnect() -");
        return manager.disconnect();
    }

    public String getIP() {
        if (isConnected) {
            return ipIntToString(manager.getConnectionInfo().getIpAddress());
        }
        else {
            return "N/A";
        }
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
            isEnabled = (manager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) ? true : false;
            isConnected = (manager.getConnectionInfo().getRssi() > -200) ? true : false;
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

    private void test(final Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !(connectionInfo.getSSID().equals(""))) {
                //if (connectionInfo != null && !StringUtil.isBlank(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
            // Get WiFi status MARAKANA
            WifiInfo info = wifiManager.getConnectionInfo();
            String textStatus = "";
            textStatus += "\n\nWiFi Status: " + info.toString();
            String BSSID = info.getBSSID();
            String MAC = info.getMacAddress();

            List<ScanResult> results = wifiManager.getScanResults();
            ScanResult bestSignal = null;
            int count = 1;
            String etWifiList = "";
            for (ScanResult result : results) {
                etWifiList += count++ + ". " + result.SSID + " : " + result.level + "\n" +
                        result.BSSID + "\n" + result.capabilities +"\n" +
                        "\n=======================\n";
            }
            Logger.log(TAG, "from SO: \n"+etWifiList);

            // List stored networks
            List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration config : configs) {
                textStatus+= "\n\n" + config.toString();
            }
            Logger.log(TAG,"from marakana: \n"+textStatus);
        }

        Logger.log(TAG, "test(): " + ssid);
    }
}

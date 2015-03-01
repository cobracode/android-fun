package ned.androidfun;


import android.os.Bundle;
import android.util.Log;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class Util {
    private static final String TAG = "Util";

    public static final void printAllBundleExtras(final Bundle bundle) {
        for (final String key : bundle.keySet()) {
            Log.v(TAG, "printAllBundleExtras(): \"" + key + "\" = \"" + bundle.get(key).toString() + "\"");
        }
    }

    public static final String ipIntToString(final int ip) {
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

    public static final String getTimeMillis() {
        final Long seconds = System.currentTimeMillis() / 1000;
        return seconds.toString();
    }

    public static final String getTimestamp() {
        //Calendar c = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat(); //called without pattern
        return new SimpleDateFormat().format(Calendar.getInstance().getTime());
    }
}

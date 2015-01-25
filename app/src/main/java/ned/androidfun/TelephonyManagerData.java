package ned.androidfun;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

class TelephonyManagerData {
    private static final String TAG = "TelephonyManagerData";

    private String deviceID;
    private String softwareVersion;
    private String groupLevel1ID;
    private String line1Number;
    private String mmsUAProfileURL;
    private String mmsUA;
    private String networkCountryISO;
    private String networkOperator;
    private String networkOpName;
    private String simCountryISO;
    private String simOperator;
    private String simOpName;
    private String simSerialNum;
    private String subscriberID;
    private String voicemailAlphaTag;
    private String voicemailNum;

    public TelephonyManagerData(final Intent initialIntent) {
        Log.v(TAG, "CellData() begin");
        update(initialIntent);
        Log.v(TAG, "CellData() end");
    }

    public void update(final Intent intent) {
        Log.v(TAG, "update() begin");
        //deviceID = intent.getStringExtra(TelephonyManager.E)
        Log.v(TAG, "update() end");
    }

    public String getTelephonyManagerData(final Context context) {
        Log.v(TAG, "getTelephonyManagerData() begin");
        String result = "";

        TelephonyManager phone = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            result = "Decive ID: " + phone.getDeviceId() + "\n";
            result += "Software version: " + phone.getDeviceSoftwareVersion() + "\n";
            result += "Group ID Level 1: " + phone.getGroupIdLevel1() + "\n";
            result += "Line 1 #: " + phone.getLine1Number() + "\n";
            result += "MMS UA profile URL: " + phone.getMmsUAProfUrl() + "\n";
            result += "MMS user agent: " + phone.getMmsUserAgent() + "\n";
            result += "Network Country ISO: " + phone.getNetworkCountryIso() + "\n";
            result += "Network Operator: " + phone.getNetworkOperator() + "\n";
            result += "Network Op Name: " + phone.getNetworkOperatorName() + "\n";
            result += "SIM Country ISO: " + phone.getSimCountryIso() + "\n";
            result += "SIM Operator: " + phone.getSimOperator() + "\n";
            result += "SIM Op Name: " + phone.getSimOperatorName() + "\n";
            result += "SIM Serial #: " + phone.getSimSerialNumber() + "\n";
            result += "Subscriber ID: " + phone.getSubscriberId() + "\n";
            result += "Voicemail Alpha Tag: " + phone.getVoiceMailAlphaTag() + "\n";
            result += "Voicemail #: " + phone.getVoiceMailNumber() + "\n";
            result += "Has ICC Card? " + (phone.hasIccCard() ? "yes" : "no") + "\n";
            result += "Network is roaming? " + (phone.isNetworkRoaming() ? "yes" : "no") + "\n";
        }
        catch (final Exception e) {
            Log.e(TAG, "Exception polling TelephonyManager: " + e.toString());
        }

        Log.v(TAG, "getTelephonyManagerData() end");
        return result;
    }
}
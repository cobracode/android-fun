package ned.androidfun;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.widget.TextView;

import java.util.List;

class PhoneListener extends PhoneStateListener {
    private static final String TAG = "PhoneListener";
    private TextView text = null;
    private Context context = null;
    private static String from = "unknown contact";

    static final int listenSettings =
            PhoneStateListener.LISTEN_SIGNAL_STRENGTHS |
                    PhoneStateListener.LISTEN_SERVICE_STATE |
                    PhoneStateListener.LISTEN_CALL_STATE |
                    PhoneStateListener.LISTEN_CELL_INFO |
                    PhoneStateListener.LISTEN_CELL_LOCATION |
                    PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                    PhoneStateListener.LISTEN_DATA_ACTIVITY;

    public PhoneListener(final Context inContext, final TextView inText) {
        context = inContext;
        text = inText;
    }

    @Override
    public void onCallStateChanged(final int state, final String incomingNumber) {
        String callState = "";

        from = incomingNumber.isEmpty()? from : Contacts.getContactName(context, incomingNumber);
        Logger.log(TAG, "onCallStateChanged(): incoming #: " + from);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                callState = "idle";
                Parrot.say("phone is idle");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                callState = "dialing, on a call, or on hold";
                Parrot.say("On a call with " + from);
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                callState = "ringing";
                Parrot.say(from + " is calling");
                break;
            default:
                callState = "unknown";
                break;
        }

        Logger.log(TAG, "onCallStateChanged(): state: " + callState + "; from: " + from);
    }

    @Override
    public void onCellInfoChanged(final List<CellInfo> info) {
//            if (null != info) {
//                for (final CellInfo infoItem : info) {
//                    log(TAG, "onCellInfoChanged(): ---- " + infoItem.toString());
//                }
//            }
//            else {
//                log(TAG, "onCellInfoChanged() ---- null");
//            }
    }

    @Override
    public void onCellLocationChanged(final CellLocation location) {
        if (location instanceof CdmaCellLocation) {
            CdmaCellLocation CDMALoc = (CdmaCellLocation)location;

            try {
                Logger.log(TAG, "onCellLocationChanged(): CDMA base station ID: " + CDMALoc.getBaseStationId() + "; lat: " +
                        CdmaCellLocation.convertQuartSecToDecDegrees(CDMALoc.getBaseStationLatitude()) + "; lon: " +
                        CdmaCellLocation.convertQuartSecToDecDegrees(CDMALoc.getBaseStationLongitude()) + "; network ID: " +
                        CDMALoc.getNetworkId() + "; system ID: " + CDMALoc.getSystemId());
                Parrot.say(Integer.toString(CDMALoc.getBaseStationId()));
            }
            catch (final Exception e) {
                //Log.e(TAG, "Error printing location: " + e);
            }
        }
        else if (location instanceof GsmCellLocation) {
            Logger.log(TAG, "onCellLocationChanged(): GSM");
        }
        else {
            Logger.log(TAG, "onCellLocationChanged(): don't know what type of location this is");
        }
    }

    @Override
    public void onDataActivity(final int direction) {
        String directionString = "";

        switch (direction) {
            case TelephonyManager.DATA_ACTIVITY_DORMANT:
                directionString = "active connection; physical link down";
                break;
            case TelephonyManager.DATA_ACTIVITY_IN:
                directionString = "receiving IP PPP traffic";
                break;
            case TelephonyManager.DATA_ACTIVITY_INOUT:
                directionString = "sending/receiving IP PPP traffic";
                break;
            case TelephonyManager.DATA_ACTIVITY_NONE:
                directionString = "no traffic";
                break;
            case TelephonyManager.DATA_ACTIVITY_OUT:
                directionString = "sending IP PPP traffic";
                break;
            default:
                directionString = "unknown";
                break;
        }

        Logger.log(TAG, "onDataActivity(): " + directionString);
    }

    @Override
    public void onServiceStateChanged(final ServiceState state) {
        //Logger.log(TAG, "onServiceStateChanged(): state: " + state);
    }

    @Override
    public void onSignalStrengthsChanged(final SignalStrength strength) {
        //Logger.log(TAG, "onSignalStrengthsChanged(): " + strength);
    }
}
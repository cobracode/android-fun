//package ned.androidfun;
//
//import android.support.v7.app.ActionBarActivity;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//
//
//public class MainActivity extends ActionBarActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}


package ned.androidfun;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class MainActivity extends ActionBarActivity {

    private class CellData {
        private static final String TAG = "CellData";

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

        public CellData(final Intent initialIntent) {
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

    // Gets Android-private methods
    private class AndroidSource {
        private static final String TAG = "AndroidSource";

        public boolean executeMethod(final String className, final String methodName, final Object object, final Class<?>... params) {
            Log.v(TAG, "excuteMethod() begin");
            boolean result = false;

            if (doesMethodExist(className, methodName, params)) {
                try {
                    // Get Method
                    final Method m = Class.forName(className).getDeclaredMethod(methodName, params);

                    // Invoke class method with parameters
                    m.invoke(object, params);
                }
                catch (final Exception e) {
                    Log.e(TAG, "executeMethod(): error executing method: " + e);
                }
            }

            Log.v(TAG, "executeMethod() end; result = " + result);
            return result;
        }

        public boolean doesMethodExist(final String className, final String methodName, final Class<?>... params) {
            Log.v(TAG, "doesMethodExist(): begin");
            boolean result = true;

            try {
                Class.forName(className).getDeclaredMethod(methodName, params);
            }
            catch (final Exception e) {
                Log.e(TAG, "doesMethodExist(): method " + className + "." + methodName + " doesn't exist.");
                result = false;
            }

            Log.v(TAG, "doesMethodExist(): end");
            return result;
        }

        public boolean doesFieldExist(final String className, final String fieldName) {
            Log.v(TAG, "doesFieldExist(): begin");
            boolean result = true;

            try {
                Class.forName(className).getDeclaredField(fieldName);
            }
            catch (final Exception e) {
                Log.e(TAG, "doesFieldExist(): field " + className + "." + fieldName + " doesn't exist.");
                result = false;
            }

            Log.v(TAG, "doesFieldExist(): end");
            return result;
        }

        public void printMethods(final String className) {
            Log.v(TAG, "printMethods() begin");
            try {
                for (final Method m : Class.forName(className).getDeclaredMethods()) {
                    Log.v(TAG, m.toString());
                }
            }
            catch (final Exception e) {
                Log.e(TAG, "printMethods(): " + e);
            }
            Log.v(TAG, "printMethods() end");
        }

        public void printFields(final String className) {
            Log.v(TAG, "printFields() begin");
            try {
                for (final Field m : Class.forName(className).getDeclaredFields()) {
                    Log.v(TAG, "printFields(): " + m.toString());
                }
            }
            catch (final Exception e) {
                Log.e(TAG, "printFields(): " + e);
            }
            Log.v(TAG, "printFields() end");
        }
    }

    // GUI
    private TextView text = null;
    private Button buttonToggleListen = null;

    // Data
    private static final String TAG = "Connections";
    private boolean listening = true;
    private TelephonyManager phone = null;
    private PhoneListener phoneListener = null;
    private IConnectivityManager connectivityManager = null;
    private SmsManager sms = SmsManager.getDefault();
    private SmsListener smsListener = null;
    private Parrot parrot = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.v(TAG, "onCreate() begin; listening = " + listening);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapXMLIDs();
        Logger.setText(text);

        parrot = new Parrot(this);
        initCellListener();
        connectivityManager = new IConnectivityManager(this);

        smsListener = new SmsListener();

        //final String message = "the tree of life. it happens that many people in the western world are thinking about this on this day for reasons that have built up over millenia. but this tree of life is within us all no matter belief or geographic location or culture. our sacred biology that has components and layers that modern science has yet to come close to comprehending. expanding the feeling and idea to a larger role throughout the year and in a more universal tone";
        //sms.sendMultipartTextMessage("9259630843", null, sms.divideMessage(message), null, null);

        Log.v(TAG, "onCreate() end; listening = " + listening);
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart() begin; listening = " + listening);
        super.onStart();
        Log.v(TAG, "onStart() end");
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume() begin; listening = " + listening);
        super.onResume();
        //registerReceiver(smsListener, IntentFilter);
        Log.v(TAG, "onResume() end");
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause() begin");
        super.onPause();
        Log.v(TAG, "onPause() end; listening = " + listening);
    }

    @Override
    public void onStop() {
        Log.v(TAG, "onStop() begin; listening = " + listening);
        super.onStop();
        Log.v(TAG, "onStop() end");
    }

    @Override
    public void onRestart() {
        Log.v(TAG, "onRestart() begin; listening = " + listening);
        super.onRestart();
        Log.v(TAG, "onRestart() end");
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy() begin; listening = " + listening);
        parrot.close();
        super.onDestroy();
        Log.v(TAG, "onDestroy() end");
    }

    //-------------------------------------------------------------

    private void mapXMLIDs() {
        Log.v(TAG, "mapXMLIDs() begin");
        text = (TextView)findViewById(R.id.edit_text);
        buttonToggleListen = (Button)findViewById(R.id.button_toggle_listen);
        Log.v(TAG, "mapXMLIDs() end");
    }

    private void initCellListener() {
        Log.v(TAG, "initCellListener() begin");
        phone = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        phoneListener = new PhoneListener(this, text);
        toggleListen(null);
        Log.v(TAG, "initCellListener() end");
    }

    public final void clearText(final View button) {
        Log.v(TAG, "clearText() begin");
        text.setText("");
        Logger.resetCount();
        Log.v(TAG, "clearText() end");
    }

    public final void toggleListen(final View button) {
        Log.v(TAG, "toggleListen() begin; listening = " + listening);
        if (listening) {
            phone.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
            listening = false;
            buttonToggleListen.setText("Start Listening");
        }
        else {
            phone.listen(phoneListener, PhoneListener.listenSettings);
            listening = true;
            buttonToggleListen.setText("Stop Listening");
        }
        Log.v(TAG, "toggleListen() end; listening = " + listening);
    }

    public final void disableCell(final View view) {
        Log.v(TAG, "disableCell() begin");
        connectivityManager.disableCell();
        Log.v(TAG, "disableCell() end");
    }

    public final void turnOnRadios(final View view) {
        Log.v(TAG, "turnOnRadios begin");
        connectivityManager.turnOnRadios();
        Log.v(TAG, "turnOnRadios end");
    }
}

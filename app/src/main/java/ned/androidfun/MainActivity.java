package ned.androidfun;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    // GUI
    private TextView text = null;
    private Button buttonToggleListen = null;
    private Button buttonToggleCellRadio = null;
    private Button buttonToggleWifiRadio = null;

    // Managers
    private IConnectivityManager connectivityManager = null;
    private PhoneListener phoneListener = null;
    private SensedEnvironment sensedEnvironment = null;
    private SmsListener smsListener = new SmsListener();;
    private SmsManager sms = SmsManager.getDefault();
    private TelephonyManager phone = null;
    private Parrot parrot = null;
    private Wifi wifi = null;

    // Data
    private static final String TAG = "Connections";
    private boolean listening = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.v(TAG, "onCreate() begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapXMLIDs();
        Logger.setText(text);

        initializeWithContext();
        initializeRadioButtons();
        initCellListener();

        //final String message = "the tree of life. it happens that many people in the western " +
        // "world are thinking about this on this day for reasons that have built up over " +
        // "millenia. but this tree of life is within us all no matter belief or geographic " +
        // location or culture. our sacred biology that has components and layers that modern " +
        // science has yet to come close to comprehending. expanding the feeling and idea to a " +
        // larger role throughout the year and in a more universal tone";
        //sms.sendMultipartTextMessage("ENTER # HERE", null, sms.divideMessage(message), null, null);

        Log.v(TAG, "onCreate() end");
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart() begin");
        super.onStart();
        Log.v(TAG, "onStart() end");
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume() begin");
        super.onResume();
        //registerReceiver(smsListener, IntentFilter);
        //sensedEnvironment.registerListeners();
        Log.v(TAG, "onResume() end");
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause() begin");
        //sensedEnvironment.unregisterListeners();
        super.onPause();
        Log.v(TAG, "onPause() end");
    }

    @Override
    public void onStop() {
        Log.v(TAG, "onStop() begin");
        Network.cancelRequests();
        super.onStop();
        Log.v(TAG, "onStop() end");
    }

    @Override
    public void onRestart() {
        Log.v(TAG, "onRestart() begin");
        super.onRestart();
        Log.v(TAG, "onRestart() end");
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy() begin");
        parrot.close();
        super.onDestroy();
        Log.v(TAG, "onDestroy() end");
    }

    //-------------------------------------------------------------

    private void mapXMLIDs() {
        Log.v(TAG, "mapXMLIDs() begin");
        text = (TextView)findViewById(R.id.edit_text);
        buttonToggleListen = (Button)findViewById(R.id.button_toggle_listen);
        buttonToggleCellRadio = (Button)findViewById(R.id.button_toggle_cell_radio);
        buttonToggleWifiRadio = (Button)findViewById(R.id.button_toggle_wifi_radio);
        Log.v(TAG, "mapXMLIDs() end");
    }

    private void initializeWithContext() {
        parrot = new Parrot(this);
        Network.initialize(this);
        wifi = new Wifi(this);
        connectivityManager = new IConnectivityManager(this);
        sensedEnvironment = new SensedEnvironment(this, 50);
    }

    private void initializeRadioButtons() {
        // Get current status of radios and
        // set button labels
        buttonToggleCellRadio.setText(connectivityManager.isCellRadioOn() ? "Disable Cell" : "Enable Cell");
        buttonToggleWifiRadio.setText(wifi.isEnabled() ? "Disable Wifi" : "Enable Wifi");
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

    public final void toggleCellRadio(final View button) {
        Log.v(TAG, "toggleCellRadio() begin");

        if (connectivityManager.isCellRadioOn()) {
            connectivityManager.disableCell();
            buttonToggleCellRadio.setText("Enable Cell");
        } else {
            connectivityManager.enableCell();
            buttonToggleCellRadio.setText("Disable Cell");
        }

        Log.v(TAG, "toggleCellRadio() end");
    }

    public final void toggleWifiRadio(final View button) {
        Log.v(TAG, "toggleWifiRadio() begin");

        if (wifi.isEnabled()) {
            wifi.disable();
            buttonToggleWifiRadio.setText("Enable Wifi");
        } else {
            wifi.enable();
            buttonToggleWifiRadio.setText("Disable Wifi");
        }

        Log.v(TAG, "toggleWifiRadio() end");
    }

    public final void disableCell(final View view) {
        Log.v(TAG, "disableCell() begin");
        connectivityManager.disableCell();
        Log.v(TAG, "disableCell() end");
    }

    public final void enableCell(final View view) {
        Log.v(TAG, "enableCell begin");
        connectivityManager.enableCell();
        Log.v(TAG, "enableCell end");
    }

    public final void disableRadios(final View view) {
        Log.v(TAG, "disableRadios() begin");
        connectivityManager.disableRadios();
        Log.v(TAG, "disableRadios() end");
    }

    public final void enableRadios(final View view) {
        Log.v(TAG, "enableRadios begin");
        connectivityManager.enableRadios();
        Log.v(TAG, "enableRadios end");
    }
}

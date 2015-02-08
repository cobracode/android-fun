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

    // Data
    private static final String TAG = "Connections";
    private boolean listening = true;
    private TelephonyManager phone = null;
    private PhoneListener phoneListener = null;
    private IConnectivityManager connectivityManager = null;
    private SmsManager sms = SmsManager.getDefault();
    private SmsListener smsListener = null;
    private Parrot parrot = null;
    private SensedEnvironment sensedEnvironment = null;
    private Wifi wifi = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.v(TAG, "onCreate() begin; listening = " + listening);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapXMLIDs();
        Logger.setText(text);

        wifi = new Wifi(this);
        parrot = new Parrot(this);
        initCellListener();
        connectivityManager = new IConnectivityManager(this);

        smsListener = new SmsListener();
        sensedEnvironment = new SensedEnvironment(this, 50);


        //final String message = "the tree of life. it happens that many people in the western world are thinking about this on this day for reasons that have built up over millenia. but this tree of life is within us all no matter belief or geographic location or culture. our sacred biology that has components and layers that modern science has yet to come close to comprehending. expanding the feeling and idea to a larger role throughout the year and in a more universal tone";
        //sms.sendMultipartTextMessage("ENTER # HERE", null, sms.divideMessage(message), null, null);

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
        //sensedEnvironment.registerListeners();
        Log.v(TAG, "onResume() end");
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause() begin");
        //sensedEnvironment.unregisterListeners();
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

        //wifi.displayWifiInfo();
        Logger.log(TAG, "WIFI IP: " + wifi.getIP() + "; state: " + wifi.getState());

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

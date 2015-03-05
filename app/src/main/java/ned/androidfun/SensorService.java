package ned.androidfun;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class SensorService extends IntentService {
    private static final String TAG = "SensorService";
    private static SensedEnvironment sensedEnvironment = null;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * 'name' may be used to name the worker thread, important only for debugging.
     */
    public SensorService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.d(TAG, "onHandleIntent() begin");

        sensedEnvironment = new SensedEnvironment(this, 1);

        final TimerTask getSensorData = new TimerTask() {
            @Override
            public void run() {
                final long startTime = System.currentTimeMillis();
                final long runTime = 300;
                final long endTime = startTime + runTime;

                Log.d(TAG, "run(): Activating environment sensors");
                sensedEnvironment.registerListeners();

                do {
                    try {
                        Thread.sleep(runTime);
                    } catch (final InterruptedException e) {
                        Log.w(TAG, "run(): wait() interrupted: " + e);
                    }
                } while (System.currentTimeMillis() < endTime);

                Log.d(TAG, "run(): Deactivating environment sensors");
                sensedEnvironment.unregisterListeners();
            }
        };

        // Run every 15 minutes, starting 1 second after service start
        new Timer().schedule(getSensorData, 1000, 15 * 60000);

        // Stop & Destroy service
        stopSelf();

        Log.d(TAG, "onHandleIntent() end");
    }
}

package ned.androidfun;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

public class ProximitySensor extends ListeningSensor {
    private static final String TAG = "ProximitySensor";
    private boolean isNear = false;

    ProximitySensor(final SensorManager sensorManager, final int interval) {
        super(sensorManager, Sensor.TYPE_PROXIMITY, interval);
        Log.v(TAG, "ProximitySensor() -");
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        isNear = (event.values[0] == 3.0f) ? true : false;
        Logger.log(TAG, "onSensorChanged(): isNear = " + isNear);
    }

    public boolean isNear() {
        return isNear;
    }
}

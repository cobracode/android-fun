package ned.androidfun;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

class AtmosphericPressureSensor extends ListeningSensor {
    private static final String TAG = "AtmosphericPressureSensor";
    private float millibars = 0.0f;

    AtmosphericPressureSensor(final SensorManager sensorManager, final int interval) {
        super(sensorManager, Sensor.TYPE_PRESSURE, interval);
        Log.v(TAG, "AtmosphericPressureSensor() -");
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if ((changeCount++ % changeInterval) == 0) {
            millibars = event.values[0];
            Log.v(TAG, "onSensorChanged(): millibars = " + millibars);
        }
    }

    public float getMillibars() {
        return millibars;
    }
}
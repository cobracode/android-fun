package ned.androidfun;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

class LightSensor extends ListeningSensor {
    private static final String TAG = "LightSensor";
    private float illuminance = 0.0f;

    LightSensor(final SensorManager sensorManager, final int interval) {
        super(sensorManager, Sensor.TYPE_LIGHT, interval);
        Log.v(TAG, "LightSensor() -");
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if ((changeCount++ % changeInterval) == 0) {
            illuminance = event.values[0];
            Logger.log(TAG, "onSensorChanged(): illuminance = " + illuminance);
        }
    }

    public float getIlluminance() {
        return illuminance;
    }
}

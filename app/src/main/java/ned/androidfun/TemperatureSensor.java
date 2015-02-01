package ned.androidfun;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

class TemperatureSensor extends ListeningSensor {
    private static final String TAG = "TemperatureSensor";
    private float temperatureC = 0.0f;

    TemperatureSensor(final SensorManager sensorManager, final int interval) {
        super(sensorManager, Sensor.TYPE_TEMPERATURE, interval);
        Log.v(TAG, "TemperatureSensor() -");
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if ((changeCount++ % changeInterval) == 0) {
            temperatureC = event.values[0];
            Logger.log(TAG, "onSensorChanged(): temperatureC = " + temperatureC + "; F = " + Conversions.celsiusToFahrenheit(temperatureC));
        }
    }

    public final float getTemperatureF() {
        return Conversions.celsiusToFahrenheit(temperatureC);
    }
}

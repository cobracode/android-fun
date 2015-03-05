package ned.androidfun;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

class AtmosphericPressureSensor extends ListeningSensor {
    private static final String TAG = "AtmosphericPressureSensor";
    private SensorManager sensorManager;
    private float millibars = 0.0f;

    AtmosphericPressureSensor(final SensorManager inSensorManager, final int interval) {
        super(inSensorManager, Sensor.TYPE_PRESSURE, interval);
        sensorManager = inSensorManager;
        Log.v(TAG, "AtmosphericPressureSensor() -");
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if ((changeCount++ % changeInterval) == 0) {
            millibars = event.values[0];
            final String status = String.format("Air pressure: %.2f mb | Estimated altitude: %.0f ft", millibars,
                    Conversions.metersToFeet(sensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, millibars)));

            Logger.log(TAG, "onSensorChanged(): " + status);
            Network.sendToSite(status);
        }
    }

    public float getMillibars() {
        return millibars;
    }
}
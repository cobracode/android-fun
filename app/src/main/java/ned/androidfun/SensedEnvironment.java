package ned.androidfun;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;

class SensedEnvironment {
    private static final String TAG = "SensedEnvironment";
    private SensorManager environmentSensorManager = null;
    private AtmosphericPressureSensor pressureSensor = null;
    private LightSensor lightSensor = null;

    SensedEnvironment(final Context context, final int interval) {
        Log.v(TAG, "SensedEnvironment() begin");
        environmentSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = new AtmosphericPressureSensor(environmentSensorManager, interval);

        // Light sensor occurs as light values change and not constantly;
        // typically need much lower interval, like ~3-5
        lightSensor = new LightSensor(environmentSensorManager, 3);
        Log.v(TAG, "SensedEnvironment() end");
    }

    public void registerListeners() {
        Log.v(TAG, "registerListeners() begin");
        pressureSensor.register(environmentSensorManager);
        lightSensor.register(environmentSensorManager);
        Log.v(TAG, "registerListeners() end");
    }

    public void unregisterListeners() {
        Log.v(TAG, "unregisterListeners() begin");
        pressureSensor.unregister(environmentSensorManager);
        lightSensor.unregister(environmentSensorManager);
        Log.v(TAG, "unregisterListeners() end");
    }

    public double getHumidity() {
        double humidity = 0.0;

        return humidity;
    }

    public float getIlluminance() {
        return lightSensor.getIlluminance();
    }

    public float getMillibars() {
        return pressureSensor.getMillibars();
    }

    public double getTemperatureF() {
        double f = 0.0;

        return f;
    }

    public double getTemperatureDeviceF() {
        double f = 0.0;

        return f;
    }
}

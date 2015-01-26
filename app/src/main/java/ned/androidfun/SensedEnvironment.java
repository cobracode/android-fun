package ned.androidfun;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;

class SensedEnvironment {
    private static final String TAG = "Environment";
    private SensorManager environmentSensorManager = null;
    private AtmosphericPressureSensor pressureSensor = null;
    private LightSensor lightSensor = null;

    SensedEnvironment(final Context context, final int interval) {
        Log.v(TAG, "Environment() begin");
        environmentSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = new AtmosphericPressureSensor(environmentSensorManager, interval);

        // Light sensor occurs as light values change and not constantly;
        // typically need much lower interval, like ~3-5
        lightSensor = new LightSensor(environmentSensorManager, 3);
        Log.v(TAG, "Environment() end");
    }

    public void registerListeners() {
        Log.v(TAG, "registerSensorListener() begin");
        pressureSensor.registerSensorListener(environmentSensorManager);
        lightSensor.registerSensorListener(environmentSensorManager);
        Log.v(TAG, "registerSensorListener() end");
    }

    public void unRegisterListeners() {
        Log.v(TAG, "unRegisterSensorListener() begin");
        pressureSensor.unregisterSensorListener(environmentSensorManager);
        lightSensor.unregisterSensorListener(environmentSensorManager);
        Log.v(TAG, "unRegisterSensorListener() end");
    }

    public double getHumidity() {
        double humidity = 0.0;

        return humidity;
    }

    public float getIlluminanceLX() {
        return lightSensor.getIlluminance();
    }

    public float getPressureMBR() {
        return pressureSensor.getPressureMBR();
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

package ned.androidfun;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;

class SensedEnvironment {
    private static final String TAG = "SensedEnvironment";
    private static SensorManager environmentSensorManager = null;

    // Sensors
    private static AtmosphericPressureSensor pressureSensor = null;
    private static LightSensor lightSensor = null;
    private static ProximitySensor proximitySensor = null;
    private static TemperatureSensor temperatureSensor = null;

    SensedEnvironment(final Context context, final int interval) {
        Log.v(TAG, "SensedEnvironment() begin");
        environmentSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        initializeSensors(interval);
        Log.v(TAG, "SensedEnvironment() end");
    }

    private void initializeSensors(final int interval) {
        // Light sensor occurs as light values change and not constantly;
        // typically need much lower interval, like ~3-5
        lightSensor = new LightSensor(environmentSensorManager, 5);

        pressureSensor = new AtmosphericPressureSensor(environmentSensorManager, interval);
        proximitySensor = new ProximitySensor(environmentSensorManager, interval);
        temperatureSensor = new TemperatureSensor(environmentSensorManager, interval);
    }

    public void registerListeners() {
        Log.v(TAG, "registerListeners() begin");
        lightSensor.register(environmentSensorManager);
        pressureSensor.register(environmentSensorManager);
        proximitySensor.register(environmentSensorManager);
        temperatureSensor.register(environmentSensorManager);
        Log.v(TAG, "registerListeners() end");
    }

    public void unregisterListeners() {
        Log.v(TAG, "unregisterListeners() begin");
        lightSensor.unregister(environmentSensorManager);
        pressureSensor.unregister(environmentSensorManager);
        proximitySensor.unregister(environmentSensorManager);
        temperatureSensor.unregister(environmentSensorManager);
        Log.v(TAG, "unregisterListeners() end");
    }

    public float getAltitudeAirPressure() {
        return environmentSensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressureSensor.getMillibars());
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
        return temperatureSensor.getTemperatureF();
    }

    public double getTemperatureDeviceF() {
        double f = 0.0;

        return f;
    }
}

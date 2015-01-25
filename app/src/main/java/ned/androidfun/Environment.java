package ned.androidfun;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Environment implements SensorEventListener {
    private static final String TAG = "Environment";
    private SensorManager sensorManager = null;
    private Sensor pressureSensor = null;

    private int sensorCount = 0;
    private int sensorInterval = 10;

    // Environmental data
    private float pressureMBR = 0;

    Environment(final Context context, final int interval) {
        Log.v(TAG, "Environment() begin");
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorInterval = interval;
        Log.v(TAG, "Environment() end");
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (sensorCount++ % sensorInterval == 0) {
            Log.v(TAG, "onSensorChanged() begin");
            pressureMBR = event.values[0];
            Log.v(TAG, "onSensorChanged(): pressureMBR = " + pressureMBR);
            Logger.log(TAG, "onSensorChanged(): altitude (ft) = " + SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressureMBR) * 3.281);
            Log.v(TAG, "onSensorChanged() end");
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
        Log.v(TAG, "onAccuracyChanged() begin");
        Log.v(TAG, "onAccuracyChanged() end");
    }

    public void registerListener() {
        Log.v(TAG, "registerSensorListener() begin");
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.v(TAG, "registerSensorListener() end");
    }

    public void unRegisterListener() {
        Log.v(TAG, "unRegisterSensorListener() begin");
        sensorManager.unregisterListener(this);
        Log.v(TAG, "unRegisterSensorListener() end");
    }

    public double getHumidity() {
        double humidity = 0.0;

        return humidity;
    }

    public double getIlluminanceLX() {
        double lx = 0.0;

        return lx;
    }

    public double getPressureMBR() {
        return pressureMBR;
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

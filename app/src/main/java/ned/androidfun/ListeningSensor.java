package ned.androidfun;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

abstract class ListeningSensor implements SensorEventListener, SensorListenerRegistrar {
    private static final String TAG = "ListeningSensor";
    private Sensor sensor = null;
    protected int changeInterval = 0;
    protected int changeCount = 0;

    ListeningSensor(final SensorManager sensorManager, final int sensorType, final int interval) {
        Log.v(TAG, "ListeningSensor() begin");
        sensor = sensorManager.getDefaultSensor(sensorType);
        changeInterval = interval;
        Log.v(TAG, "ListeningSensor() end");
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
        Log.v(TAG, "onAccuracyChanged(): accuracy = " + accuracy);
    }

    @Override
    public void registerSensorListener(final SensorManager manager) {
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void unregisterSensorListener(final SensorManager manager) {
        manager.unregisterListener(this);
    }
}

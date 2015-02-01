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

        if (null == sensor) {
            Log.w(TAG, "ListeningSensor(): sensor type not found on device: " + sensorType);
        }

        changeInterval = interval;
        Log.v(TAG, "ListeningSensor() end");
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
        Log.v(TAG, "onAccuracyChanged(): accuracy = " + accuracy);
    }

    @Override
    public void register(final SensorManager manager) {
        if (null != sensor) {
            manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void unregister(final SensorManager manager) {
        if (null != sensor) {
            manager.unregisterListener(this);
        }
    }
}

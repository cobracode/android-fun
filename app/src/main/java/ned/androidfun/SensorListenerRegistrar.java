package ned.androidfun;

import android.hardware.SensorManager;

interface SensorListenerRegistrar {
    public void registerSensorListener(final SensorManager manager);

    public void unregisterSensorListener(final SensorManager manager);
}

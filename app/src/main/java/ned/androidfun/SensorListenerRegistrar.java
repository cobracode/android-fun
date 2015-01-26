package ned.androidfun;

import android.hardware.SensorManager;

interface SensorListenerRegistrar {
    public void register(final SensorManager manager);

    public void unregister(final SensorManager manager);
}

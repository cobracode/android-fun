package ned.androidfun;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Environment implements SensorEventListener {
    private SensorManager sensorManager = null;
    private Sensor pressureSensor = null;

    Environment(final Context context) {
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {

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
        double mbr = 0.0;

        return mbr;
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

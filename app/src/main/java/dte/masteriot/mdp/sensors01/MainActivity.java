package dte.masteriot.mdp.sensors01;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Button bLight, bAccelerometer;
    TextView tvSensorValue, axValue, ayValue, azValue;
    private SensorManager sensorManager;
    private Sensor lightSensor, accelerometerSensor;
    boolean lightSensorIsActive, accelerometerSensorIsActive;

    @Override
    public void onStop(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("lightSensorIsActive", lightSensorIsActive);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightSensorIsActive = false;
        accelerometerSensorIsActive = false;

        // Get the references to the UI:
        bLight = findViewById(R.id.bLight); // button to start/stop sensor's readings
        bAccelerometer = findViewById(R.id.bAccelerometer);
        tvSensorValue = findViewById(R.id.lightMeasurement); // sensor's values
        axValue = findViewById(R.id.ax); // sensor's values
        ayValue = findViewById(R.id.ay); // sensor's values
        azValue = findViewById(R.id.az); // sensor's values


        // Get the reference to the sensor manager and the sensor:
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Obtain the reference to the default light sensor of the device:
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Listener for the button:
        bLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lightSensorIsActive) {
                    // unregister listener and make the appropriate changes in the UI:
                    sensorManager.unregisterListener(MainActivity.this, lightSensor);
                    bLight.setText(R.string.light_sensor_off);
                    bLight.setBackground(getResources().getDrawable(R.drawable.round_button_off, getTheme()));
                    tvSensorValue.setText("Light sensor is OFF");
                    lightSensorIsActive = false;
                } else {
                    // register listener and make the appropriate changes in the UI:
                    sensorManager.registerListener(MainActivity.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    bLight.setText(R.string.light_sensor_on);
                    bLight.setBackground(getResources().getDrawable(R.drawable.round_button_on, getTheme()));
                    tvSensorValue.setText("Waiting for first light sensor value");
                    lightSensorIsActive = true;
                }
            }
        });
        // Listener for the Accelerometer:
        bAccelerometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accelerometerSensorIsActive) {
                    // unregister listener and make the appropriate changes in the UI:
                    sensorManager.unregisterListener(MainActivity.this, accelerometerSensor);
                    bAccelerometer.setText(R.string.accelerometer_sensor_off);
                    bAccelerometer.setBackground(getResources().getDrawable(R.drawable.round_button_off, getTheme()));
                    axValue.setText("x = None");
                    ayValue.setText("y = None");
                    azValue.setText("z = None");
                    accelerometerSensorIsActive = false;
                } else {
                    // register listener and make the appropriate changes in the UI:
                    sensorManager.registerListener(MainActivity.this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    bAccelerometer.setText(R.string.accelerometer_sensor_on);
                    bAccelerometer.setBackground(getResources().getDrawable(R.drawable.round_button_on, getTheme()));
                    axValue.setText("x = Waiting");
                    ayValue.setText("y = Waiting");
                    azValue.setText("z = Waiting");
                    accelerometerSensorIsActive = true;
                }
            }
        });
    }

    // Methods related to the SensorEventListener interface:

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Show the sensor's value in the UI:
        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            tvSensorValue.setText(Float.toString(sensorEvent.values[0]));
        }
        else if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            axValue.setText(Float.toString(sensorEvent.values[0]));
            ayValue.setText(Float.toString(sensorEvent.values[1]));
            azValue.setText(Float.toString(sensorEvent.values[2]));
        }

        Log.d("sensorEvent", "Blabla");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // In this app we do nothing if sensor's accuracy changes
    }

}

package com.example.dbhowmik.accel;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private View side, front;
    private TextView tvSide, tvFront, tvSideMax, tvFrontMax;

    private long lastUpdate;
    private int base_color = 0xFF444444;
    private int sensitivity = 10;
    private int sideMax, frontMax;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        side = findViewById(R.id.side);
        front = findViewById(R.id.front);
        tvSide = (TextView)findViewById(R.id.sideValue);
        tvFront = (TextView)findViewById(R.id.frontValue);
        tvSideMax = (TextView)findViewById(R.id.sideMax);
        tvFrontMax = (TextView)findViewById(R.id.frontMax);
        side.setBackgroundColor(base_color);
        front.setBackgroundColor(base_color);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        // Movement
        int sideValue = Math.round(Math.abs(values[0])) ;
        int frontValue = Math.round(Math.abs(values[2]));
        long actualTime = event.timestamp;

        if (actualTime - lastUpdate < 200) return;

        lastUpdate = actualTime;

        side.setBackgroundColor(base_color + sideValue * sensitivity * 256 * 256);
        tvSide.setText("" + sideValue);
        if(sideValue > sideMax) sideMax = sideValue;
        tvSideMax.setText("Side Max: " + sideMax);

        front.setBackgroundColor(base_color + frontValue * sensitivity * 256 * 256);
        tvFront.setText("" + frontValue);
        if(frontValue > frontMax) frontMax = frontValue;
        tvFrontMax.setText("Front Max: " + frontMax);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}

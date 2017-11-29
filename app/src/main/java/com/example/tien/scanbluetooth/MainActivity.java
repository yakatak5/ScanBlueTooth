package com.example.tien.scanbluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.util.TimingLogger;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
   public ListView listView;
    public ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();

    private long startnow;
    private long endnow;
    private static final String MYTAG = "MyActivity";
    TimingLogger timings = new TimingLogger(MYTAG, "MainActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        timings.addSplit("work A");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        Button boton = (Button) findViewById(R.id.Bscan);
        boton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BTAdapter.startDiscovery();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            timings.addSplit("work B");
              startnow = System.nanoTime();

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
               // String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                TextView rssi_msg = (TextView) findViewById(R.id.textView);
                endnow = System.nanoTime();
                long timeEnd = (endnow - startnow)/1000000;
               // rssi_msg.setText( "\n" + deviceHardwareAddress +  " | " + rssi_msg.getText()  + " | " +  deviceName + " |  "  + rssi + " dBm"   +  " | time process: " + timeEnd+ " milliseconds" );
    rssi_msg.setText("\n" + rssi_msg.getText() + " Device Name " + deviceName  + " | Device Address  " + deviceHardwareAddress + "\n" +  " Signal Strength : " + rssi + "dBm.\n"  + " | time process: " + timeEnd + " milliseconds" + "\n\n");
                //rssi_msg.setText("Device Name " + deviceName + " | " + deviceHardwareAddress + " | " + rssi + " dBm\n" +  " | time process: " + timeEx + " milliseconds");

            }

            timings.addSplit("work C");
            timings.dumpToLog();
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();


        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);

    }
}

package com.example.tien.scanbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TimingLogger;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
   public ListView listView;
    public ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    RequestQueue requestQueue;
    String insertUrl = "http://10.176.138.38/log/insert.php";
    private long startnow;
    private long endnow;
    private static final String MYTAG = "MyActivity";
    TimingLogger timings = new TimingLogger(MYTAG, "MainActivity");
    String deviceName;
    String deviceHardwareAddress;
    int rssi;
    Date currentTime;
    long timeEnd;
    TextView rssi_msg = (TextView) findViewById(R.id.textView);

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

        requestQueue = Volley.newRequestQueue(getApplicationContext());



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
            currentTime = Calendar.getInstance().getTime();
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();
                rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
               // String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceName = device.getName();
                deviceHardwareAddress = device.getAddress();

                endnow = System.nanoTime();
                timeEnd = (endnow - startnow)/1000000;
               // rssi_msg.setText( "\n" + deviceHardwareAddress +  " | " + rssi_msg.getText()  + " | " +  deviceName + " |  "  + rssi + " dBm"   +  " | time process: " + timeEnd+ " milliseconds" );
    rssi_msg.setText("\n"  + rssi_msg.getText() +   currentTime + " Device Name " + deviceName  + " | Device Address  " + deviceHardwareAddress + "\n" +  " Signal Strength : " + rssi + "dBm.\n"  + " | time process: " + timeEnd + " milliseconds" + "\n\n");
                //rssi_msg.setText("Device Name " + deviceName + " | " + deviceHardwareAddress + " | " + rssi + " dBm\n" +  " | time process: " + timeEx + " milliseconds");

                StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters  = new HashMap<String, String>();
                        parameters.put("date",currentTime.toString());
                        parameters.put("name",deviceName);
                        parameters.put("address",deviceHardwareAddress);
                        parameters.put("strength", Integer.toString(rssi));
                        parameters.put("start", (timeEnd + "ms") );

                        return parameters;
                    }
                };
                requestQueue.add(request);
            }

        }

        };




    @Override
    protected void onDestroy() {
        super.onDestroy();


        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);

    }
}

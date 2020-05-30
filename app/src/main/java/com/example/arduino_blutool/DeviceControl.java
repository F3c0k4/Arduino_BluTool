package com.example.arduino_blutool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.UUID;

public class DeviceControl extends AppCompatActivity {

    ToggleButton Tgl_Btn_13;
    ToggleButton Tgl_Btn_12;
    ToggleButton Tgl_Btn_11;
    Button Btn_Dc;

    String DeviceAddr = null;
    private ProgressDialog progress;
    BluetoothAdapter Bt_Adapter = null;
    BluetoothSocket Bt_Socket = null;
    private boolean isBtConnected = false;
    CompoundButton.OnCheckedChangeListener Tgl_Btn_Listener;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_device_control);
        Tgl_Btn_13 = (ToggleButton) findViewById(R.id.Tgl_Btn13);
        Tgl_Btn_12 = (ToggleButton) findViewById(R.id.Tgl_Btn12);
        Tgl_Btn_11 = (ToggleButton) findViewById(R.id.Tgl_Btn11);

        Tgl_Btn_Listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Bt_Socket != null) {
                    try {
                        switch(buttonView.getId()) {
                            case R.id.Tgl_Btn13:
                                Bt_Socket.getOutputStream().write("13\n".getBytes());
                                break;
                            case R.id.Tgl_Btn12:
                                Bt_Socket.getOutputStream().write("12\n".getBytes());
                                break;
                            case R.id.Tgl_Btn11:
                                Bt_Socket.getOutputStream().write("11\n".getBytes());
                        }
                    } catch (IOException e) {
                        msg("Error");
                    }
                }
            }
        };
        Tgl_Btn_13.setOnCheckedChangeListener(Tgl_Btn_Listener);
        Tgl_Btn_12.setOnCheckedChangeListener(Tgl_Btn_Listener);
        Tgl_Btn_11.setOnCheckedChangeListener(Tgl_Btn_Listener);
        Intent intent = getIntent();
        DeviceAddr = intent.getStringExtra(DeviceList.EXTRA_ADDRESS);
        new ConnectBT().execute();

    }


    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(DeviceControl.this, "Connecting...", "Please wait!!!");
        }

        @Override
        protected Void doInBackground(Void... params) //connecting in background
        {
            try {
                if (Bt_Socket == null || !isBtConnected) {
                    Bt_Adapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice remote_device = Bt_Adapter.getRemoteDevice(DeviceAddr);//check if remote device is available
                    Bt_Socket = remote_device.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    Bt_Socket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }

    }

}

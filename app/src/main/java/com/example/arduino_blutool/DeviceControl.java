package com.example.arduino_blutool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class DeviceControl extends AppCompatActivity {

    ToggleButton TglBtn_13;
    Button Btn_Dc;

    String DeviceAddr = null;
    private ProgressDialog progress;
    BluetoothAdapter Bt_Adapter = null;
    BluetoothSocket Bt_Socket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        TglBtn_13 = (ToggleButton)findViewById(R.id.TglBtn13);
        TglBtn_13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Bt_Socket!=null)
                {
                    try{

                        Bt_Socket.getOutputStream().write("13\n".getBytes());
                        //Bt_Socket.getOutputStream().write("\n".getBytes());
                        msg("Output written");

                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }

            }
        });
        Intent newint = getIntent();
        DeviceAddr = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);
        new ConnectBT().execute();
    }


    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(DeviceControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... params) //connecting in background
        {
            try
            {
                if (Bt_Socket == null || !isBtConnected)
                {
                    Bt_Adapter = BluetoothAdapter.getDefaultAdapter();//get the adapter
                    BluetoothDevice dispositivo = Bt_Adapter.getRemoteDevice(DeviceAddr);//check if remote device is available
                    Bt_Socket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    Bt_Socket.connect();//start connecting
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the execute
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }

    }

}

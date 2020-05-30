package com.example.arduino_blutool;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeviceList extends AppCompatActivity {
    private BluetoothAdapter Bt_Adapter = null;
    private BluetoothDevice[] paired_Devices;
    Spinner PairedSpinner;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_device_list);
        Bt_Adapter = BluetoothAdapter.getDefaultAdapter();
        PairedSpinner = (Spinner)findViewById(R.id.Spinner_Paired);
        if(Bt_Adapter == null)
        {

            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();
        }
        else
        {
            if (Bt_Adapter.isEnabled())
            {

            }
            else
            {

                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon,1);
            }
            GeneratePairedDevicesList();
            FillSpinner();
        }
    }
    public void GeneratePairedDevicesList()
    {
        paired_Devices = Bt_Adapter.getBondedDevices().toArray(new BluetoothDevice[0]);
        ArrayList list = new ArrayList();
        String prntString = "";

        if (paired_Devices.length>0)
        {
            for(BluetoothDevice bt : paired_Devices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress());
                prntString = prntString + bt.getName() +"\n";
                Toast.makeText(getApplicationContext(), prntString, Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

    }

    void FillSpinner(){
        SpinAdapter spinner_adp1 = new SpinAdapter(this,
                android.R.layout.simple_list_item_1, paired_Devices);
        spinner_adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PairedSpinner.setAdapter(spinner_adp1);

    }

    public void TakeControl(View view){
        BluetoothDevice selected_bd = (BluetoothDevice)PairedSpinner.getSelectedItem();
        Toast.makeText(getApplicationContext(), selected_bd.getAddress(), Toast.LENGTH_LONG).show();
        Intent i = new Intent(DeviceList.this, DeviceControl.class);


        i.putExtra(EXTRA_ADDRESS, selected_bd.getAddress());
        startActivity(i);
    }
    public class SpinAdapter extends ArrayAdapter<BluetoothDevice>{

        // Your sent context
        private Context context;
        // Your custom values for the spinner (User)
        private BluetoothDevice[] values;

        public SpinAdapter(Context context, int textViewResourceId,
                           BluetoothDevice[] values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount(){
            return values.length;
        }

        @Override
        public BluetoothDevice getItem(int position){
            return values[position];
        }

        public String getItemAddress(int position){
            return values[position].getAddress();
        }
        @Override
        public long getItemId(int position){
            return position;
        }







        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.BLACK);


            label.setText(values[position].getName());

            return label;
        }


        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(values[position].getName());

            return label;
        }
    }

}

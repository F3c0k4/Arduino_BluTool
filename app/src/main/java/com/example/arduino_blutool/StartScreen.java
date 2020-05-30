package com.example.arduino_blutool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

    }

    public void list_devices(View view){
        Intent intent = new Intent(StartScreen.this,DeviceList.class);
        startActivity(intent);
    }
}

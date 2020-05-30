package com.example.arduino_blutool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

    }

    public void list_devices(View view){
        Intent intent = new Intent(StartScreen.this,DeviceList.class);
        startActivity(intent);
    }
}

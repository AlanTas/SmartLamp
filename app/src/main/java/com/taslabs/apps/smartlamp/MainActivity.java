package com.taslabs.apps.smartlamp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;


import java.io.StringReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    ColorPickerView pickerView;
    TextView textView;
    TextView upp;
    TextView downn;

    AjudanteMqtt ajudanteMqtt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pickerView = (ColorPickerView) findViewById(R.id.color_picker_view);
        textView = (TextView) findViewById(R.id.textView);
        upp = (TextView) findViewById(R.id.textView2);
        downn = (TextView) findViewById(R.id.textView3);


        ajudanteMqtt = new AjudanteMqtt(getApplicationContext(), upp, downn);
        ajudanteMqtt.connect();

        pickerView.addOnColorSelectedListener((new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                textView.setBackgroundTintList(ColorStateList.valueOf(selectedColor));

                int red = Color.red(selectedColor);
                int green = Color.green(selectedColor);
                int blue = Color.blue(selectedColor);

                String color = Integer.toString(red) + ", " + Integer.toString(green) + ", " + Integer.toString(blue);

                textView.setText(color);


                    String mensagem = "#r=" + Integer.toString(red) + "*g=" + Integer.toString(green) + "*b=" + Integer.toString(blue) + "*";
                    ajudanteMqtt.publish(mensagem);

            }
        }));


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

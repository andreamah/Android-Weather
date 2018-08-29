package com.example.andrea.android_weather;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView weather;
    private Button buttonWeather;
    private TextView cityLabel;
    private EditText latText;
    private EditText longText;
    private Button useCoordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weather = findViewById(R.id.weather);
        cityLabel = findViewById(R.id.cityLabel);
        latText = findViewById(R.id.latText);
        longText = findViewById(R.id.longText);
        buttonWeather = findViewById(R.id.buttonWeather);
        useCoordButton = findViewById(R.id.useCoordButton);
        buttonWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeather("-95.3","29.76");

            }
        });
        useCoordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeather(longText.getText().toString().trim(),latText.getText().toString().trim());

            }
        });
    }


    public void getWeather(String longit, String lat)
    {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + longit + "&appid=4d7b12afe17f7310bc20440105c1776d&units=metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            weather.setText(response.getJSONObject("main").getDouble("temp") + "°C");


                            String currentCity = response.getString("name");
                            if (currentCity.length()==0)
                                currentCity = "this location";

                            cityLabel.setText("The weather in " + currentCity + " is");

                            Toast.makeText(MainActivity.this, "Weather Updated!",
                                    Toast.LENGTH_LONG).show();

                        }
                        catch(Exception e)
                        {
                            cityLabel.setText("ERROR!");
                            weather.setText("");

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cityLabel.setText("ERROR RETRIEVING DATA");
                        weather.setText("");

                    }
                });
        //from guide on https://www.youtube.com/watch?v=8-7Ip6xum6E
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


}

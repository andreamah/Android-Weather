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
    private TextView weather,cityLabel;
    private Button buttonWeather,useCoordButton;
    private EditText latText,longText;

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

        //set on-click listener for the button that displays the Houston weather
        buttonWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get weather using Houston coordinates
                getWeather("-95.3","29.76");

            }
        });

        //set on-click listener for custom weather button
        useCoordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //use values from EditTexts for latitude and longitude
                getWeather(longText.getText().toString(),latText.getText().toString());

            }
        });
    }

    /**
     * Sets weather and cityLabel TextViews using location-based JSON string
     * @param longit the longitude of the custom location
     * @param lat the latitude of the custom location
     */
    public void getWeather(String longit, String lat)
    {
        //define request link using custom lat & long values
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + longit + "&appid=4d7b12afe17f7310bc20440105c1776d&units=metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            //display the weather
                            weather.setText(response.getJSONObject("main").getDouble("temp") + "°C");

                            //find city located at lat & long
                            //if no city is located, use generic string
                            String currentCity = response.getString("name");
                            if (currentCity.length()==0)
                                currentCity = "this location";

                            //display city where weather is
                            cityLabel.setText("The weather in " + currentCity + " is");

                            Toast.makeText(MainActivity.this, "Weather Updated!",
                                    Toast.LENGTH_LONG).show();

                        }
                        catch(Exception e)
                        {
                            //if exception from JSON is thrown, set textViews to show error
                            cityLabel.setText("ERROR!");
                            weather.setText("");

                        }
                    }
                }, new Response.ErrorListener() {
                    //if issue with API request occurs, set textViews to show error

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cityLabel.setText("ERROR RETRIEVING DATA");
                        weather.setText("");

                    }
                });
        //from guide on https://www.youtube.com/watch?v=8-7Ip6xum6E
        //add the request to request queue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


}

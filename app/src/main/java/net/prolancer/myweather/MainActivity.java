package net.prolancer.myweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import net.prolancer.myweather.adapter.WeatherListAdapter;
import net.prolancer.myweather.domain.LocationVo;
import net.prolancer.myweather.domain.WeatherVo;
import net.prolancer.myweather.network.NetworkManager;
import net.prolancer.myweather.network.NetworkResponseListener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSION_FINE_LOCATION = 99;

    private FusedLocationProviderClient fusedLocationClient;

    private TextView txtLocation;
    private Button btnRefresh;
    private ListView lvWeatherForecast;
    private ProgressBar progressBar;

    WeatherListAdapter weatherListAdapter;
    List<WeatherVo> weatherForecasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLocation = findViewById(R.id.txtLocation);
        btnRefresh = findViewById(R.id.btnRefresh);
        lvWeatherForecast = findViewById(R.id.lvWeatherForecast);
        progressBar = findViewById(R.id.progressBar);

        createLocationRequest();
        updateGPS();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGPS();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                } else {
                    Toast.makeText(this, R.string.location_permission_error, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    /**
     * SETUP LOCATION REQUEST
     */
    private void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    /**
     * Update GPS
     */
    private void updateGPS() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        getLocationFromMetaWeather(location.getLatitude(), location.getLongitude());
                    }
                }
            });
        }
    }

    /**
     * Get Location (woeid) from MetaWeather
     * @param latitude
     * @param longitude
     */
    private void getLocationFromMetaWeather(double latitude, double longitude) {
        // #1. Generate request message with Json
        Map<String, String> reqMap = new LinkedHashMap<>();
        reqMap.put("lattlong", latitude + "," + longitude);

        progressBar.setVisibility(View.VISIBLE);

        // #2. Call API
        NetworkManager.sendToServer(MainActivity.this, "/api/location/search/", "GET", reqMap, new NetworkResponseListener() {
            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, "Search Location Error - " + throwable.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onSuccess(Response response) {
                try {
                    if (response != null) {
                        String result = response.body().string();
                        LocationVo[] arrLocation = new Gson().fromJson(result, LocationVo[].class);
                        if (arrLocation != null && arrLocation.length > 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtLocation.setText("[" + arrLocation[0].getTitle() + "]");
                                }
                            });

                            int cityID = arrLocation[0].getWoeid();
                            getWeatherForecast(cityID);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * Get Weather Forecast
     * @param cityID
     */
    private void getWeatherForecast(int cityID) {
        // #1. Call API
        NetworkManager.sendToServer(MainActivity.this, "/api/location/" + cityID + "/", "GET", null, new NetworkResponseListener() {
            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, "Weather Forecast Error - " + throwable.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onSuccess(Response response) {
                try {
                    if (response != null) {
                        String result = response.body().string();
                        JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
                        String consolidated_weather = jsonObject.get("consolidated_weather").getAsJsonArray().toString();
                        WeatherVo[] arrWeather = new Gson().fromJson(consolidated_weather, WeatherVo[].class);

                        weatherForecasts = new ArrayList<>();
                        for (WeatherVo weatherVo : arrWeather) {
                            weatherVo.setMin_temp_f(convertCelsiusToFahrenheit(weatherVo.getMin_temp()));
                            weatherVo.setMax_temp_f(convertCelsiusToFahrenheit(weatherVo.getMax_temp()));
                            weatherVo.setThe_temp_f(convertCelsiusToFahrenheit(weatherVo.getThe_temp()));
                            weatherForecasts.add(weatherVo);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                weatherListAdapter = new WeatherListAdapter(MainActivity.this, weatherForecasts);
                                lvWeatherForecast.setAdapter(weatherListAdapter);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * Convert Celsius To Fahrenheit
     * @param temp
     * @return
     */
    private float convertCelsiusToFahrenheit(float temp) {
        return (temp * 9/5) + 32;
    }
}
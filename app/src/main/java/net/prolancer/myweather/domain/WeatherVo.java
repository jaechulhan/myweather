package net.prolancer.myweather.domain;

import android.util.Log;

public class WeatherVo {
    private String id;
    private String applicable_date;
    private String weather_state_name;
    private String weather_state_abbr;
    private float wind_speed;
    private float wind_direction;
    private String wind_direction_compass;
    private float min_temp;
    private float max_temp;
    private float the_temp;
    private float min_temp_f;
    private float max_temp_f;
    private float the_temp_f;
    private float air_pressure;
    private float humidity;
    private float visibility;
    private int predictability;
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicable_date() {
        return applicable_date;
    }

    public void setApplicable_date(String applicable_date) {
        this.applicable_date = applicable_date;
    }

    public String getWeather_state_name() {
        return weather_state_name;
    }

    public void setWeather_state_name(String weather_state_name) {
        this.weather_state_name = weather_state_name;
    }

    public String getWeather_state_abbr() {
        return weather_state_abbr;
    }

    public void setWeather_state_abbr(String weather_state_abbr) {
        this.weather_state_abbr = weather_state_abbr;
    }

    public float getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(float wind_speed) {
        this.wind_speed = wind_speed;
    }

    public float getWind_direction() {
        return wind_direction;
    }

    public void setWind_direction(float wind_direction) {
        this.wind_direction = wind_direction;
    }

    public String getWind_direction_compass() {
        return wind_direction_compass;
    }

    public void setWind_direction_compass(String wind_direction_compass) {
        this.wind_direction_compass = wind_direction_compass;
    }

    public float getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(float min_temp) {
        this.min_temp = min_temp;
    }

    public float getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(float max_temp) {
        this.max_temp = max_temp;
    }

    public float getThe_temp() {
        return the_temp;
    }

    public void setThe_temp(float the_temp) {
        this.the_temp = the_temp;
    }

    public float getMin_temp_f() {
        return min_temp_f;
    }

    public void setMin_temp_f(float min_temp_f) {
        this.min_temp_f = min_temp_f;
    }

    public float getMax_temp_f() {
        return max_temp_f;
    }

    public void setMax_temp_f(float max_temp_f) {
        this.max_temp_f = max_temp_f;
    }

    public float getThe_temp_f() {
        return the_temp_f;
    }

    public void setThe_temp_f(float the_temp_f) {
        this.the_temp_f = the_temp_f;
    }

    public float getAir_pressure() {
        return air_pressure;
    }

    public void setAir_pressure(float air_pressure) {
        this.air_pressure = air_pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public int getPredictability() {
        return predictability;
    }

    public void setPredictability(int predictability) {
        this.predictability = predictability;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Date: " + applicable_date +
                ", State: " + weather_state_name +
                ", Wind: " + String.format("%.2f", wind_speed) + "(" + wind_direction_compass + ")" +
                ", Temp: (Min) " + String.format("%.2f (C) %.2f (F)", min_temp, min_temp_f) + " (Max) " + String.format("%.2f (C) %.2f (F)", max_temp, max_temp_f);
    }
}

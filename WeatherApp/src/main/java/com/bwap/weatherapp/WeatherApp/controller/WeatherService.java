package com.bwap.weatherapp.WeatherApp.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class WeatherService
{
    private OkHttpClient client;
    private Response response;
    private String CityName;
    String unit;
    private String API="c8fcea75e6642ca0e2f4fec7e968b300";


    public JSONObject getWeather()
    {
       client=new OkHttpClient();
        Request request= new Request.Builder().url("http://api.openweathermap.org/data/2.5/weather?q="+getCityName()+"&units="+getUnit()+"&appid=c8fcea75e6642ca0e2f4fec7e968b300")
                .build();
        try {
            response= client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;


    }

    public JSONArray returnWeatherArray() throws JSONException {

        JSONArray weatherArry=getWeather().getJSONArray("weather");
        return weatherArry;
    }
    public JSONObject returnMain() throws JSONException {
        JSONObject main = getWeather().getJSONObject("main");
        return main;
    }
    public JSONObject returnWind() throws JSONException {
        JSONObject wind = getWeather().getJSONObject("wind");
        return wind;
    }
    public JSONObject returnSys() throws JSONException {
        JSONObject sys = getWeather().getJSONObject("sys");
        return sys;
    }



    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

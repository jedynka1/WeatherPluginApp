
package com.atakmap.android.plugintemplate;

import java.util.List;
import java.util.Queue;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=503731d3cb394ea86dc6cfdd6cdb357a

public interface JsonApi {
    //?lat=44.34&lon=10.99&appid=503731d3cb394ea86dc6cfdd6cdb357a
    @GET("data/2.5/weather")
    Call<com.atakmap.android.plugintemplate.WeatherResponse> weatherResponse(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String appid);

}

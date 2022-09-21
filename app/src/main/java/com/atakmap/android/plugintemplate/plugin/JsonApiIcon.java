package com.atakmap.android.plugintemplate.plugin;



import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=503731d3cb394ea86dc6cfdd6cdb357a

    public interface JsonApiIcon {
        //?lat=44.34&lon=10.99&appid=503731d3cb394ea86dc6cfdd6cdb357a
        @GET("img/wn/{icon}@2x.png")
        Call<ResponseBody> getWeatherIcon(@Path("icon") String icon, @Query("appid") String appid);

    }


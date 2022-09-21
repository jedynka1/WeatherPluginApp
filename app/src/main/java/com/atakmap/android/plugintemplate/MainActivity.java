//package com.atakmap.android.plugintemplate;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.widget.TextView;
//
//import java.util.List;
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.http.POST;
//import retrofit2.http.GET;
//
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class MainActivity extends AppCompatActivity {
//    private TextView textView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();


//        textView = findViewById(R.id.showData);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.openweathermap.org/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        JsonApi jsonPlaceHolderApi = retrofit.create(JsonApi.class);
//
//        Call<WeatherResponse> weatherResponse = jsonPlaceHolderApi.weatherResponse(44.34, 10, "503731d3cb394ea86dc6cfdd6cdb357a");
//
//        weatherResponse.enqueue(new Callback<WeatherResponse>() {
//            @Override
//            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
////                if (!response.isSuccessful()) {
////                   textView.setText("Code:" + response.code());
////               }
//                WeatherResponse weatherResponses = response.body();
//
//                for(Weather weather: weatherResponses.getWeather()){
//                    String content = "";
//                    content += weatherResponses.getCoord().getLat() + "\n";
//                    content += weatherResponses.getCoord().getLon() + "\n";
//                    content += weather.getId() + "\n";
//                    content += weather.getMain() + "\n";
//                    content += weather.getDescription() + "\n";
//                    content += weather.getIcon() + "\n";
//                    content += weatherResponses.getBase() + "\n";
//                    content += weatherResponses.getMain().getTemp() + "\n";
//                    content += weatherResponses.getMain().getFeelsLike() + "\n";
//                    content += weatherResponses.getMain().getTempMin() + "\n";
//                    content += weatherResponses.getMain().getTempMax() + "\n";
//                    content += weatherResponses.getMain().getPressure() + "\n";
//                    content += weatherResponses.getMain().getHumidity() + "\n";
//                    content += weatherResponses.getVisibility() + "\n";
//                    content += weatherResponses.getWind().getSpeed() + "\n";
//                    content += weatherResponses.getWind().getDeg() + "\n";
//                    content += weatherResponses.getClouds().getAll() + "\n";
//                    content += weatherResponses.getDt() + "\n";
//                    content += weatherResponses.getSys().getType() + "\n";
//                    content += weatherResponses.getSys().getId() + "\n";
//                    content += weatherResponses.getSys().getCountry() + "\n";
//                    content += weatherResponses.getSys().getSunrise() + "\n";
//                    content += weatherResponses.getSys().getSunset() + "\n";
//                    content += weatherResponses.getTimezone() +  "\n";
//                    content += weatherResponses.getId() + "\n";
//                    content += weatherResponses.getName() + "\n";
//                    content += weatherResponses.getCod() + "\n";
//                    textView.append(content);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WeatherResponse> call, Throwable t) {
//                textView.setText(t.getMessage());
//
//            }
//        });
//    }
//}


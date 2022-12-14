
package com.atakmap.android.plugintemplate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atakmap.android.cot.CotMapComponent;
import com.atakmap.android.drawing.mapItems.DrawingRectangle;
import com.atakmap.android.drawing.mapItems.DrawingShape;
import com.atakmap.android.icons.UserIcon;
import com.atakmap.android.importexport.CotEventFactory;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapGroup;
import com.atakmap.android.maps.Marker;
import com.atakmap.android.maps.MultiPolyline;
import com.atakmap.android.menu.PluginMenuParser;
import com.atakmap.android.plugintemplate.plugin.JsonApiIcon;
import com.atakmap.android.routes.Route;
import com.atakmap.android.user.PlacePointTool;
import com.atakmap.coremap.cot.event.CotEvent;
import com.atakmap.coremap.cot.event.CotPoint;
import com.atakmap.coremap.maps.coords.GeoPoint;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import android.os.Bundle;
import android.content.res.Resources;



import com.atakmap.coremap.log.Log;
import com.atakmap.coremap.maps.coords.GeoPointMetaData;
import com.atakmap.coremap.maps.time.CoordinatedTime;

import okhttp3.ResponseBody;
import opencsv.CSVReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import java.lang.Math;
import retrofit2.converter.gson.GsonConverterFactory;

public class PluginTemplateDropDownReceiver extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    public static final String SHOW_PLUGIN = "com.atakmap.android.plugintemplate.SHOW_PLUGIN";
    public View templateView;

    private TextView textView;
    private MapView mapView;
    private Route r;

    private final Context pluginContext;
    private Paint paint;

    /**************************** CONSTRUCTOR *****************************/

    public PluginTemplateDropDownReceiver(final MapView mapView,
                                          final Context context) {
        super(mapView);
        this.pluginContext = context;

        templateView = PluginLayoutInflater.inflate(context,
                R.layout.fragment_plugin_main, null);



        textView = templateView.findViewById(R.id.load_weather_data);
       // textView.setBackgroundColor(Color.rgb(226, 228, 229));
       // textView.setText("P O G O D A");

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {

            private void toast(String str) {
                Toast.makeText(getMapView().getContext(), str,
                        Toast.LENGTH_LONG).show();
            }
            public boolean onLongClick(View v) {
            int id = v.getId();
                if (id == R.id.enter_coordinates) {
                    toast(context.getString(R.string.enter_coordinates));
//                }//else if (id == R.id.enter_lat) {
//                    toast(context.getString(R.string.enter_lat));
                }else if (id == R.id.enter_your_lat) {
                    toast(context.getString(R.string.enter_your_lat));
//                }else if(id == R.id.enter_lon){
//                    toast(context.getString(R.string.enter_lon));
                }else if(id == R.id.enter_your_lon){
                    toast(context.getString(R.string.enter_your_lon));
                }else if(id == R.id.refresh){
                    toast(context.getString(R.string.confirm));
//                }else if (id == R.id.load_weather_data){
//                    toast(context.getString(R.string.load_weather_data));
                }
                return true;
            }
        };

        final Button confirmCoordinates = templateView
                .findViewById(R.id.refresh);
        confirmCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        final Button displayWeatherData = templateView
                .findViewById(R.id.displayWeatherData);
        displayWeatherData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayWeather();
            }
        });
    }


    private void displayWeather() {
        //Button cloud =templateView.findViewById(R.id.cloud);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofitIcon = new Retrofit.Builder()
                .baseUrl("http://openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiIcon jsonPlaceHolderApiIcon = retrofitIcon.create(JsonApiIcon.class);
        JsonApi jsonPlaceHolderApi = retrofit.create(JsonApi.class);
        EditText parLan =templateView.findViewById(R.id.enter_your_lat);
        EditText parLon = templateView.findViewById(R.id.enter_your_lon);
        double cordLan = Double.parseDouble(parLan.getText().toString());
        double cordLon = Math.ceil(Double.parseDouble(parLon.getText().toString()));


        CotEvent cotEvent = createPoint(cordLan, cordLon);
        cotEvent.setUID("znacznik");
        cotEvent.setType("a-f-G-U-C-I");
        CotMapComponent.getInternalDispatcher().dispatch(cotEvent);


        Call<WeatherResponse> weatherResponse = jsonPlaceHolderApi.weatherResponse(cordLan,cordLon, "503731d3cb394ea86dc6cfdd6cdb357a");

        weatherResponse.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (!response.isSuccessful()) {
                    textView.setText("Code:" + response.code());
                }
                WeatherResponse weatherResponses = response.body();


                for(Weather weather: weatherResponses.getWeather()){
                    String content = "";
                    content +="Temp:"  + " " + String.format("%.1f", convert(weatherResponses.getMain().getTemp())) + "C " + "\n";
                    content +="Temp odczuwalna" + " " + String.format("%.1f", convert(weatherResponses.getMain().getFeelsLike()))+ "C" + "\n";
                    content +="Ci??nienie:" + " " + weatherResponses.getMain().getPressure() + "hPa"+"\n";
                    content +="Wilgotno????:" + " " + weatherResponses.getMain().getHumidity() + "g/m^3"+"\n";
 //                   content +="Opis:" + " " +  weather.getDescription() + "\n";
 //                   content += "alalal" + weatherResponses.getWind().getSpeed() + "m/s" + "\n";
//                    content += weatherResponses.getWind().getDeg() + "\n";
//                    content += weatherResponses.getClouds().getAll() + "\n";
//                    content += weatherResponses.getDt() + "\n";
//                    content += weatherResponses.getSys().getType() + "\n";
//                    content += weatherResponses.getSys().getId() + "\n";
                    content += "Kraj:" + " " + weatherResponses.getSys().getCountry() + "\n";
//                    content += weatherResponses.getSys().getSunrise() + "\n";
//                    content += weatherResponses.getSys().getSunset() + "\n";
//                    content += "Strefa czasowa: " + " " + weatherResponses.getTimezone() +  "\n";
//                    content += weatherResponses.getId() + "\n";
//                    content += weatherResponses.getName() + "\n";
//                    content += weatherResponses.getCod() + "\n";
                    textView.setText(content);
                   // cloud.setText(content);
                    Log.d("test", "cordinated setted succesfully" + content);
                    Log.d("test", "opis pobrany" + weather.getDescription());

                }
            }

            private double convert(Double temp) {
                temp = temp - 273.15;
                return temp;
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textView.setText(t.getMessage());
                Log.e("test",  "Nie udalo sie pobrac pogody", t);

            }
        });



    }

    public void refresh(){
        //Button cloud =templateView.findViewById(R.id.cloud);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofitIcon = new Retrofit.Builder()
                .baseUrl("http://openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        JsonApiIcon jsonPlaceHolderApiIcon = retrofitIcon.create(JsonApiIcon.class);
        JsonApi jsonPlaceHolderApi = retrofit.create(JsonApi.class);

        PlacePointTool.MarkerCreator mc = new PlacePointTool.MarkerCreator(
                getMapView().getPointWithElevation());
        mc.showCotDetails(false);
        double lantitude =  mc.placePoint().getPoint().getLatitude();
        String parseLantitude   = String.format("%.5f", lantitude);
        String[] splitLantitudeNumber= parseLantitude.split(",");
        String latNumber = splitLantitudeNumber[0]+ "."+splitLantitudeNumber[1];
        lantitude = Double.parseDouble(latNumber);
        double lontitude = mc.placePoint().getPoint().getLongitude();
        String parseLontitude =  String.format("%.5f", lontitude);
        String[] splitLontitudeNumber = parseLontitude.split(",");
        String lonNumber = splitLontitudeNumber[0] + "." + splitLontitudeNumber[1];
        lontitude = Double.parseDouble(lonNumber);


        Log.d("test", "lan set properly" + lantitude);
       // Log.d("test", "lon set properly" + lontitude);


         EditText parLan =templateView.findViewById(R.id.enter_your_lat);
         parLan.setText(String.valueOf(lantitude));
         EditText parLon = templateView.findViewById(R.id.enter_your_lon);
         parLon.setText(String.valueOf(lontitude));
        //Marker m = mc.placePoint();

        // m.setPoint(Double.parseDouble(String.valueOf(lantitude)),Double.parseDouble(String.valueOf(lontitude);
        CotPoint cotPoint = new CotPoint(Double.parseDouble(String.valueOf(lantitude)),Double.parseDouble(String.valueOf(lontitude)), 2.0, 2.0, 0.0 );
        CotEvent cotEvent = createPoint(lantitude, lontitude);
        cotEvent.setUID("znacznik");
        //cotEvent.setPoint(cotPoint);
        cotEvent.setType("a-f-G-U-C-I");
        CotMapComponent.getInternalDispatcher().dispatch(cotEvent);
        Log.d("test", "znacznik zostal poprawnie ustawiony" + cotEvent);

       // double cordLan = Double.parseDouble(parLan.getText().toString());

        Log.d("test", "cord Lan setted properly" + parLan.getText().toString());
      //  double cordLon = Math.ceil(Double.parseDouble(parLon.getText().toString()));
         Log.d("test", "cord Lon setted properly" + parLon.getText().toString());
       // Log.d("test", "get cordinate properly");



        Call<WeatherResponse> weatherResponse = jsonPlaceHolderApi.weatherResponse(lantitude,lontitude, "503731d3cb394ea86dc6cfdd6cdb357a");

        weatherResponse.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (!response.isSuccessful()) {
                    textView.setText("Code:" + response.code());
                }
                WeatherResponse weatherResponses = response.body();
                if (weatherResponses.getWeather().size() >= 1) {
                    Weather weather = weatherResponses.getWeather().get(0);
                    Log.d("test", "pobrano" + weather.getIcon());
                    Call<ResponseBody> responseIcon = jsonPlaceHolderApiIcon.getWeatherIcon(weather.getIcon(), "503731d3cb394ea86dc6cfdd6cdb357a");
                    responseIcon.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("test", "pobrano" + response.code());
                            Log.d("test", "pobrano" + response.body());
                            BufferedInputStream isr = new BufferedInputStream(response.body().byteStream());
                            Bitmap bitMap = BitmapFactory.decodeStream(isr);
                            ImageView view = templateView.findViewById(R.id.icon);
                            view.setImageBitmap(bitMap);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                    String content = "";
//                    content += weather.getId() + "\n";
                    content += "Opis" + " " + weather.getMain() + "\n";
                    //content += weather.getDescription() + "\n";
//                    content += weather.getIcon() + "\n";
//                    content += weatherResponses.getBase() + "\n";
                    content += "Temp:" + " " + String.format("%.1f", convert(weatherResponses.getMain().getTemp())) + "C " + "\n";
                    content += "Temp odczuwalna" + " " + String.format("%.1f", convert(weatherResponses.getMain().getFeelsLike())) + "C" + "\n";
                    content += "Ci??nienie:" + " " + weatherResponses.getMain().getPressure() + "hPa" + "\n";
                    content += "Wilgotno????:" + " " + weatherResponses.getMain().getHumidity() + "g/m^3" + "\n";
//                    content += weatherResponses.getVisibility() + "\n";
//                    content += weatherResponses.getWind().getSpeed() + "\n";
//                    content += weatherResponses.getWind().getDeg() + "\n";
//                    content += weatherResponses.getClouds().getAll() + "\n";
//                    content += weatherResponses.getDt() + "\n";
//                    content += weatherResponses.getSys().getType() + "\n";
//                    content += weatherResponses.getSys().getId() + "\n";
                    content += "Kraj:" + " " + weatherResponses.getSys().getCountry() + "\n";
//                    content += weatherResponses.getSys().getSunrise() + "\n";
//                    content += weatherResponses.getSys().getSunset() + "\n";
//                    content += "Strefa czasowa: " + " " + weatherResponses.getTimezone() +  "\n";
//                    content += weatherResponses.getId() + "\n";
//                    content += weatherResponses.getName() + "\n";
//                    content += weatherResponses.getCod() + "\n";
                    textView.setText(content);
                    // cloud.setText(content);
                    Log.d("test", "cordinated setted succesfully" + content);
                }
            }
            private double convert(Double temp) {
                temp = temp - 273.15;
                return temp;
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textView.setText(t.getMessage());
                Log.e("test",  "Nie udalo sie pobrac pogody", t);

            }
        });

    }

    private CotEvent createPoint(double lat, double lon) {
        CotPoint cotPoint = new CotPoint(lat, lon, 0.0, 2.0, 2.0);
        CotEvent cotEvent = new CotEvent();
        CoordinatedTime time = new CoordinatedTime();

        cotEvent.setTime(time);
        cotEvent.setStart(time);
        cotEvent.setHow("h-e");
        cotEvent.setStale(time.addMinutes(10));
        cotEvent.setType("a-f-G-U-C-I");
        cotEvent.setPoint(cotPoint);
        return  cotEvent;


    }

    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    /**************************** INHERITED METHODS *****************************/
    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals(SHOW_PLUGIN)) {

            Log.d(TAG, "showing plugin drop down");
            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, false, this);
        }
    }

    @Override
    public void onDropDownSelectionRemoved() {
    }

    @Override
    public void onDropDownVisible(boolean v) {
    }

    @Override
    public void onDropDownSizeChanged(double width, double height) {
    }

    @Override
    public void onDropDownClose() {
    }

    private String getMenu() {
        return PluginMenuParser.getMenu(pluginContext, "menu.xml");
    }

}

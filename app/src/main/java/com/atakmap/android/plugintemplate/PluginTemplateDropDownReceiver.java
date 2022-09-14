
package com.atakmap.android.plugintemplate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atakmap.android.cot.CotMapComponent;
import com.atakmap.android.drawing.mapItems.DrawingRectangle;
import com.atakmap.android.drawing.mapItems.DrawingShape;
import com.atakmap.android.importexport.CotEventFactory;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapGroup;
import com.atakmap.android.maps.Marker;
import com.atakmap.android.maps.MultiPolyline;
import com.atakmap.android.menu.PluginMenuParser;
import com.atakmap.coremap.cot.event.CotEvent;
import com.atakmap.coremap.maps.coords.GeoPoint;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;


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

import opencsv.CSVReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PluginTemplateDropDownReceiver extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    public static final String SHOW_PLUGIN = "com.atakmap.android.plugintemplate.SHOW_PLUGIN";
    public View templateView;

    private TextView textView;

    private final Context pluginContext;

    /**************************** CONSTRUCTOR *****************************/

    public PluginTemplateDropDownReceiver(final MapView mapView,
                                          final Context context) {
        super(mapView);
        this.pluginContext = context;


        // Remember to use the PluginLayoutInflator if you are actually inflating a custom view
        // In this case, using it is not necessary - but I am putting it here to remind
        // developers to look at this Inflator

        templateView = PluginLayoutInflater.inflate(context,
                R.layout.fragment_plugin_main, null);

        TextView myText = (TextView) templateView.findViewById(R.id.load_weather_data);

        textView = templateView.findViewById(R.id.load_weather_data);

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {

            private void toast(String str) {
                Toast.makeText(getMapView().getContext(), str,
                        Toast.LENGTH_LONG).show();
            }
            public boolean onLongClick(View v) {
            int id = v.getId();
                if (id == R.id.enter_coordinates) {
                    toast(context.getString(R.string.enter_coordinates));
                }else if (id == R.id.enter_lat) {
                    toast(context.getString(R.string.enter_lat));
                }else if (id == R.id.enter_your_lat) {
                    toast(context.getString(R.string.enter_your_lat));
                }else if(id == R.id.enter_lon){
                    toast(context.getString(R.string.enter_lon));
                }else if(id == R.id.enter_your_lon){
                    toast(context.getString(R.string.enter_your_lon));
                }else if(id == R.id.confirm){
                    toast(context.getString(R.string.confirm));
                }else if (id == R.id.load_weather_data){
                    toast(context.getString(R.string.load_weather_data));
                }
                return true;
            }
        };

        final Button confirmCoordinates = templateView
                .findViewById(R.id.confirm);
        confirmCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

    }

    public void confirm(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApi jsonPlaceHolderApi = retrofit.create(JsonApi.class);


        templateView.findViewById(R.id.enter_your_lat);

         EditText parLan =templateView.findViewById(R.id.enter_your_lat);
         EditText parLon = templateView.findViewById(R.id.enter_your_lon);

         double cordLan = Double.parseDouble(parLan.getText().toString());
         Log.d("test", "cord Lan setted properly" + parLan.getText().toString());
         double cordLon = Double.parseDouble(parLon.getText().toString());
         Log.d("test", "cord Lon setted properly" + parLon.getText().toString());





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
//                    content +="Szerokość geo:"+ " "+ weatherResponses.getCoord().getLat() +"\n";
//                    content +="Długość geo:" + " " + weatherResponses.getCoord().getLon() + "\n";
//                    content += weather.getId() + "\n";
//                    content += weather.getMain() + "\n";
//                    content += weather.getDescription() + "\n";
//                    content += weather.getIcon() + "\n";
//                    content += weatherResponses.getBase() + "\n";
                      content +="Temp:"  + " " + String.format("%.1f", convert(weatherResponses.getMain().getTemp())) + "C " + "\n";
                      content +="Temp odczuwalna" + " " + String.format("%.1f", convert(weatherResponses.getMain().getFeelsLike()))+ "C" + "\n";
//                    content +="Temp min:" + " " + weatherResponses.getMain().getTempMin() + "K " +"\n";
//                      content +="Temp max:" + " " + String.format("%.1f", convert(weatherResponses.getMain().getTempMax())) + " C"+ "\n";
                      content +="Ciśnienie:" + " " + weatherResponses.getMain().getPressure() + "hPa"+"\n";
                      content +="Wilgotność:" + " " + weatherResponses.getMain().getHumidity() + "g/m^3"+"\n";
//                    content += weatherResponses.getVisibility() + "\n";
//                    content += weatherResponses.getWind().getSpeed() + "\n";
//                    content += weatherResponses.getWind().getDeg() + "\n";
//                    content += weatherResponses.getClouds().getAll() + "\n";
//                    content += weatherResponses.getDt() + "\n";
//                    content += weatherResponses.getSys().getType() + "\n";
//                    content += weatherResponses.getSys().getId() + "\n";
//                    content += "Kraj:" + " " + weatherResponses.getSys().getCountry() + "\n";
//                    content += weatherResponses.getSys().getSunrise() + "\n";
//                    content += weatherResponses.getSys().getSunset() + "\n";
//                    content += "Strefa czasowa: " + " " + weatherResponses.getTimezone() +  "\n";
//                    content += weatherResponses.getId() + "\n";
//                    content += weatherResponses.getName() + "\n";
//                    content += weatherResponses.getCod() + "\n";
                    textView.setText(content);
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
                Log.d("test",  t.getMessage() + "Error read data");

            }
        });


    }
    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    public void onClick() {

    }

    /**************************** INHERITED METHODS *****************************/
    public TextView csvreee;


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


package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
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

public class PluginTemplateDropDownReceiver extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    public static final String SHOW_PLUGIN = "com.atakmap.android.plugintemplate.SHOW_PLUGIN";
    private View templateView;
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

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {

            private void toast(String str) {
                Toast.makeText(getMapView().getContext(), str,
                        Toast.LENGTH_LONG).show();
            }
        public boolean onLongClick(View v) {


            int id = v.getId();
            if (id == R.id.addObject) {
                toast(context.getString(R.string.addObject));
            }else if (id == R.id.drawShapes) {
                toast(context.getString(R.string.drawShapes));
            };
            return true;
        }
    };

        final Button wheel = templateView
                .findViewById(R.id.addObject);
        wheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createUnit();

            }
        });
        wheel.setOnLongClickListener(longClickListener);

        final Button drawShapes = templateView
                .findViewById(R.id.drawShapes);
        drawShapes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawShapes();
            }
        });



    }

    /**************************** PUBLIC METHODS *****************************/


    public void disposeImpl() {
    }

    public void onClick(){

    };

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
//         templateView = templateView.findViewById(R.id.largerButton);
//        templateView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });



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

    public void createUnit() {



        Marker m = new Marker(getMapView().getPointWithElevation(), UUID
                .randomUUID().toString());
        Log.d(TAG, "creating a new unit marker for: " + m.getUID());

        m.setType("a-f-G-U-C-I");
        // m.setMetaBoolean("disableCoordinateOverlay", true); // used if you don't want the coordinate overlay to appear
        m.setMetaBoolean("readiness", true);
        m.setMetaBoolean("archive", true);
        m.setMetaString("how", "h-g-i-g-o");
        m.setMetaBoolean("editable", true);
        m.setMetaBoolean("movable", true);
        m.setMetaBoolean("removable", true);
        m.setMetaString("entry", "user");
        m.setMetaString("callsign", "Test Marker");
        m.setTitle("Test Marker");
        m.setMetaString("menu", getMenu());

        MapGroup _mapGroup = getMapView().getRootGroup()
                .findMapGroup("Cursor on Target")
                .findMapGroup("Friendly");
        _mapGroup.addItem(m);


        m.persist(getMapView().getMapEventDispatcher(), null,
                this.getClass());

        Intent new_cot_intent = new Intent();
        new_cot_intent.setAction("com.atakmap.android.maps.COT_PLACED");
        new_cot_intent.putExtra("uid", m.getUID());
        AtakBroadcast.getInstance().sendBroadcast(
                new_cot_intent);

    }

    private List<CsvData> csvData= new ArrayList<>();
    void readCsvFile() {
        InputStream in = pluginContext.getResources().openRawResource(R.raw.dane);
        BufferedReader reader =  new BufferedReader(
                new InputStreamReader(in, Charset.forName("UTF-8"))
        );

        String line = "";
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        try{
                while ( (line = reader.readLine()) != null){
                    String[] tokens = line.split(",");

                    CsvData data = new CsvData();
                    data.setId_stacji(Integer.parseInt(tokens[0]));
                    data.setStacja(tokens[1]);
                    data.setData_pomiaru(f.parse(tokens[2]));
                    data.setGodzina_pomiaru(Integer.parseInt(tokens[3]));
                    data.setTemperatura(Double.parseDouble(tokens[4]));
                    data.setPredkosc_wiatru(Integer.parseInt(tokens[5]));
                    data.setKierunek_wiatru(Integer.parseInt(tokens[6]));
                    data.setWilgotnosc_wzgledna(Double.parseDouble(tokens[7]));
                    data.setSuma_opadu(Double.parseDouble(tokens[8]));
                    data.setCisnienie(Double.parseDouble(tokens[9]));
                    csvData.add(data);

                    Log.d("Activity", "Has been created" + data);

                };
            } catch (IOException | ParseException e) {
                Log.wtf("Activity", "Error reading data" + line, e);
                e.printStackTrace();
            }

        }


    void drawShapes() {


        MapView mapView = getMapView();
        MapGroup group = mapView.getRootGroup().findMapGroup(
                "Drawing Objects");
        List<DrawingShape> dslist = new ArrayList<>();

        DrawingShape ds = new DrawingShape(mapView, "ds-1");
        ds.setStrokeColor(Color.RED);
        ds.setPoints(new GeoPoint[] {
                new GeoPoint(0, 0), new GeoPoint(1, 1), new GeoPoint(2, 1)
        });
        ds.setHeight(10);
        //group.addItem(ds);
        dslist.add(ds);
        // test to set closed after adding to a group
        ds.setClosed(true);

//        ds = new DrawingShape(mapView, "ds-2");
//        ds.setPoints(new GeoPoint[] {
//                new GeoPoint(0, 0), new GeoPoint(5, 5), new GeoPoint(-2, -1)
//        });
//        ds.setHeight(200);
//        ds.setClosed(true);
//        ds.setStrokeColor(Color.BLUE);
//
//        //group.addItem(ds);
//        dslist.add(ds);

        MultiPolyline mp = new MultiPolyline(mapView, group, dslist, "list-1");
        group.addItem(mp);
        mp.setMovable(false);
//        ds = new DrawingShape(mapView, "ds-3");
//        ds.setPoints(new GeoPoint[] {
//                new GeoPoint(0, 0), new GeoPoint(2, 0), new GeoPoint(2, -1)
//        });
//        ds.setClosed(true);
//        ds.setStrokeColor(Color.YELLOW);
//        ds.setHeight(300);

//        ds.setMovable(false);
//        group.addItem(ds);
//        ds = new DrawingShape(mapView, "ds-4");
//        ds.setPoints(new GeoPoint[] {
//                new GeoPoint(0, 0), new GeoPoint(-2, 0), new GeoPoint(-2, 1)
//        });
//        ds.setStrokeColor(Color.GREEN);
//        group.addItem(ds);
//        ds.setHeight(400);
//        ds.setMovable(false);
//        ds.setClosed(true);

    }


    private String getMenu() {
        return PluginMenuParser.getMenu(pluginContext, "menu.xml");
    }

}

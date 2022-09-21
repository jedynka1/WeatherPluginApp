
package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.atakmap.android.ipc.AtakBroadcast.DocumentedIntentFilter;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.dropdown.DropDownMapComponent;

//import com.atakmap.android.plugintemplate.plugin.JsonApi;
import com.atakmap.coremap.log.Log;
import com.atakmap.android.plugintemplate.plugin.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PluginTemplateMapComponent extends DropDownMapComponent {

    private static final String TAG = "PluginTemplateMapComponent";
    private TextView textView;

    private Context pluginContext;

    private PluginTemplateDropDownReceiver ddr;
    public void onCreate(final Context context, Intent intent,
            final MapView view) {


        context.setTheme(R.style.ATAKPluginTheme);
        super.onCreate(context, intent, view);
        pluginContext = context;






        ddr = new PluginTemplateDropDownReceiver(
                view, context);


        Log.d(TAG, "registering the plugin filter");
        DocumentedIntentFilter ddFilter = new DocumentedIntentFilter();
        ddFilter.addAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
        registerDropDownReceiver(ddr, ddFilter);
//
    }

    @Override
    protected void onDestroyImpl(Context context, MapView view) {
        super.onDestroyImpl(context, view);
    }

}


package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.atakmap.android.ipc.AtakBroadcast.DocumentedIntentFilter;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.dropdown.DropDownMapComponent;

//import com.atakmap.android.plugintemplate.plugin.JsonApi;
import com.atakmap.android.plugintemplate.plugin.JsonApi;
import com.atakmap.coremap.log.Log;
import com.atakmap.android.plugintemplate.plugin.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PluginTemplateMapComponent extends DropDownMapComponent {

    private static final String TAG = "PluginTemplateMapComponent";

    private Context pluginContext;

    private PluginTemplateDropDownReceiver ddr;

    public void onCreate(final Context context, Intent intent,
            final MapView view) {

        context.setTheme(R.style.ATAKPluginTheme);
        super.onCreate(context, intent, view);
        pluginContext = context;


        ddr.csvreee.findViewById(R.id.weatherData);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApi jsonPlaceHolderApi = retrofit.create(JsonApi.class);

        Call<List<Post>> call =  jsonPlaceHolderApi.getPost();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()){
                    ddr.csvreee.setText("Code: " + response.code());
                }

                List<Post> posts = response.body();

                for(Post post :posts){
                    String content = "";
                    content += "Id" + post.getId() + "\n";
                    content += "User id" + post.getUserId() + "\n";
                    content += "Title" + post.getTitle() + "\n";
                    content += "Text" + post.getText() + "\n\n";

                    ddr.csvreee.append(content);
                 }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                ddr.csvreee.setText(t.getMessage());
            }
        });



        ddr = new PluginTemplateDropDownReceiver(
                view, context);

        Log.d(TAG, "registering the plugin filter");
        DocumentedIntentFilter ddFilter = new DocumentedIntentFilter();
        ddFilter.addAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
        registerDropDownReceiver(ddr, ddFilter);

        ddr.readCsvFile();




    }

    @Override
    protected void onDestroyImpl(Context context, MapView view) {
        super.onDestroyImpl(context, view);
    }

}

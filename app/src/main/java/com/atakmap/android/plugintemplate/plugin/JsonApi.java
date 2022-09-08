package com.atakmap.android.plugintemplate.plugin;

import com.atakmap.android.plugintemplate.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonApi {
    @GET("posts")
    Call<List<Post>> getPost();
}

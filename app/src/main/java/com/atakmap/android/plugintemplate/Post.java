package com.atakmap.android.plugintemplate;

import com.google.gson.annotations.SerializedName;

public class Post {
   private int userId;
   private int id;
   private String title;

   public int getUserId() {
      return userId;
   }

   public int getId() {
      return id;
   }

   public String getTitle() {
      return title;
   }

   public String getText() {
      return text;
   }

   @SerializedName("body")
   private String text;
}

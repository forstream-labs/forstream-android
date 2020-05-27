package io.forstream.api.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

  private static final Gson GSON = new GsonBuilder()
    .setDateFormat(DATE_FORMAT)
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .create();

  private JsonUtils() {

  }

  public static Gson getGson() {
    return GSON;
  }

  public static String toJson(Object obj) {
    return GSON.toJson(obj);
  }
}

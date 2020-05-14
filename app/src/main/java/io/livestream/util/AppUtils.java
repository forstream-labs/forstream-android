package io.livestream.util;

import android.content.Context;

import io.livestream.api.enums.StreamStatus;

public class AppUtils {

  private AppUtils() {

  }

  public static String getStreamStatusName(Context context, StreamStatus streamStatus) {
    return getString(context, "stream_status_" + streamStatus.name().toLowerCase());
  }

  private static String getString(Context context, String key) {
    int resourceId = context.getResources().getIdentifier(key, "string", context.getPackageName());
    if (resourceId > 0) {
      return context.getString(resourceId);
    }
    return null;
  }
}

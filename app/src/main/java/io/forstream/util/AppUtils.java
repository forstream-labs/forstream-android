package io.forstream.util;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.pedro.rtplibrary.rtmp.RtmpCamera2;

import io.forstream.R;
import io.forstream.api.enums.StreamStatus;
import io.forstream.api.model.Channel;
import io.forstream.api.model.ChannelAlert;
import io.forstream.api.model.ProviderMessage;

public class AppUtils {

  private AppUtils() {

  }

  public static String getStreamStatusName(Context context, StreamStatus streamStatus, RtmpCamera2 rtmpCamera) {
    return getString(context, "stream_status_" + (streamStatus.equals(StreamStatus.LIVE) && rtmpCamera != null && !rtmpCamera.isStreaming() ? "paused" : streamStatus.name().toLowerCase()));
  }

  public static int getStreamStatusColor(Context context, StreamStatus streamStatus, RtmpCamera2 rtmpCamera) {
    int color = 0;
    switch (streamStatus) {
      case READY:
        color = ContextCompat.getColor(context, R.color.stream_status_ready);
        break;
      case LIVE:
        color = ContextCompat.getColor(context, rtmpCamera != null && !rtmpCamera.isStreaming() ? R.color.stream_status_paused : R.color.stream_status_live);
        break;
      case ENDED:
        color = ContextCompat.getColor(context, R.color.stream_status_complete);
        break;
    }
    return color;
  }

  public static String getString(Context context, String key) {
    int resourceId = context.getResources().getIdentifier(key, "string", context.getPackageName());
    if (resourceId > 0) {
      return context.getString(resourceId);
    }
    return null;
  }

  public static String getChannelAlertString(Context context, Channel channel, ChannelAlert channelAlert) {
    return getString(context, "channel_alert_" + channel.getIdentifier().toString().toLowerCase() + "_" + channelAlert.getId());
  }

  public static String getProviderStreamMessageString(Context context, Channel channel, ProviderMessage providerMessage) {
    return getString(context, "provider_stream_message_" + channel.getIdentifier().toString().toLowerCase() + "_" + providerMessage.getCode());
  }
}

package io.forstream.api.enums;

import com.google.gson.annotations.SerializedName;

public enum ChannelIdentifier {

  @SerializedName("youtube")
  YOUTUBE,
  @SerializedName("facebook")
  FACEBOOK,
  @SerializedName("facebook_page")
  FACEBOOK_PAGE,
  @SerializedName("twitch")
  TWITCH,
  @SerializedName("rtmp")
  RTMP

}

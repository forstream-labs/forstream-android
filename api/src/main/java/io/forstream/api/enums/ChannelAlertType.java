package io.forstream.api.enums;

import com.google.gson.annotations.SerializedName;

public enum ChannelAlertType {

  @SerializedName("info")
  INFO,
  @SerializedName("warning")
  WARNING,
  @SerializedName("error")
  ERROR

}

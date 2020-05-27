package io.forstream.api.enums;

import com.google.gson.annotations.SerializedName;

public enum StreamStatus {

  @SerializedName("error")
  ERROR,
  @SerializedName("ready")
  READY,
  @SerializedName("live")
  LIVE,
  @SerializedName("complete")
  COMPLETE

}

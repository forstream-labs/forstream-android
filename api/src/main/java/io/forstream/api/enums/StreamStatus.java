package io.forstream.api.enums;

import com.google.gson.annotations.SerializedName;

public enum StreamStatus {

  @SerializedName("error")
  ERROR,
  @SerializedName("ready")
  READY,
  @SerializedName("error_starting")
  ERROR_STARTING,
  @SerializedName("live")
  LIVE,
  @SerializedName("ended")
  ENDED

}

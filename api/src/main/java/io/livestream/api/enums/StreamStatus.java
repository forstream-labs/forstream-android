package io.livestream.api.enums;

import com.google.gson.annotations.SerializedName;

public enum StreamStatus {

  @SerializedName("ready")
  READY,
  @SerializedName("live")
  LIVE,
  @SerializedName("complete")
  COMPLETE

}

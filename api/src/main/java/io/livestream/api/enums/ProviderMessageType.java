package io.livestream.api.enums;

import com.google.gson.annotations.SerializedName;

public enum ProviderMessageType {

  @SerializedName("error")
  ERROR,
  @SerializedName("warning")
  WARNING

}

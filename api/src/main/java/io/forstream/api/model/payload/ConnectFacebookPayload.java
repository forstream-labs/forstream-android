package io.forstream.api.model.payload;

public class ConnectFacebookPayload {

  private String accessToken;
  private String targetId;

  public ConnectFacebookPayload(String accessToken, String targetId) {
    this.accessToken = accessToken;
    this.targetId = targetId;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getTargetId() {
    return targetId;
  }
}

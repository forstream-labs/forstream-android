package io.forstream.api.model.payload;

public class AccessTokenPayload {

  private String accessToken;

  public AccessTokenPayload(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
}

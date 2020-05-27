package io.forstream.api.model.payload;

public class AuthCodePayload {

  private String authCode;

  public AuthCodePayload(String authCode) {
    this.authCode = authCode;
  }

  public String getAuthCode() {
    return authCode;
  }

  public void setAuthCode(String authCode) {
    this.authCode = authCode;
  }
}

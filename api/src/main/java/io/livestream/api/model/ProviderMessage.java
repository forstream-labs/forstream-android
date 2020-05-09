package io.livestream.api.model;

import java.io.Serializable;

import io.livestream.api.enums.ProviderMessageType;

public class ProviderMessage implements Serializable {

  private static final long serialVersionUID = -7578867643434135781L;

  private ProviderMessageType type;
  private String code;
  private String message;

  public ProviderMessageType getType() {
    return type;
  }

  public void setType(ProviderMessageType type) {
    this.type = type;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

package io.forstream.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.forstream.api.enums.ChannelIdentifier;

public class Channel extends Entity implements Serializable {

  private static final long serialVersionUID = -1620185166808396363L;

  private String name;
  private ChannelIdentifier identifier;
  private String imageUrl;
  private List<String> requiredScopes;
  private Date registrationDate;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ChannelIdentifier getIdentifier() {
    return identifier;
  }

  public void setIdentifier(ChannelIdentifier identifier) {
    this.identifier = identifier;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public List<String> getRequiredScopes() {
    return requiredScopes;
  }

  public void setRequiredScopes(List<String> requiredScopes) {
    this.requiredScopes = requiredScopes;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }
}

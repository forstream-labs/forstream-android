package io.forstream.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.forstream.api.enums.ChannelIdentifier;

public class Channel extends Entity implements Serializable {

  private static final long serialVersionUID = -1620185166808396363L;

  private ChannelIdentifier identifier;
  private String name;
  private String imageUrl;
  private List<String> requiredScopes;
  private boolean comingSoon;
  private Date registrationDate;

  public ChannelIdentifier getIdentifier() {
    return identifier;
  }

  public void setIdentifier(ChannelIdentifier identifier) {
    this.identifier = identifier;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public boolean isComingSoon() {
    return comingSoon;
  }

  public void setComingSoon(boolean comingSoon) {
    this.comingSoon = comingSoon;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }
}

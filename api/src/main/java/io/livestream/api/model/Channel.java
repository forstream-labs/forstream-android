package io.livestream.api.model;

import java.io.Serializable;
import java.util.Date;

import io.livestream.api.enums.ChannelIdentifier;

public class Channel extends Entity implements Serializable {

  private static final long serialVersionUID = -1620185166808396363L;

  private String name;
  private ChannelIdentifier identifier;
  private String imageUrl;
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

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }
}

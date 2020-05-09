package io.livestream.api.model;

import java.io.Serializable;
import java.util.Date;

public class ConnectedChannel extends Entity implements Serializable {

  private static final long serialVersionUID = 5760712247261320132L;

  private User user;
  private Channel channel;
  private String targetId;
  private Boolean enabled;
  private Date registrationDate;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Channel getChannel() {
    return channel;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }
}
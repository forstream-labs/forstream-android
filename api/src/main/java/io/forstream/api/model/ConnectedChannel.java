package io.forstream.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ConnectedChannel extends Entity implements Serializable {

  private static final long serialVersionUID = 5760712247261320132L;

  private User user;
  private Channel channel;
  private ChannelTarget target;
  private List<ChannelAlert> alerts;
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

  public ChannelTarget getTarget() {
    return target;
  }

  public void setTarget(ChannelTarget target) {
    this.target = target;
  }

  public List<ChannelAlert> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<ChannelAlert> alerts) {
    this.alerts = alerts;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }
}

package io.livestream.api.model;

import java.util.Date;
import java.util.List;

public class LiveStream extends Entity {

  private static final long serialVersionUID = -1626500447999436237L;

  private User owner;
  private String title;
  private String description;
  private List<StreamProvider> streamProviders;
  private Date startDate;
  private Date registrationDate;

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<StreamProvider> getStreamProviders() {
    return streamProviders;
  }

  public void setStreamProviders(List<StreamProvider> streamProviders) {
    this.streamProviders = streamProviders;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }
}

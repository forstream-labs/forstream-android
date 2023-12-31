package io.forstream.api.model;

import java.util.Date;
import java.util.List;

import io.forstream.api.enums.StreamStatus;

public class LiveStream extends Entity {

  private static final long serialVersionUID = -1626500447999436237L;

  private User owner;
  private String title;
  private String description;
  private String streamKey;
  private String streamUrl;
  private StreamStatus streamStatus;
  private List<ProviderStream> providers;
  private Date startDate;
  private Date endDate;
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

  public String getStreamKey() {
    return streamKey;
  }

  public void setStreamKey(String streamKey) {
    this.streamKey = streamKey;
  }

  public String getStreamUrl() {
    return streamUrl;
  }

  public void setStreamUrl(String streamUrl) {
    this.streamUrl = streamUrl;
  }

  public StreamStatus getStreamStatus() {
    return streamStatus;
  }

  public void setStreamStatus(StreamStatus streamStatus) {
    this.streamStatus = streamStatus;
  }

  public List<ProviderStream> getProviders() {
    return providers;
  }

  public void setProviders(List<ProviderStream> providers) {
    this.providers = providers;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }
}

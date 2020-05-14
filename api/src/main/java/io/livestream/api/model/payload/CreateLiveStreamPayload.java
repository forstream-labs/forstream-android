package io.livestream.api.model.payload;

import java.util.List;

import io.livestream.api.enums.ChannelIdentifier;

public class CreateLiveStreamPayload {

  private String title;
  private String description;
  private List<ChannelIdentifier> channels;

  public CreateLiveStreamPayload(String title, String description, List<ChannelIdentifier> channels) {
    this.title = title;
    this.description = description;
    this.channels = channels;
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

  public List<ChannelIdentifier> getChannels() {
    return channels;
  }

  public void setChannels(List<ChannelIdentifier> channels) {
    this.channels = channels;
  }
}

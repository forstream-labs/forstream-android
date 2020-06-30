package io.forstream.api.model.payload;

import java.util.List;

import io.forstream.api.enums.ChannelIdentifier;

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

  public String getDescription() {
    return description;
  }

  public List<ChannelIdentifier> getChannels() {
    return channels;
  }
}

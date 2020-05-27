package io.forstream.api.model;

import java.io.Serializable;
import java.util.List;

import io.forstream.api.enums.StreamStatus;

public class ProviderStream extends Entity implements Serializable {

  private static final long serialVersionUID = -429385756607589770L;

  private Channel channel;
  private ConnectedChannel connectedChannel;
  private Boolean enabled;
  private String broadcastId;
  private String streamUrl;
  private StreamStatus streamStatus;
  private List<ProviderMessage> messages;

  public Channel getChannel() {
    return channel;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  public ConnectedChannel getConnectedChannel() {
    return connectedChannel;
  }

  public void setConnectedChannel(ConnectedChannel connectedChannel) {
    this.connectedChannel = connectedChannel;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String getBroadcastId() {
    return broadcastId;
  }

  public void setBroadcastId(String broadcastId) {
    this.broadcastId = broadcastId;
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

  public List<ProviderMessage> getMessages() {
    return messages;
  }

  public void setMessages(List<ProviderMessage> messages) {
    this.messages = messages;
  }
}

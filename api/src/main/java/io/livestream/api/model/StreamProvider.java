package io.livestream.api.model;

import io.livestream.api.enums.StreamStatus;

public class StreamProvider extends Entity {

  private static final long serialVersionUID = -429385756607589770L;

  private ConnectedChannel connectedChannel;
  private String broadcastId;
  private String streamUrl;
  private StreamStatus streamStatus;

  public ConnectedChannel getConnectedChannel() {
    return connectedChannel;
  }

  public void setConnectedChannel(ConnectedChannel connectedChannel) {
    this.connectedChannel = connectedChannel;
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
}

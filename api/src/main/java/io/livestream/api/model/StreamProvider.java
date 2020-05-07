package io.livestream.api.model;

public class StreamProvider extends Entity {

  private static final long serialVersionUID = -429385756607589770L;

  private ConnectedChannel connectedChannel;
  private String broadcastId;
  private String streamId;
  private String streamName;
  private String ingestionAddress;

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

  public String getStreamId() {
    return streamId;
  }

  public void setStreamId(String streamId) {
    this.streamId = streamId;
  }

  public String getStreamName() {
    return streamName;
  }

  public void setStreamName(String streamName) {
    this.streamName = streamName;
  }

  public String getIngestionAddress() {
    return ingestionAddress;
  }

  public void setIngestionAddress(String ingestionAddress) {
    this.ingestionAddress = ingestionAddress;
  }
}

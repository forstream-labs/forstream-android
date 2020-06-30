package io.forstream.api.model.payload;

public class ConnectRtmpChannelPayload {

  private final String channelName;
  private final String rtmpUrl;
  private final String streamKey;

  public ConnectRtmpChannelPayload(String channelName, String rtmpUrl, String streamKey) {
    this.channelName = channelName;
    this.rtmpUrl = rtmpUrl;
    this.streamKey = streamKey;
  }

  public String getChannelName() {
    return channelName;
  }

  public String getRtmpUrl() {
    return rtmpUrl;
  }

  public String getStreamKey() {
    return streamKey;
  }
}

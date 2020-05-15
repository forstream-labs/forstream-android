package io.livestream.api.service;

import android.content.Context;

import com.onehilltech.promises.Promise;

import java.util.List;

import io.livestream.api.enums.ChannelIdentifier;
import io.livestream.api.model.LiveStream;
import io.livestream.api.model.payload.CreateLiveStreamPayload;
import io.livestream.api.service.api.StreamApi;
import io.livestream.api.util.PromiseUtils;

public class StreamService {

  private StreamApi api;

  public StreamService(Context context) {
    this.api = ApiFactory.build(StreamApi.class, context);
  }

  public Promise<LiveStream> getLiveStream(String id, String populate) {
    return PromiseUtils.build(api.getLiveStream(id, populate));
  }

  public Promise<LiveStream> createLiveStream(String title, String description, List<ChannelIdentifier> channelsIdentifiers) {
    return PromiseUtils.build(api.createLiveStream(new CreateLiveStreamPayload(title, description, channelsIdentifiers)));
  }

  public Promise<LiveStream> startLiveStream(LiveStream liveStream) {
    return PromiseUtils.build(api.startLiveStream(liveStream.getId()));
  }

  public Promise<LiveStream> endLiveStream(LiveStream liveStream) {
    return PromiseUtils.build(api.endLiveStream(liveStream.getId()));
  }
}

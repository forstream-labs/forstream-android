package io.forstream.api.service;

import android.content.Context;

import com.onehilltech.promises.Promise;

import java.util.List;

import io.forstream.api.enums.ChannelIdentifier;
import io.forstream.api.model.LiveStream;
import io.forstream.api.model.ProviderStream;
import io.forstream.api.model.payload.CreateLiveStreamPayload;
import io.forstream.api.service.api.StreamApi;
import io.forstream.api.util.PromiseUtils;

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

  public Promise<LiveStream> removeLiveStream(LiveStream liveStream) {
    return PromiseUtils.build(api.removeLiveStream(liveStream.getId()));
  }

  public Promise<LiveStream> startLiveStream(LiveStream liveStream) {
    return PromiseUtils.build(api.startLiveStream(liveStream.getId()));
  }

  public Promise<LiveStream> endLiveStream(LiveStream liveStream) {
    return PromiseUtils.build(api.endLiveStream(liveStream.getId()));
  }

  public Promise<LiveStream> enableLiveStreamProvider(LiveStream liveStream, ProviderStream providerStream) {
    return PromiseUtils.build(api.enableLiveStreamProvider(liveStream.getId(), providerStream.getConnectedChannel().getChannel().getIdentifier()));
  }

  public Promise<LiveStream> disableLiveStreamProvider(LiveStream liveStream, ProviderStream providerStream) {
    return PromiseUtils.build(api.disableLiveStreamProvider(liveStream.getId(), providerStream.getConnectedChannel().getChannel().getIdentifier()));
  }
}

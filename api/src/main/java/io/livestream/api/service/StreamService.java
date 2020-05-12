package io.livestream.api.service;

import android.content.Context;

import com.onehilltech.promises.Promise;

import io.livestream.api.model.LiveStream;
import io.livestream.api.service.api.StreamApi;
import io.livestream.api.util.PromiseUtils;

public class StreamService {

  private StreamApi api;

  public StreamService(Context context) {
    this.api = ApiFactory.build(StreamApi.class, context);
  }

  public Promise<LiveStream> createLiveStream() {
    return PromiseUtils.build(api.createLiveStream());
  }

  public Promise<LiveStream> startLiveStream(LiveStream liveStream) {
    return PromiseUtils.build(api.startLiveStream(liveStream.getId()));
  }

  public Promise<LiveStream> endLiveStream(LiveStream liveStream) {
    return PromiseUtils.build(api.endLiveStream(liveStream.getId()));
  }
}

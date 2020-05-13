package io.livestream.api.service.api;

import io.livestream.api.model.LiveStream;
import io.livestream.api.model.payload.CreateLiveStreamPayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StreamApi {

  @POST("streams")
  Call<LiveStream> createLiveStream(@Body CreateLiveStreamPayload payload);

  @POST("streams/{live_stream}/start")
  Call<LiveStream> startLiveStream(@Path("live_stream") String liveStreamId);

  @POST("streams/{live_stream}/end")
  Call<LiveStream> endLiveStream(@Path("live_stream") String liveStreamId);

}

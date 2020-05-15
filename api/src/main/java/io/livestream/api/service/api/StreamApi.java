package io.livestream.api.service.api;

import io.livestream.api.model.LiveStream;
import io.livestream.api.model.payload.CreateLiveStreamPayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StreamApi {

  @POST("streams")
  Call<LiveStream> createLiveStream(@Body CreateLiveStreamPayload payload);

  @GET("streams/{live_stream}")
  Call<LiveStream> getLiveStream(@Path("live_stream") String liveStreamId, @Query("populate") String populate);

  @POST("streams/{live_stream}/start")
  Call<LiveStream> startLiveStream(@Path("live_stream") String liveStreamId);

  @POST("streams/{live_stream}/end")
  Call<LiveStream> endLiveStream(@Path("live_stream") String liveStreamId);

}

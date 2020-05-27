package io.forstream.api.service.api;

import io.forstream.api.enums.ChannelIdentifier;
import io.forstream.api.model.LiveStream;
import io.forstream.api.model.payload.CreateLiveStreamPayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StreamApi {

  @POST("streams")
  Call<LiveStream> createLiveStream(@Body CreateLiveStreamPayload payload);

  @GET("streams/{live_stream}")
  Call<LiveStream> getLiveStream(@Path("live_stream") String liveStreamId, @Query("populate") String populate);

  @DELETE("streams/{live_stream}")
  Call<LiveStream> removeLiveStream(@Path("live_stream") String liveStreamId);

  @POST("streams/{live_stream}/start")
  Call<LiveStream> startLiveStream(@Path("live_stream") String liveStreamId);

  @POST("streams/{live_stream}/end")
  Call<LiveStream> endLiveStream(@Path("live_stream") String liveStreamId);

  @POST("streams/{live_stream}/providers/{provider}/enable")
  Call<LiveStream> enableLiveStreamProvider(@Path("live_stream") String liveStreamId, @Path("provider") ChannelIdentifier channelIdentifier);

  @POST("streams/{live_stream}/providers/{provider}/disable")
  Call<LiveStream> disableLiveStreamProvider(@Path("live_stream") String liveStreamId, @Path("provider") ChannelIdentifier channelIdentifier);

}

package io.forstream.api.service.api;

import java.util.List;

import io.forstream.api.enums.ChannelIdentifier;
import io.forstream.api.model.Channel;
import io.forstream.api.model.ChannelTarget;
import io.forstream.api.model.ConnectedChannel;
import io.forstream.api.model.payload.AuthCodePayload;
import io.forstream.api.model.payload.ConnectFacebookPayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChannelApi {

  @GET("channels")
  Call<List<Channel>> listChannels();

  @POST("channels/youtube/connect")
  Call<ConnectedChannel> connectYouTubeChannel(@Body AuthCodePayload payload);

  @GET("channels/facebook/targets")
  Call<List<ChannelTarget>> listFacebookChannelTargets(@Query("access_token") String accessToken);

  @POST("channels/facebook/connect")
  Call<ConnectedChannel> connectFacebookChannel(@Body ConnectFacebookPayload payload);

  @POST("channels/{channel}/disconnect")
  Call<Void> disconnectChannel(@Path("channel") ChannelIdentifier channelIdentifier);

  @GET("connected_channels/{connected_channel}")
  Call<ConnectedChannel> getConnectedChannel(@Path("connected_channel") String id, @Query("populate") String populate);

}

package io.forstream.api.service.api;

import java.util.List;

import io.forstream.api.enums.ChannelIdentifier;
import io.forstream.api.model.Channel;
import io.forstream.api.model.ChannelTarget;
import io.forstream.api.model.ConnectedChannel;
import io.forstream.api.model.payload.AuthCodePayload;
import io.forstream.api.model.payload.ConnectFacebookPayload;
import io.forstream.api.model.payload.ConnectRtmpChannelPayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChannelApi {

  @GET("v1/channels")
  Call<List<Channel>> listChannels();

  @POST("v1/channels/youtube/connect")
  Call<ConnectedChannel> connectYouTubeChannel(@Body AuthCodePayload payload);

  @POST("v1/channels/facebook/connect")
  Call<ConnectedChannel> connectFacebookChannel(@Body ConnectFacebookPayload payload);

  @GET("v1/channels/facebook_page/targets")
  Call<List<ChannelTarget>> listFacebookPageChannelTargets(@Header("Access-Token") String accessToken);

  @POST("v1/channels/facebook_page/connect")
  Call<ConnectedChannel> connectFacebookPageChannel(@Body ConnectFacebookPayload payload);

  @POST("v1/channels/twitch/connect")
  Call<ConnectedChannel> connectTwitchChannel(@Body AuthCodePayload payload);

  @POST("v1/channels/rtmp/connect")
  Call<ConnectedChannel> connectRtmpChannel(@Body ConnectRtmpChannelPayload payload);

  @POST("v1/channels/{channel}/disconnect")
  Call<Void> disconnectChannel(@Path("channel") ChannelIdentifier channelIdentifier);

  @GET("v1/connected_channels/{connected_channel}")
  Call<ConnectedChannel> getConnectedChannel(@Path("connected_channel") String id, @Query("populate") String populate);

  @POST("v1/connected_channels/{connected_channel}/alerts/{alert}/check")
  Call<ConnectedChannel> checkConnectedChannelAlert(@Path("connected_channel") String id, @Path("alert") String alertId);
}

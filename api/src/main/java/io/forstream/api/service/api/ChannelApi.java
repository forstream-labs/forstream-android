package io.forstream.api.service.api;

import java.util.List;

import io.forstream.api.enums.ChannelIdentifier;
import io.forstream.api.model.payload.AuthCodePayload;
import io.forstream.api.model.Channel;
import io.forstream.api.model.ConnectedChannel;
import io.forstream.api.model.payload.AccessTokenPayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChannelApi {

  @GET("channels")
  Call<List<Channel>> listChannels();

  @POST("channels/youtube/connect")
  Call<ConnectedChannel> connectYouTubeChannel(@Body AuthCodePayload payload);

  @POST("channels/facebook/connect")
  Call<ConnectedChannel> connectFacebookChannel(@Body AccessTokenPayload payload);

  @POST("channels/{channel}/disconnect")
  Call<Void> disconnectChannel(@Path("channel") ChannelIdentifier channelIdentifier);

}

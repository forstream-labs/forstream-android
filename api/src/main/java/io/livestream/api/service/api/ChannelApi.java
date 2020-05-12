package io.livestream.api.service.api;

import java.util.List;

import io.livestream.api.model.Channel;
import io.livestream.api.model.ConnectedChannel;
import io.livestream.api.model.payload.AccessTokenPayload;
import io.livestream.api.model.payload.AuthCodePayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ChannelApi {

  @GET("channels")
  Call<List<Channel>> listChannels();

  @POST("channels/youtube/connect")
  Call<ConnectedChannel> connectYouTubeChannel(@Body AuthCodePayload payload);

  @POST("channels/facebook/connect")
  Call<ConnectedChannel> connectFacebookChannel(@Body AccessTokenPayload payload);

}

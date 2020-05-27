package io.forstream.api.service;

import android.content.Context;

import com.onehilltech.promises.Promise;

import java.util.List;

import io.forstream.api.model.payload.AuthCodePayload;
import io.forstream.api.model.Channel;
import io.forstream.api.model.ConnectedChannel;
import io.forstream.api.model.payload.AccessTokenPayload;
import io.forstream.api.service.api.ChannelApi;
import io.forstream.api.util.PromiseUtils;

public class ChannelService {

  private ChannelApi api;

  public ChannelService(Context context) {
    this.api = ApiFactory.build(ChannelApi.class, context);
  }

  public Promise<List<Channel>> listChannels() {
    return PromiseUtils.build(api.listChannels());
  }

  public Promise<ConnectedChannel> connectYouTubeChannel(String authCode) {
    return PromiseUtils.build(api.connectYouTubeChannel(new AuthCodePayload(authCode)));
  }

  public Promise<ConnectedChannel> connectFacebookChannel(String accessToken) {
    return PromiseUtils.build(api.connectFacebookChannel(new AccessTokenPayload(accessToken)));
  }

  public Promise<Void> disconnectChannel(Channel channel) {
    return PromiseUtils.build(api.disconnectChannel(channel.getIdentifier()));
  }
}

package io.livestream.api.service;

import android.content.Context;

import com.onehilltech.promises.Promise;

import io.livestream.api.model.ConnectedChannel;
import io.livestream.api.model.LiveStream;
import io.livestream.api.model.User;
import io.livestream.api.model.payload.AccessTokenPayload;
import io.livestream.api.model.payload.AuthCodePayload;
import io.livestream.api.service.api.UserApi;
import io.livestream.api.util.PromiseUtils;

import static com.onehilltech.promises.Promise.value;

public class UserService {

  private Context context;
  private UserApi api;

  public UserService(Context context) {
    this.context = context;
    this.api = ApiFactory.build(UserApi.class, context);
  }

  public Promise<User> signInWithGoogle(String authCode) {
    return PromiseUtils.build(api.signInWithGoogle(new AuthCodePayload(authCode))).then(result -> {
      TokenManager.setToken(context, result.getToken());
      return value(result.getUser());
    });
  }

  public Promise<User> signInWithFacebook(String accessToken) {
    return PromiseUtils.build(api.signInWithFacebook(new AccessTokenPayload(accessToken))).then(result -> {
      TokenManager.setToken(context, result.getToken());
      return value(result.getUser());
    });
  }

  public Promise<Void> signOut() {
    return PromiseUtils.build(api.signOut()).then(result -> {
      TokenManager.setToken(context, null);
      return value(null);
    });
  }

  public Promise<ConnectedChannel> connectYouTubeChannel(String authCode) {
    return PromiseUtils.build(api.connectYouTubeChannel(new AuthCodePayload(authCode)));
  }

  public Promise<ConnectedChannel> connectFacebookChannel(String accessToken) {
    return PromiseUtils.build(api.connectFacebookChannel(new AccessTokenPayload(accessToken)));
  }

  public Promise<LiveStream> createLiveStream() {
    return PromiseUtils.build(api.createLiveStream());
  }

  public Promise<LiveStream> startLiveStream(LiveStream liveStream) {
    return PromiseUtils.build(api.startLiveStream(liveStream.getId()));
  }
}

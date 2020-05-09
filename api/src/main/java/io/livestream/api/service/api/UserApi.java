package io.livestream.api.service.api;

import io.livestream.api.model.ConnectedChannel;
import io.livestream.api.model.LiveStream;
import io.livestream.api.model.SignInResult;
import io.livestream.api.model.payload.AccessTokenPayload;
import io.livestream.api.model.payload.AuthCodePayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

  @POST("users/sign_in/google")
  Call<SignInResult> signInWithGoogle(@Body AuthCodePayload payload);

  @POST("users/sign_in/facebook")
  Call<SignInResult> signInWithFacebook(@Body AccessTokenPayload payload);

  @POST("users/sign_out")
  Call<Void> signOut();

  @POST("users/channels/youtube")
  Call<ConnectedChannel> connectYouTubeChannel(@Body AuthCodePayload payload);

  @POST("users/channels/facebook")
  Call<ConnectedChannel> connectFacebookChannel(@Body AccessTokenPayload payload);

  @POST("users/streams")
  Call<LiveStream> createLiveStream();

  @POST("users/streams/{live_stream}/start")
  Call<LiveStream> startLiveStream(@Path("live_stream") String liveStreamId);

}
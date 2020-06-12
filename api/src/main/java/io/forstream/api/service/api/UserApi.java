package io.forstream.api.service.api;

import java.util.List;

import io.forstream.api.model.payload.AuthCodePayload;
import io.forstream.api.model.ConnectedChannel;
import io.forstream.api.model.LiveStream;
import io.forstream.api.model.SignInResult;
import io.forstream.api.model.User;
import io.forstream.api.model.payload.AccessTokenPayload;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

  @GET("v1/users/me")
  Call<User> getMyUser();

  @PUT("v1/users/me")
  Call<User> updateMyUser(@Body User user);

  @Multipart
  @PUT("v1/users/me/images")
  Call<User> updateMyUserImage(@Part MultipartBody.Part imagePart);

  @GET("v1/users/me/channels")
  Call<List<ConnectedChannel>> listMyConnectedChannels(@Query("populate") String populate);

  @GET("v1/users/me/streams")
  Call<List<LiveStream>> listMyLiveStreams(@Query("populate") String populate);

  @POST("v1/users/sign_in/google")
  Call<SignInResult> signInWithGoogle(@Body AuthCodePayload payload);

  @POST("v1/users/sign_in/facebook")
  Call<SignInResult> signInWithFacebook(@Body AccessTokenPayload payload);

  @POST("v1/users/sign_out")
  Call<Void> signOut();

  @POST("v1/users/channels/youtube")
  Call<ConnectedChannel> connectYouTubeChannel(@Body AuthCodePayload payload);

  @POST("v1/users/channels/facebook")
  Call<ConnectedChannel> connectFacebookChannel(@Body AccessTokenPayload payload);

  @POST("users/streams")
  Call<LiveStream> createLiveStream();

  @POST("users/streams/{live_stream}/start")
  Call<LiveStream> startLiveStream(@Path("live_stream") String liveStreamId);

}

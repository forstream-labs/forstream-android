package io.forstream.api.service;

import android.content.Context;
import android.net.Uri;

import com.onehilltech.promises.Promise;

import java.io.File;
import java.util.List;

import io.forstream.api.model.ConnectedChannel;
import io.forstream.api.model.LiveStream;
import io.forstream.api.model.User;
import io.forstream.api.model.payload.AccessTokenPayload;
import io.forstream.api.model.payload.AuthCodePayload;
import io.forstream.api.service.api.UserApi;
import io.forstream.api.util.PromiseUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.onehilltech.promises.Promise.value;

public class UserService {

  private Context context;
  private UserApi api;

  public UserService(Context context) {
    this.context = context;
    this.api = ApiFactory.build(UserApi.class, context);
  }

  public Promise<User> getMyUser() {
    return PromiseUtils.build(api.getMyUser());
  }

  public Promise<User> updateMyUser(User user) {
    return PromiseUtils.build(api.updateMyUser(user));
  }

  public Promise<User> updateMyUserImage(Uri imageUri) {
    File imageFile = new File(imageUri.getPath());
    RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageBody);
    return PromiseUtils.build(api.updateMyUserImage(imagePart));
  }

  public Promise<List<ConnectedChannel>> listMyConnectedChannels() {
    return PromiseUtils.build(api.listMyConnectedChannels(null));
  }

  public Promise<List<ConnectedChannel>> listMyConnectedChannels(String populate) {
    return PromiseUtils.build(api.listMyConnectedChannels(populate));
  }

  public Promise<List<LiveStream>> listMyLiveStreams(String populate) {
    return PromiseUtils.build(api.listMyLiveStreams(populate));
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
    return PromiseUtils.build(api.signOut()).then(aVoid -> {
      TokenManager.setToken(context, null);
      return value(null);
    });
  }
}

package io.livestream.view.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.livestream.api.model.LiveStream;
import io.livestream.api.service.UserService;
import io.livestream.common.viewmodel.BaseViewModel;
import io.livestream.service.AuthenticatedUser;
import timber.log.Timber;

public class MainViewModel extends BaseViewModel {

  private final AuthenticatedUser authenticatedUser;
  private final UserService userService;

  private MutableLiveData<LiveStream> createLiveStream = new MutableLiveData<>();
  private MutableLiveData<LiveStream> startLiveStream = new MutableLiveData<>();

  public MainViewModel(AuthenticatedUser authenticatedUser, UserService userService) {
    this.authenticatedUser = authenticatedUser;
    this.userService = userService;
  }

  public LiveData<LiveStream> getCreateLiveStream() {
    return createLiveStream;
  }

  public LiveData<LiveStream> getStartLiveStream() {
    return startLiveStream;
  }

  public void signInWithGoogle(String authCode) {
    userService.signInWithGoogle(authCode).then(user -> {
      authenticatedUser.set(user);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error signing in with Google");
      error.postValue(reason);
      return null;
    }));
  }

  public void signInWithFacebook(String accessToken) {
    userService.signInWithFacebook(accessToken).then(user -> {
      authenticatedUser.set(user);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error signing in with Facebook");
      error.postValue(reason);
      return null;
    }));
  }

  public void connectYouTubeChannel(String authCode) {
    userService.connectYouTubeChannel(authCode).then(connectedChannel -> null)._catch((reason -> {
      Timber.e(reason, "Error connecting YouTube channel");
      error.postValue(reason);
      return null;
    }));
  }

  public void connectFacebookChannel(String accessToken) {
    userService.connectFacebookChannel(accessToken).then(connectedChannel -> null)._catch((reason -> {
      Timber.e(reason, "Error connecting Facebook channel");
      error.postValue(reason);
      return null;
    }));
  }

  public void createLiveStream() {
    userService.createLiveStream().then(liveStream -> {
      createLiveStream.postValue(liveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error creating live stream");
      error.postValue(reason);
      return null;
    }));
  }

  public void startLiveStream(LiveStream liveStream) {
    userService.startLiveStream(liveStream).then(updatedLiveStream -> {
      startLiveStream.postValue(updatedLiveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error starting live stream %s", liveStream.getId());
      error.postValue(reason);
      return null;
    }));
  }
}

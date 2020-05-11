package io.livestream.view.intro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.livestream.api.model.User;
import io.livestream.api.service.UserService;
import io.livestream.common.viewmodel.BaseViewModel;
import io.livestream.service.AuthenticatedUser;
import timber.log.Timber;

public class IntroViewModel extends BaseViewModel {

  private final AuthenticatedUser authenticatedUser;
  private final UserService userService;

  private MutableLiveData<User> signInWithFacebook = new MutableLiveData<>();
  private MutableLiveData<User> signInWithGoogle = new MutableLiveData<>();

  public IntroViewModel(AuthenticatedUser authenticatedUser, UserService userService) {
    this.authenticatedUser = authenticatedUser;
    this.userService = userService;
  }

  public LiveData<User> getSignInWithFacebook() {
    return signInWithFacebook;
  }

  public LiveData<User> getSignInWithGoogle() {
    return signInWithGoogle;
  }

  public void signInWithFacebook(String accessToken) {
    userService.signInWithFacebook(accessToken).then(user -> {
      authenticatedUser.set(user);
      signInWithFacebook.postValue(user);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error signing in with Facebook");
      return null;
    });
  }

  public void signInWithGoogle(String authCode) {
    userService.signInWithGoogle(authCode).then(user -> {
      authenticatedUser.set(user);
      signInWithGoogle.postValue(user);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error signing in with Google");
      return null;
    });
  }
}
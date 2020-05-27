package io.forstream.view.intro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.forstream.api.model.User;
import io.forstream.api.service.UserService;
import io.forstream.common.viewmodel.BaseViewModel;
import io.forstream.service.AuthenticatedUser;
import timber.log.Timber;

public class IntroViewModel extends BaseViewModel {

  private AuthenticatedUser authenticatedUser;
  private UserService userService;

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
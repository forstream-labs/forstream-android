package io.livestream.view.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.livestream.api.model.LiveStream;
import io.livestream.api.model.User;
import io.livestream.api.service.UserService;
import io.livestream.common.viewmodel.BaseViewModel;
import io.livestream.service.AuthenticatedUser;
import timber.log.Timber;

public class MainViewModel extends BaseViewModel {

  private AuthenticatedUser authenticatedUser;
  private UserService userService;

  private MutableLiveData<User> signOut = new MutableLiveData<>();

  public MainViewModel(AuthenticatedUser authenticatedUser, UserService userService) {
    this.authenticatedUser = authenticatedUser;
    this.userService = userService;
  }

  public LiveData<User> getSignOut() {
    return signOut;
  }

  public User getAuthenticatedUser() {
    return authenticatedUser.get();
  }

  public void signOut() {
    userService.signOut().then(result -> {
      User user = authenticatedUser.get();
      authenticatedUser.remove();
      signOut.postValue(user);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error signing out");
      error.postValue(reason);
      return null;
    });
  }
}

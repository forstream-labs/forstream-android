package io.forstream.view.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.forstream.api.model.User;
import io.forstream.api.service.UserService;
import io.forstream.common.viewmodel.BaseViewModel;
import io.forstream.service.AuthenticatedUser;
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
    userService.signOut().then(aVoid -> {
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

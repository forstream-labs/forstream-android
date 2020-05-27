package io.forstream.view.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.forstream.api.model.User;
import io.forstream.api.service.UserService;
import io.forstream.common.viewmodel.BaseViewModel;
import io.forstream.service.AuthenticatedUser;
import timber.log.Timber;

public class SplashViewModel extends BaseViewModel {

  private final AuthenticatedUser authenticatedUser;
  private final UserService userService;

  private MutableLiveData<User> user = new MutableLiveData<>();

  public SplashViewModel(AuthenticatedUser authenticatedUser, UserService userService) {
    this.authenticatedUser = authenticatedUser;
    this.userService = userService;
  }

  public LiveData<User> getUser() {
    return user;
  }

  public void loadUser() {
    userService.getMyUser().then(user -> {
      authenticatedUser.set(user);
      this.user.postValue(user);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error loading user");
      error.postValue(reason);
      return null;
    }));
  }
}

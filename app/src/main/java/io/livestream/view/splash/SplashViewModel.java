package io.livestream.view.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.livestream.api.model.User;
import io.livestream.api.service.UserService;
import io.livestream.common.viewmodel.BaseViewModel;
import io.livestream.service.AuthenticatedUser;
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

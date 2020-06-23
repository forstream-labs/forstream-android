package io.forstream.view.profile;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import io.forstream.api.model.User;
import io.forstream.api.service.UserService;
import io.forstream.common.livedata.SingleLiveData;
import io.forstream.common.viewmodel.BaseViewModel;
import io.forstream.service.AuthenticatedUser;
import timber.log.Timber;

public class ProfileViewModel extends BaseViewModel {

  private SingleLiveData<User> user = new SingleLiveData<>();

  private AuthenticatedUser authenticatedUser;
  private UserService userService;

  public ProfileViewModel(AuthenticatedUser authenticatedUser, UserService userService) {
    this.authenticatedUser = authenticatedUser;
    this.userService = userService;
  }

  public LiveData<User> getUser() {
    return user;
  }

  public void loadUser() {
    user.postValue(authenticatedUser.get());
  }

  public void updateImage(Uri imageUri) {
    User user = new User();
    user.setId(authenticatedUser.get().getId());
    userService.updateMyUserImage(imageUri).then(updatedUser -> {
      authenticatedUser.set(updatedUser);
      this.user.postValue(updatedUser);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error updating user's image");
      error.postValue(reason);
      return null;
    });
  }

  public void updateFirstName(String firstName) {
    User user = new User();
    user.setId(authenticatedUser.get().getId());
    user.setFirstName(firstName);
    userService.updateMyUser(user).then(updatedUser -> {
      authenticatedUser.set(updatedUser);
      this.user.postValue(updatedUser);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error updating user's first name");
      error.postValue(reason);
      return null;
    });
  }

  public void updateLastName(String lastName) {
    User user = new User();
    user.setId(authenticatedUser.get().getId());
    user.setLastName(lastName);
    userService.updateMyUser(user).then(updatedUser -> {
      authenticatedUser.set(updatedUser);
      this.user.postValue(updatedUser);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error updating user's last name");
      error.postValue(reason);
      return null;
    });
  }
}

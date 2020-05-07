package io.livestream.dagger.module;

import androidx.lifecycle.ViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import io.livestream.api.service.UserService;
import io.livestream.dagger.util.ViewModelKey;
import io.livestream.service.AuthenticatedUser;
import io.livestream.view.main.MainViewModel;

@Module
public class ViewModelModule {

  @Provides
  @IntoMap
  @ViewModelKey(MainViewModel.class)
  ViewModel provideMainViewModel(AuthenticatedUser authenticatedUser, UserService userService) {
    return new MainViewModel(authenticatedUser, userService);
  }
}

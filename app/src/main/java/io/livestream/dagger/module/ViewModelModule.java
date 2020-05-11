package io.livestream.dagger.module;

import androidx.lifecycle.ViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import io.livestream.api.service.UserService;
import io.livestream.dagger.util.ViewModelKey;
import io.livestream.service.AuthenticatedUser;
import io.livestream.view.intro.IntroViewModel;
import io.livestream.view.main.MainViewModel;
import io.livestream.view.splash.SplashViewModel;

@Module
public class ViewModelModule {

  @Provides
  @IntoMap
  @ViewModelKey(SplashViewModel.class)
  ViewModel provideSplashViewModel(AuthenticatedUser authenticatedUser, UserService userService) {
    return new SplashViewModel(authenticatedUser, userService);
  }

  @Provides
  @IntoMap
  @ViewModelKey(IntroViewModel.class)
  ViewModel provideIntroViewModel(AuthenticatedUser authenticatedUser, UserService userService) {
    return new IntroViewModel(authenticatedUser, userService);
  }

  @Provides
  @IntoMap
  @ViewModelKey(MainViewModel.class)
  ViewModel provideMainViewModel(AuthenticatedUser authenticatedUser, UserService userService) {
    return new MainViewModel(authenticatedUser, userService);
  }
}

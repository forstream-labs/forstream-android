package io.livestream.dagger.module;

import androidx.lifecycle.ViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import io.livestream.api.service.ChannelService;
import io.livestream.api.service.StreamService;
import io.livestream.api.service.UserService;
import io.livestream.dagger.util.ViewModelKey;
import io.livestream.service.AuthenticatedUser;
import io.livestream.view.intro.IntroViewModel;
import io.livestream.view.livestream.LiveStreamViewModel;
import io.livestream.view.main.MainViewModel;
import io.livestream.view.main.channels.ChannelsViewModel;
import io.livestream.view.main.home.HomeViewModel;
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

  @Provides
  @IntoMap
  @ViewModelKey(HomeViewModel.class)
  ViewModel provideHomeViewModel(UserService userService, ChannelService channelService, StreamService streamService) {
    return new HomeViewModel(userService, channelService, streamService);
  }

  @Provides
  @IntoMap
  @ViewModelKey(ChannelsViewModel.class)
  ViewModel provideChannelsViewModel(ChannelService channelService) {
    return new ChannelsViewModel(channelService);
  }

  @Provides
  @IntoMap
  @ViewModelKey(LiveStreamViewModel.class)
  ViewModel provideLiveStreamViewModel(AuthenticatedUser authenticatedUser, StreamService streamService) {
    return new LiveStreamViewModel(authenticatedUser, streamService);
  }
}

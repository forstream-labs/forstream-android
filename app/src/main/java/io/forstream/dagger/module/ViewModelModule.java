package io.forstream.dagger.module;

import androidx.lifecycle.ViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import io.forstream.api.service.ChannelService;
import io.forstream.api.service.StreamService;
import io.forstream.api.service.UserService;
import io.forstream.dagger.util.ViewModelKey;
import io.forstream.service.AuthenticatedUser;
import io.forstream.service.NotificationService;
import io.forstream.view.intro.IntroViewModel;
import io.forstream.view.livestream.LiveStreamViewModel;
import io.forstream.view.main.MainViewModel;
import io.forstream.view.main.channels.ChannelsViewModel;
import io.forstream.view.main.home.HomeViewModel;
import io.forstream.view.profile.ProfileViewModel;
import io.forstream.view.splash.SplashViewModel;

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
  @ViewModelKey(ProfileViewModel.class)
  ViewModel provideProfileViewModel(AuthenticatedUser authenticatedUser, UserService userService) {
    return new ProfileViewModel(authenticatedUser, userService);
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
  ViewModel provideHomeViewModel(UserService userService, ChannelService channelService, StreamService streamService, NotificationService notificationService) {
    return new HomeViewModel(userService, channelService, streamService, notificationService);
  }

  @Provides
  @IntoMap
  @ViewModelKey(ChannelsViewModel.class)
  ViewModel provideChannelsViewModel(UserService userService, ChannelService channelService, NotificationService notificationService) {
    return new ChannelsViewModel(userService, channelService, notificationService);
  }

  @Provides
  @IntoMap
  @ViewModelKey(LiveStreamViewModel.class)
  ViewModel provideLiveStreamViewModel(AuthenticatedUser authenticatedUser, StreamService streamService, NotificationService notificationService) {
    return new LiveStreamViewModel(authenticatedUser, streamService, notificationService);
  }
}

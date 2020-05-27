package io.forstream.dagger.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.forstream.dagger.module.view.intro.IntroActivityModule;
import io.forstream.dagger.module.view.livestream.LiveStreamActivityModule;
import io.forstream.dagger.module.view.main.MainActivityModule;
import io.forstream.dagger.module.view.profile.ProfileActivityModule;
import io.forstream.dagger.module.view.splash.SplashActivityModule;
import io.forstream.dagger.scope.ActivityScope;
import io.forstream.view.intro.IntroActivity;
import io.forstream.view.livestream.LiveStreamActivity;
import io.forstream.view.main.MainActivity;
import io.forstream.view.profile.ProfileActivity;
import io.forstream.view.splash.SplashActivity;

@Module
public abstract class ViewInjectorModule {

  @ActivityScope
  @ContributesAndroidInjector(modules = SplashActivityModule.class)
  abstract SplashActivity injectSplashActivity();

  @ActivityScope
  @ContributesAndroidInjector(modules = IntroActivityModule.class)
  abstract IntroActivity injectIntroActivity();

  @ActivityScope
  @ContributesAndroidInjector(modules = ProfileActivityModule.class)
  abstract ProfileActivity injectProfileActivity();

  @ActivityScope
  @ContributesAndroidInjector(modules = MainActivityModule.class)
  abstract MainActivity injectMainActivity();

  @ActivityScope
  @ContributesAndroidInjector(modules = LiveStreamActivityModule.class)
  abstract LiveStreamActivity injectLiveStreamActivity();

}
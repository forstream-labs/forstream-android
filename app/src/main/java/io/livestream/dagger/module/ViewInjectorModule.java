package io.livestream.dagger.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.livestream.dagger.module.view.intro.IntroActivityModule;
import io.livestream.dagger.module.view.livestream.LiveStreamActivityModule;
import io.livestream.dagger.module.view.main.MainActivityModule;
import io.livestream.dagger.module.view.splash.SplashActivityModule;
import io.livestream.dagger.scope.ActivityScope;
import io.livestream.view.intro.IntroActivity;
import io.livestream.view.livestream.LiveStreamActivity;
import io.livestream.view.main.MainActivity;
import io.livestream.view.splash.SplashActivity;

@Module
public abstract class ViewInjectorModule {

  @ActivityScope
  @ContributesAndroidInjector(modules = SplashActivityModule.class)
  abstract SplashActivity injectSplashActivity();

  @ActivityScope
  @ContributesAndroidInjector(modules = IntroActivityModule.class)
  abstract IntroActivity injectIntroActivity();

  @ActivityScope
  @ContributesAndroidInjector(modules = MainActivityModule.class)
  abstract MainActivity injectMainActivity();

  @ActivityScope
  @ContributesAndroidInjector(modules = LiveStreamActivityModule.class)
  abstract LiveStreamActivity injectLiveStreamActivity();

}
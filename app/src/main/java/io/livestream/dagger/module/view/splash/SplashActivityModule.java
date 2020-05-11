package io.livestream.dagger.module.view.splash;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import io.livestream.dagger.module.base.ActivityModule;
import io.livestream.dagger.scope.ActivityScope;
import io.livestream.view.splash.SplashActivity;

@Module(includes = {ActivityModule.class, SplashViewModelModule.class})
public abstract class SplashActivityModule {

  @ActivityScope
  @Binds
  abstract AppCompatActivity provideActivity(SplashActivity activity);

}

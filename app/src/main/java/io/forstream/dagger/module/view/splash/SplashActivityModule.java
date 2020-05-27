package io.forstream.dagger.module.view.splash;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import io.forstream.dagger.module.base.ActivityModule;
import io.forstream.dagger.scope.ActivityScope;
import io.forstream.view.splash.SplashActivity;

@Module(includes = {ActivityModule.class, SplashViewModelModule.class})
public abstract class SplashActivityModule {

  @ActivityScope
  @Binds
  abstract AppCompatActivity provideActivity(SplashActivity activity);

}

package io.livestream.dagger.module.view.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import io.livestream.dagger.scope.ActivityScope;
import io.livestream.view.splash.SplashViewModel;

@Module
public class SplashViewModelModule {

  @ActivityScope
  @Provides
  SplashViewModel provideViewModel(AppCompatActivity activity, ViewModelProvider.Factory factory) {
    return new ViewModelProvider(activity, factory).get(SplashViewModel.class);
  }
}

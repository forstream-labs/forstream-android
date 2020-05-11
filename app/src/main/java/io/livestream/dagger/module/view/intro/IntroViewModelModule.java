package io.livestream.dagger.module.view.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import io.livestream.dagger.scope.ActivityScope;
import io.livestream.view.intro.IntroViewModel;

@Module
public class IntroViewModelModule {

  @ActivityScope
  @Provides
  IntroViewModel provideViewModel(AppCompatActivity activity, ViewModelProvider.Factory factory) {
    return new ViewModelProvider(activity, factory).get(IntroViewModel.class);
  }
}

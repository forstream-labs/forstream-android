package io.forstream.dagger.module.view.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import io.forstream.dagger.scope.ActivityScope;
import io.forstream.view.intro.IntroViewModel;

@Module
public class IntroViewModelModule {

  @ActivityScope
  @Provides
  IntroViewModel provideViewModel(AppCompatActivity activity, ViewModelProvider.Factory factory) {
    return new ViewModelProvider(activity, factory).get(IntroViewModel.class);
  }
}

package io.livestream.dagger.module.view.livestream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import io.livestream.dagger.scope.ActivityScope;
import io.livestream.view.livestream.LiveStreamViewModel;

@Module
public class LiveStreamViewModelModule {

  @ActivityScope
  @Provides
  LiveStreamViewModel provideViewModel(AppCompatActivity activity, ViewModelProvider.Factory factory) {
    return new ViewModelProvider(activity, factory).get(LiveStreamViewModel.class);
  }
}

package io.livestream.dagger.module.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import io.livestream.dagger.scope.FragmentScope;
import io.livestream.view.main.live.LiveViewModel;

@Module
public class LiveFragmentModule {

  @FragmentScope
  @Provides
  LiveViewModel provideViewModel(AppCompatActivity activity, ViewModelProvider.Factory factory) {
    return new ViewModelProvider(activity, factory).get(LiveViewModel.class);
  }
}

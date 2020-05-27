package io.forstream.dagger.module.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import io.forstream.dagger.scope.ActivityScope;
import io.forstream.view.main.MainViewModel;

@Module
public class MainViewModelModule {

  @ActivityScope
  @Provides
  MainViewModel provideViewModel(AppCompatActivity activity, ViewModelProvider.Factory factory) {
    return new ViewModelProvider(activity, factory).get(MainViewModel.class);
  }
}

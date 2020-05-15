package io.livestream.dagger.module.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import io.livestream.dagger.scope.FragmentScope;
import io.livestream.view.main.home.HomeViewModel;

@Module
public class HomeFragmentModule {

  @FragmentScope
  @Provides
  HomeViewModel provideViewModel(AppCompatActivity activity, ViewModelProvider.Factory factory) {
    return new ViewModelProvider(activity, factory).get(HomeViewModel.class);
  }
}

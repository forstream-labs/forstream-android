package io.livestream.dagger.module.view.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import io.livestream.dagger.scope.ActivityScope;
import io.livestream.view.profile.ProfileViewModel;

@Module
public class ProfileViewModelModule {

  @ActivityScope
  @Provides
  ProfileViewModel provideViewModel(AppCompatActivity activity, ViewModelProvider.Factory factory) {
    return new ViewModelProvider(activity, factory).get(ProfileViewModel.class);
  }
}

package io.livestream.dagger.module.view.profile;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import io.livestream.dagger.module.base.ActivityModule;
import io.livestream.dagger.scope.ActivityScope;
import io.livestream.view.profile.ProfileActivity;

@Module(includes = {ActivityModule.class, ProfileViewModelModule.class})
public abstract class ProfileActivityModule {

  @ActivityScope
  @Binds
  abstract AppCompatActivity provideActivity(ProfileActivity activity);

}

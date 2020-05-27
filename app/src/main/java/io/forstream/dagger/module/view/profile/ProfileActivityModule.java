package io.forstream.dagger.module.view.profile;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import io.forstream.dagger.module.base.ActivityModule;
import io.forstream.dagger.scope.ActivityScope;
import io.forstream.view.profile.ProfileActivity;

@Module(includes = {ActivityModule.class, ProfileViewModelModule.class})
public abstract class ProfileActivityModule {

  @ActivityScope
  @Binds
  abstract AppCompatActivity provideActivity(ProfileActivity activity);

}

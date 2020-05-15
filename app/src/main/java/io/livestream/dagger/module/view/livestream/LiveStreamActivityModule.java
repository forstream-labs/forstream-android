package io.livestream.dagger.module.view.livestream;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import io.livestream.dagger.module.base.ActivityModule;
import io.livestream.dagger.scope.ActivityScope;
import io.livestream.view.livestream.LiveStreamActivity;

@Module(includes = {ActivityModule.class, LiveStreamViewModelModule.class})
public abstract class LiveStreamActivityModule {

  @ActivityScope
  @Binds
  abstract AppCompatActivity provideActivity(LiveStreamActivity activity);

}

package io.forstream.dagger.module.view.livestream;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import io.forstream.dagger.module.base.ActivityModule;
import io.forstream.dagger.scope.ActivityScope;
import io.forstream.view.livestream.LiveStreamActivity;

@Module(includes = {ActivityModule.class, LiveStreamViewModelModule.class})
public abstract class LiveStreamActivityModule {

  @ActivityScope
  @Binds
  abstract AppCompatActivity provideActivity(LiveStreamActivity activity);

}

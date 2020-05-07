package io.livestream.dagger.module;

import android.app.Application;

import dagger.Binds;
import dagger.Module;
import io.livestream.LiveStreamApplication;
import io.livestream.dagger.scope.AppScope;

@Module
public abstract class AppModule {

  @AppScope
  @Binds
  abstract Application provideApplication(LiveStreamApplication application);

}
package io.forstream.dagger.module;

import android.app.Application;

import dagger.Binds;
import dagger.Module;
import io.forstream.ForstreamApplication;
import io.forstream.dagger.scope.AppScope;

@Module
public abstract class AppModule {

  @AppScope
  @Binds
  abstract Application provideApplication(ForstreamApplication application);

}
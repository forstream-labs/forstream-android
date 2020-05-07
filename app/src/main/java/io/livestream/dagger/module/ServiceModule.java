package io.livestream.dagger.module;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import io.livestream.api.service.UserService;
import io.livestream.dagger.scope.AppScope;

@Module
public class ServiceModule {

  @AppScope
  @Provides
  UserService provideUserService(Application application) {
    return new UserService(application);
  }
}

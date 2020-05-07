package io.livestream.dagger;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import io.livestream.LiveStreamApplication;
import io.livestream.dagger.module.AndroidModule;
import io.livestream.dagger.module.AppModule;
import io.livestream.dagger.module.ServiceModule;
import io.livestream.dagger.module.ViewInjectorModule;
import io.livestream.dagger.module.ViewModelFactoryModule;
import io.livestream.dagger.scope.AppScope;

@AppScope
@Component(modules = {
  AndroidInjectionModule.class,
  AppModule.class,
  AndroidModule.class,
  ServiceModule.class,
  ViewModelFactoryModule.class,
  ViewInjectorModule.class,
})
public interface AppComponent extends AndroidInjector<LiveStreamApplication> {

  @Component.Factory
  interface Factory extends AndroidInjector.Factory<LiveStreamApplication> {

  }
}
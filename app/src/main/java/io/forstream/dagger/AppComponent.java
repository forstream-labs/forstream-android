package io.forstream.dagger;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import io.forstream.ForstreamApplication;
import io.forstream.dagger.module.AndroidModule;
import io.forstream.dagger.module.AppModule;
import io.forstream.dagger.module.ServiceModule;
import io.forstream.dagger.module.ViewInjectorModule;
import io.forstream.dagger.module.ViewModelFactoryModule;
import io.forstream.dagger.scope.AppScope;

@AppScope
@Component(modules = {
  AndroidInjectionModule.class,
  AppModule.class,
  AndroidModule.class,
  ServiceModule.class,
  ViewModelFactoryModule.class,
  ViewInjectorModule.class,
})
public interface AppComponent extends AndroidInjector<ForstreamApplication> {

  @Component.Factory
  interface Factory extends AndroidInjector.Factory<ForstreamApplication> {

  }
}
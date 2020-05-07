package io.livestream.dagger.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.livestream.dagger.module.view.main.MainActivityModule;
import io.livestream.dagger.scope.ActivityScope;
import io.livestream.view.main.MainActivity;

@Module
public abstract class ViewInjectorModule {

  @ActivityScope
  @ContributesAndroidInjector(modules = MainActivityModule.class)
  abstract MainActivity injectMainActivity();

}
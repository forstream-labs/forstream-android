package io.livestream.dagger.module.view.main;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import io.livestream.dagger.module.base.ActivityModule;
import io.livestream.dagger.scope.ActivityScope;
import io.livestream.view.main.MainActivity;

@Module(includes = {ActivityModule.class, MainViewModelModule.class})
public abstract class MainActivityModule {

  @ActivityScope
  @Binds
  abstract AppCompatActivity provideActivity(MainActivity activity);

}

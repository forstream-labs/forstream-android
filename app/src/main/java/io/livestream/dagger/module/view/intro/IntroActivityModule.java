package io.livestream.dagger.module.view.intro;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import io.livestream.dagger.module.base.ActivityModule;
import io.livestream.dagger.scope.ActivityScope;
import io.livestream.view.intro.IntroActivity;

@Module(includes = {ActivityModule.class, IntroViewModelModule.class})
public abstract class IntroActivityModule {

  @ActivityScope
  @Binds
  abstract AppCompatActivity provideActivity(IntroActivity activity);

}

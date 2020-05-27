package io.forstream.dagger.module.view.intro;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import io.forstream.dagger.module.base.ActivityModule;
import io.forstream.dagger.scope.ActivityScope;
import io.forstream.view.intro.IntroActivity;

@Module(includes = {ActivityModule.class, IntroViewModelModule.class})
public abstract class IntroActivityModule {

  @ActivityScope
  @Binds
  abstract AppCompatActivity provideActivity(IntroActivity activity);

}

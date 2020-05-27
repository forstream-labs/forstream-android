package io.forstream.dagger.module.base;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import io.forstream.dagger.scope.ActivityScope;

@Module
public abstract class ActivityModule {

  @ActivityScope
  @Binds
  abstract Context provideContext(AppCompatActivity activity);

}
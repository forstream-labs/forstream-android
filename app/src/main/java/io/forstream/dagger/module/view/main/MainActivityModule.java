package io.forstream.dagger.module.view.main;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.forstream.dagger.module.base.ActivityModule;
import io.forstream.dagger.scope.ActivityScope;
import io.forstream.dagger.scope.FragmentScope;
import io.forstream.view.main.MainActivity;
import io.forstream.view.main.channels.ChannelsFragment;
import io.forstream.view.main.home.HomeFragment;

@Module(includes = {ActivityModule.class, MainViewModelModule.class})
public abstract class MainActivityModule {

  @ActivityScope
  @Binds
  abstract AppCompatActivity provideActivity(MainActivity activity);

  @FragmentScope
  @ContributesAndroidInjector(modules = HomeFragmentModule.class)
  abstract HomeFragment injectHomeFragment();

  @FragmentScope
  @ContributesAndroidInjector(modules = ChannelsFragmentModule.class)
  abstract ChannelsFragment injectChannelsFragment();

}

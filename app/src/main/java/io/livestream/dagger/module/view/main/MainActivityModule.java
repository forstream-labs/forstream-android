package io.livestream.dagger.module.view.main;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.livestream.dagger.module.base.ActivityModule;
import io.livestream.dagger.scope.ActivityScope;
import io.livestream.dagger.scope.FragmentScope;
import io.livestream.view.main.MainActivity;
import io.livestream.view.main.channels.ChannelsFragment;
import io.livestream.view.main.home.HomeFragment;

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

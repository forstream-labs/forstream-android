package io.livestream.dagger.module.view.main;

import androidx.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import io.livestream.dagger.scope.FragmentScope;
import io.livestream.view.main.channels.ChannelsFragment;
import io.livestream.view.main.channels.ChannelsViewModel;

@Module
public class ChannelsFragmentModule {

  @FragmentScope
  @Provides
  ChannelsViewModel provideViewModel(ChannelsFragment fragment, ViewModelProvider.Factory factory) {
    return new ViewModelProvider(fragment, factory).get(ChannelsViewModel.class);
  }
}

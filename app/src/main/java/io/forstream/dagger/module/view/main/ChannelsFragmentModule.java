package io.forstream.dagger.module.view.main;

import androidx.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import io.forstream.dagger.scope.FragmentScope;
import io.forstream.view.main.channels.ChannelsFragment;
import io.forstream.view.main.channels.ChannelsViewModel;

@Module
public class ChannelsFragmentModule {

  @FragmentScope
  @Provides
  ChannelsViewModel provideViewModel(ChannelsFragment fragment, ViewModelProvider.Factory factory) {
    return new ViewModelProvider(fragment, factory).get(ChannelsViewModel.class);
  }
}

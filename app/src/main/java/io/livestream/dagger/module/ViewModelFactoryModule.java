package io.livestream.dagger.module;

import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import io.livestream.common.viewmodel.ViewModelFactory;
import io.livestream.dagger.scope.AppScope;

@Module(includes = ViewModelModule.class)
public abstract class ViewModelFactoryModule {

  @AppScope
  @Binds
  abstract ViewModelProvider.Factory provideViewModelFactory(ViewModelFactory viewModelFactory);

}

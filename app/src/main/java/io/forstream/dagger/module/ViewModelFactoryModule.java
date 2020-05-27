package io.forstream.dagger.module;

import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import io.forstream.common.viewmodel.ViewModelFactory;
import io.forstream.dagger.scope.AppScope;

@Module(includes = ViewModelModule.class)
public abstract class ViewModelFactoryModule {

  @AppScope
  @Binds
  abstract ViewModelProvider.Factory provideViewModelFactory(ViewModelFactory viewModelFactory);

}

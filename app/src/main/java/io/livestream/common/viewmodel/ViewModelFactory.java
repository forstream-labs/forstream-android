package io.livestream.common.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class ViewModelFactory implements ViewModelProvider.Factory {

  private Map<Class<? extends ViewModel>, Provider<ViewModel>> providers;

  @Inject
  public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providers) {
    this.providers = providers;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> clazz) {
    Provider<? extends ViewModel> provider = providers.get(clazz);
    if (provider == null) {
      throw new IllegalArgumentException("No view model provider found for " + clazz);
    }
    try {
      return (T) provider.get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
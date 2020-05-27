package io.forstream.common.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {

  protected MutableLiveData<Throwable> error = new MutableLiveData<>();

  public LiveData<Throwable> getError() {
    return error;
  }
}

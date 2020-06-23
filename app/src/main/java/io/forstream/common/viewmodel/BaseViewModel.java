package io.forstream.common.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import io.forstream.common.livedata.SingleLiveData;

public class BaseViewModel extends ViewModel {

  protected SingleLiveData<Throwable> error = new SingleLiveData<>();

  public LiveData<Throwable> getError() {
    return error;
  }
}

package io.forstream.common.livedata;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 * <p>
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 * <p>
 * Note that only one observer is going to be notified of changes.
 */
public class SingleLiveData<T> extends MutableLiveData<T> {

  private final AtomicBoolean pending = new AtomicBoolean(false);

  public SingleLiveData() {
    super();
  }

  public SingleLiveData(T value) {
    super(value);
  }

  @MainThread
  @Override
  public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
    // Observe the internal MutableLiveData
    super.observe(owner, data -> {
      if (pending.compareAndSet(true, false)) {
        observer.onChanged(data);
      }
    });
  }

  @MainThread
  @Override
  public void setValue(T data) {
    pending.set(true);
    super.setValue(data);
  }

  @Override
  public void postValue(T data) {
    pending.set(true);
    super.postValue(data);
  }
}
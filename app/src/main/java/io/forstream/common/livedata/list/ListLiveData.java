package io.forstream.common.livedata.list;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ListLiveData<T> extends MutableLiveData<ListHolder<T>> {

  private boolean reversed;

  public ListLiveData() {
    this(false);
  }

  public ListLiveData(boolean reversed) {
    super(new ListHolder<>(new ArrayList<>(), reversed));
    this.reversed = reversed;
  }

  public void postValue(List<T> items) {
    postValue(new ListHolder<>(items, reversed, ListUpdateType.CHANGE_ALL));
  }

  public void setValue(List<T> items) {
    setValue(new ListHolder<>(items, reversed));
  }

  public void set(T item) {
    if (getValue() != null) {
      getValue().set(item);
      updateValue();
    }
  }

  public void set(int index, T item) {
    if (getValue() != null) {
      getValue().set(index, item);
      updateValue();
    }
  }

  public void add(T item) {
    if (getValue() != null) {
      getValue().add(item);
      updateValue();
    }
  }

  public void add(int index, T item) {
    if (getValue() != null) {
      getValue().add(index, item);
      updateValue();
    }
  }

  public void add(List<T> items) {
    if (getValue() != null) {
      getValue().add(items);
      updateValue();
    }
  }

  public void remove(T item) {
    if (getValue() != null) {
      getValue().remove(item);
      updateValue();
    }
  }

  public void remove(int index) {
    if (getValue() != null) {
      getValue().remove(index);
      updateValue();
    }
  }

  public T get(int index) {
    return getValue() != null ? getValue().get(index) : null;
  }

  public int indexOf(T item) {
    return getValue() != null ? getValue().indexOf(item) : -1;
  }

  public boolean isEmpty() {
    return getValue() == null || getValue().isEmpty();
  }

  public int size() {
    return getValue() != null ? getValue().size() : 0;
  }

  private void updateValue() {
    if (getValue() != null && !ListUpdateType.NONE.equals(getValue().getUpdateType())) {
      postValue(getValue());
    }
  }
}
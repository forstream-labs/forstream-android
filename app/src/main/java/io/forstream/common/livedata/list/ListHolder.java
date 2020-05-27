package io.forstream.common.livedata.list;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListHolder<T> {

  private static final int NO_INDEX_CHANGED = -1;

  private List<T> items;
  private boolean reversed;
  private ListUpdateType updateType;
  private int indexChanged = NO_INDEX_CHANGED;

  ListHolder(List<T> items, boolean reversed) {
    this(items, reversed, ListUpdateType.NONE);
  }

  ListHolder(List<T> items, boolean reversed, ListUpdateType updateType) {
    this.items = items;
    this.reversed = reversed;
    this.updateType = updateType;
  }

  public ListUpdateType getUpdateType() {
    return updateType;
  }

  public int getIndexChanged() {
    return indexChanged;
  }

  void set(T item) {
    int index = items.indexOf(item);
    if (index >= 0) {
      set(index, item);
    } else {
      indexChanged = index;
      updateType = ListUpdateType.NONE;
    }
  }

  void set(int index, T item) {
    items.set(index, item);
    indexChanged = index;
    updateType = ListUpdateType.CHANGE;
  }

  void add(T item) {
    if (!reversed) {
      items.add(item);
      indexChanged = items.size() - 1;
    } else {
      items.add(0, item);
      indexChanged = 0;
    }
    updateType = ListUpdateType.INSERT;
  }

  void add(int index, T item) {
    items.add(index, item);
    indexChanged = index;
    updateType = ListUpdateType.INSERT;
  }

  void add(List<T> items) {
    if (!items.isEmpty()) {
      indexChanged = this.items.size();
      updateType = ListUpdateType.INSERT_MANY;
      this.items.addAll(items);
    } else {
      indexChanged = NO_INDEX_CHANGED;
      updateType = ListUpdateType.NONE;
    }
  }

  void remove(T item) {
    int index = items.indexOf(item);
    if (index >= 0) {
      remove(index);
    } else {
      indexChanged = index;
      updateType = ListUpdateType.NONE;
    }
  }

  void remove(int index) {
    items.remove(index);
    indexChanged = index;
    updateType = ListUpdateType.REMOVE;
  }

  T get(int index) {
    return items.get(index);
  }

  int indexOf(T item) {
    return items.indexOf(item);
  }

  boolean isEmpty() {
    return items.isEmpty();
  }

  int size() {
    return items.size();
  }

  public List<T> getItems() {
    return items;
  }

  public void applyChanges(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
    updateType.notifyChange(adapter, indexChanged);
  }
}

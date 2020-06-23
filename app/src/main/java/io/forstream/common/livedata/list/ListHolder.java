package io.forstream.common.livedata.list;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListHolder<T> {

  private static final int NO_INDEX_CHANGED = -1;

  private List<T> items;
  private ListUpdateType updateType;
  private int indexChanged = NO_INDEX_CHANGED;

  ListHolder(List<T> items, ListUpdateType updateType) {
    this.items = items;
    this.updateType = updateType;
  }

  public List<T> getItems() {
    return items;
  }

  public int getIndexChanged() {
    return indexChanged;
  }

  public ListUpdateType getUpdateType() {
    return updateType;
  }

  public void applyChanges(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
    updateType.notifyChange(adapter, indexChanged);
  }

  void set(T item) {
    int index = items.indexOf(item);
    if (index >= 0) {
      set(index, item);
    } else {
      indexChanged = NO_INDEX_CHANGED;
      updateType = ListUpdateType.NONE;
    }
  }

  void set(int index, T item) {
    items.set(index, item);
    indexChanged = index;
    updateType = ListUpdateType.CHANGE;
  }

  void add(T item) {
    items.add(item);
    indexChanged = items.size() - 1;
    updateType = ListUpdateType.INSERT;
  }

  void add(int index, T item) {
    items.add(index, item);
    indexChanged = index;
    updateType = ListUpdateType.INSERT;
  }

  void addAll(List<T> items) {
    indexChanged = this.items.size();
    updateType = ListUpdateType.INSERT_MANY;
    this.items.addAll(items);
  }

  void remove(T item) {
    int index = items.indexOf(item);
    if (index >= 0) {
      remove(index);
    } else {
      indexChanged = NO_INDEX_CHANGED;
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
}

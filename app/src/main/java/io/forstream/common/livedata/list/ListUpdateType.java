package io.forstream.common.livedata.list;

import androidx.recyclerview.widget.RecyclerView;

public enum ListUpdateType {

  CHANGE_ALL {
    @Override
    public void notifyChange(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter, int indexChanged) {
      adapter.notifyDataSetChanged();
    }
  },
  CHANGE {
    @Override
    public void notifyChange(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter, int indexChanged) {
      adapter.notifyItemChanged(indexChanged);
    }
  },
  INSERT {
    @Override
    public void notifyChange(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter, int indexChanged) {
      adapter.notifyItemInserted(indexChanged);
    }
  },
  INSERT_MANY {
    @Override
    public void notifyChange(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter, int indexChanged) {
      int itemCount = adapter.getItemCount() - indexChanged;
      adapter.notifyItemRangeInserted(indexChanged, itemCount);
    }
  },
  REMOVE {
    @Override
    public void notifyChange(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter, int indexChanged) {
      adapter.notifyItemRemoved(indexChanged);
    }
  },
  NONE {
    @Override
    public void notifyChange(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter, int indexChanged) {

    }
  };

  ListUpdateType() {

  }

  public abstract void notifyChange(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter, int indexChanged);

}

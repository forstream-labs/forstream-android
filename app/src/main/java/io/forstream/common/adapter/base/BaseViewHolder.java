package io.forstream.common.adapter.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

  public BaseViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  public abstract void bindView(T model);

}

package io.livestream.util.component;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

  private int space;
  private int orientation;

  public SpaceItemDecoration(int space, int orientation) {
    this.space = space;
    this.orientation = orientation;
  }

  @Override
  public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
    if (orientation == RecyclerView.HORIZONTAL) {
      outRect.right = space;
    } else {
      outRect.bottom = space;
    }
  }
}
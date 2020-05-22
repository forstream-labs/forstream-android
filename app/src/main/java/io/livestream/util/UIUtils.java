package io.livestream.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import io.livestream.R;

public class UIUtils {

  private UIUtils() {

  }

  public static int getStatusBarHeight(Context context) {
    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      return context.getResources().getDimensionPixelSize(resourceId);
    }
    return 0;
  }

  public static int getNavigationBarHeight(Context context, int orientation) {
    Resources resources = context.getResources();
    int id = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
    if (id > 0) {
      return resources.getDimensionPixelSize(id);
    }
    return convertDpToPixel(context, 48);
  }

  public static void changeStatusBarColor(Activity activity, @ColorRes int color) {
    Window window = activity.getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    window.setStatusBarColor(ContextCompat.getColor(activity, color));
  }

  public static void defaultToolbar(AppCompatActivity activity) {
    Toolbar toolbar = activity.getWindow().getDecorView().findViewById(R.id.toolbar);
    if (toolbar == null) {
      throw new RuntimeException("Toolbar required");
    }
    activity.setSupportActionBar(toolbar);
    activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(view -> activity.finish());
  }

  public static void defaultSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout, SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
    swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primary, R.color.primary);
  }

  public static void showKeyboard(Activity activity, View view) {
    if (view.requestFocus()) {
      InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
      inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
  }

  public static void hideKeyboard(Activity activity) {
    if (activity != null) {
      InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
      if (activity.getCurrentFocus() != null) {
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
      }
    }
  }

  public static int convertDpToPixel(Context context, int dp) {
    return dp * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
  }

  public static int convertPixelsToDp(Context context, int px) {
    return px / (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
  }

  public static void setColorFilter(Drawable drawable, int color) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
    } else {
      drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }
  }
}

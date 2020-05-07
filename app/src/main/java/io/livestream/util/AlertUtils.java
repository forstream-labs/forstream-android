package io.livestream.util;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import io.livestream.R;
import io.livestream.api.exception.ApiException;

public class AlertUtils {

  private static final long SNACKBAR_DURATION = 5000L;
  private static final String ERROR_PREFIX = "error_";

  static {

  }

  private AlertUtils() {

  }

  public static void alert(Context context, String message) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
  }

  public static void alert(Context context, @StringRes int message) {
    Toast.makeText(context, context.getString(message), Toast.LENGTH_LONG).show();
  }

  public static void alert(Context context, Throwable throwable) {
    alert(context, getMessageId(context, throwable));
  }

  public static void alert(Context context, Throwable throwable, @StringRes int fallbackMessage) {
    alert(context, getMessageId(context, throwable, fallbackMessage));
  }

  public static void alert(View view, @StringRes int message) {
    alert(view, message, 0, null);
  }

  public static void alert(View view, @StringRes int message, @StringRes int action, View.OnClickListener onActionClickListener) {
    Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
    if (action != 0 && onActionClickListener != null) {
      snackbar.setAction(action, onActionClickListener);
      snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.secondary));
    }
    Handler handler = new Handler();
    handler.postDelayed(snackbar::dismiss, SNACKBAR_DURATION);
    snackbar.show();
  }

  public static void alert(View view, Throwable throwable) {
    int messageId = getMessageId(view.getContext(), throwable);
    alert(view, messageId);
  }

  public static void alert(View view, Throwable throwable, int fallbackTitle) {
    int messageId = getMessageId(view.getContext(), throwable, fallbackTitle);
    alert(view, messageId);
  }

  private static int getMessageId(Context context, Throwable throwable) {
    return getMessageId(context, throwable, 0);
  }

  private static int getMessageId(Context context, Throwable throwable, @StringRes int fallbackMessage) {
    if (throwable instanceof ApiException) {
      ApiException apiException = (ApiException) throwable;
      String resourceName = ERROR_PREFIX.concat(apiException.getCode());
      int resourceId = context.getResources().getIdentifier(resourceName, "string", context.getPackageName());
      if (resourceId > 0) {
        return resourceId;
      }
    }
    return fallbackMessage != 0 ? fallbackMessage : R.string.error_unexpected;
  }
}

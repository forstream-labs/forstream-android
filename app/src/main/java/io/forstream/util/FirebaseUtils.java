package io.forstream.util;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import io.forstream.api.model.User;

public class FirebaseUtils {

  private static final String EVENT_SIGN_IN = "sign_in";
  private static final String EVENT_SIGN_OUT = "sign_out";

  private FirebaseUtils() {

  }

  public static void logSignInWithFacebook(Context context, User user) {
    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, user.getId());
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user");
    bundle.putString(FirebaseAnalytics.Param.METHOD, "facebook");
    FirebaseAnalytics.getInstance(context).logEvent(EVENT_SIGN_IN, bundle);
  }

  public static void logSignInWithGoogle(Context context, User user) {
    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, user.getId());
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user");
    bundle.putString(FirebaseAnalytics.Param.METHOD, "google");
    FirebaseAnalytics.getInstance(context).logEvent(EVENT_SIGN_IN, bundle);
  }

  public static void logSignOut(Context context, User user) {
    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, user.getId());
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user");
    FirebaseAnalytics.getInstance(context).logEvent(EVENT_SIGN_OUT, bundle);
  }
}

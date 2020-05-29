package io.forstream.util;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import io.forstream.api.model.User;
import io.forstream.common.BaseActivity;

public class FirebaseUtils {

  private static final String EVENT_SIGN_UP = "sign_up";
  private static final String EVENT_SIGN_IN = "sign_in";
  private static final String EVENT_SIGN_OUT = "sign_out";

  private FirebaseUtils() {

  }

  public static void logSignInWithFacebook(BaseActivity baseActivity, User user) {
    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, user.getId());
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user");
    bundle.putString(FirebaseAnalytics.Param.METHOD, "facebook");
    baseActivity.getFirebaseAnalytics().logEvent(EVENT_SIGN_IN, bundle);
  }

  public static void logSignInWithGoogle(BaseActivity baseActivity, User user) {
    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, user.getId());
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user");
    bundle.putString(FirebaseAnalytics.Param.METHOD, "google");
    baseActivity.getFirebaseAnalytics().logEvent(EVENT_SIGN_IN, bundle);
  }

  public static void logSignOut(BaseActivity baseActivity, User user) {
    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, user.getId());
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user");
    baseActivity.getFirebaseAnalytics().logEvent(EVENT_SIGN_OUT, bundle);
  }
}

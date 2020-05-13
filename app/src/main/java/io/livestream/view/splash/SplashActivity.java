package io.livestream.view.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import io.livestream.R;
import io.livestream.api.exception.ApiException;
import io.livestream.common.BaseActivity;
import io.livestream.util.AlertUtils;
import io.livestream.util.UIUtils;
import io.livestream.view.intro.IntroActivity;
import io.livestream.view.main.MainActivity;

public class SplashActivity extends BaseActivity {

  private static final String TOKEN_EXPIRED_ERROR = "token_expired";
  private static final String AUTHENTICATION_REQUIRED = "authentication_required";

  @Inject SplashViewModel splashViewModel;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    UIUtils.changeStatusBarColor(this, android.R.color.darker_gray);
    setupObservers();
    setupUser();
  }

  private void setupObservers() {
    splashViewModel.getUser().observe(this, user -> startActivity(true));
    splashViewModel.getError().observe(this, throwable -> {
      if (mustSignOut(throwable)) {
        startActivity(false);
      } else {
        AlertUtils.alert(this, throwable);
      }
    });
  }

  private void setupUser() {
    splashViewModel.loadUser();
  }

  private boolean mustSignOut(Throwable throwable) {
    if (throwable instanceof ApiException) {
      ApiException apiException = (ApiException) throwable;
      return TOKEN_EXPIRED_ERROR.equals(apiException.getCode()) || AUTHENTICATION_REQUIRED.equals(apiException.getCode());
    }
    return false;
  }

  private void startActivity(boolean signedIn) {
    Intent intent = new Intent(this, signedIn ? MainActivity.class : IntroActivity.class);
    startActivity(intent);
    finish();
  }
}

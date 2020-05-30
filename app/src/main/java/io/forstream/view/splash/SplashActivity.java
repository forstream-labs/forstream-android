package io.forstream.view.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import io.forstream.R;
import io.forstream.api.exception.ApiException;
import io.forstream.common.BaseActivity;
import io.forstream.util.AlertUtils;
import io.forstream.view.intro.IntroActivity;
import io.forstream.view.main.MainActivity;

public class SplashActivity extends BaseActivity {

  private static final String TOKEN_EXPIRED_ERROR = "token_expired";
  private static final String AUTHENTICATION_REQUIRED = "authentication_required";

  @Inject SplashViewModel splashViewModel;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

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

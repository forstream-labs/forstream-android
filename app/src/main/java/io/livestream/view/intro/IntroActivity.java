package io.livestream.view.intro;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.livestream.R;
import io.livestream.common.BaseActivity;
import io.livestream.util.AlertUtils;
import io.livestream.util.UIUtils;
import io.livestream.view.main.MainActivity;

public class IntroActivity extends BaseActivity implements FacebookCallback<LoginResult> {

  private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 0;

  private static final String FACEBOOK_SCOPE_EMAIL = "email";

  @BindView(R.id.sign_in_with_facebook_button) MaterialButton signInWithFacebookButton;
  @BindView(R.id.sign_in_with_google_button) MaterialButton signInWithGoogleButton;

  @Inject IntroViewModel introViewModel;

  private GoogleSignInClient googleSignInClient;
  private CallbackManager callbackManager = CallbackManager.Factory.create();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro);
    ButterKnife.bind(this);

    UIUtils.changeStatusBarColor(this, android.R.color.darker_gray);
    setupObservers();
    setupGoogleSignIn();
    setupViews();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    callbackManager.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case GOOGLE_SIGN_IN_REQUEST_CODE:
        Task<GoogleSignInAccount> googleSignInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
        handleGoogleSignInResult(googleSignInTask);
        break;
    }
  }

  @Override
  public void onSuccess(LoginResult loginResult) {
    introViewModel.signInWithFacebook(loginResult.getAccessToken().getToken());
  }

  @Override
  public void onCancel() {

  }

  @Override
  public void onError(FacebookException exception) {
    AlertUtils.alert(this, exception);
  }

  @OnClick(R.id.sign_in_with_facebook_button)
  public void onSignInWithFacebookButtonClick() {
    LoginManager loginManager = LoginManager.getInstance();
    loginManager.registerCallback(callbackManager, this);
    loginManager.logInWithReadPermissions(this, Collections.singletonList(FACEBOOK_SCOPE_EMAIL));
  }

  @OnClick(R.id.sign_in_with_google_button)
  public void onSignInWithGoogleButtonClick() {
    Intent signInIntent = googleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
  }

  private void setupObservers() {
    introViewModel.getSignInWithFacebook().observe(this, user -> {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    });
    introViewModel.getSignInWithGoogle().observe(this, user -> {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    });
    introViewModel.getError().observe(this, throwable -> AlertUtils.alert(this, throwable));
  }

  private void setupGoogleSignIn() {
    String googleAuthClientId = getString(R.string.google_oauth2_client_id);
    GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestId()
      .requestEmail()
      .requestProfile()
      .requestIdToken(googleAuthClientId)
      .requestServerAuthCode(googleAuthClientId)
      .build();
    googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
  }

  private void setupViews() {
    signInWithFacebookButton.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_facebook_square));
    signInWithGoogleButton.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_google));
  }

  private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
      GoogleSignInAccount googleSignInAccount = completedTask.getResult(ApiException.class);
      introViewModel.signInWithGoogle(googleSignInAccount.getServerAuthCode());
    } catch (ApiException e) {
      AlertUtils.alert(this, e);
    }
  }
}

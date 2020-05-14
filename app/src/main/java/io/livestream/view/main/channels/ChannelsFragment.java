package io.livestream.view.main.channels;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

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
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.livestream.R;
import io.livestream.api.model.Channel;
import io.livestream.common.BaseFragment;
import io.livestream.common.livedata.list.ListUpdateType;
import io.livestream.util.AlertUtils;
import io.livestream.util.component.SpaceItemDecoration;

public class ChannelsFragment extends BaseFragment implements ChannelsAdapter.Listener {

  private static final int YOUTUBE_CHANNEL_SIGN_IN_REQUEST_CODE = 1;

  private static final String YOUTUBE_MANAGE_SCOPE = "https://www.googleapis.com/auth/youtube";
  private static final String FACEBOOK_PUBLISH_VIDEO_SCOPE = "publish_video";

  @BindView(R.id.channels_view) RecyclerView channelsView;

  @Inject Context context;
  @Inject ChannelsViewModel channelsViewModel;
  @Inject ChannelsAdapter channelsAdapter;

  private GoogleSignInClient youtubeChannelSignInClient;
  private CallbackManager callbackManager = CallbackManager.Factory.create();

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_channels, container, false);
    ButterKnife.bind(this, view);

    setupObservers();
    setupViews();
    setupYouTubeChannelSignIn();
    setupContent();
    return view;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case YOUTUBE_CHANNEL_SIGN_IN_REQUEST_CODE:
        Task<GoogleSignInAccount> youtubeChannelSignInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
        handleYouTubeChannelSignInResult(youtubeChannelSignInTask);
        break;
    }
  }

  @Override
  public void onChannelClick(Channel channel) {
    switch (channel.getIdentifier()) {
      case YOUTUBE:
        Intent signInIntent = youtubeChannelSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, YOUTUBE_CHANNEL_SIGN_IN_REQUEST_CODE);
        break;
      case FACEBOOK:
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new ConnectFacebookCallback());
        loginManager.logInWithPublishPermissions(this, Collections.singletonList(FACEBOOK_PUBLISH_VIDEO_SCOPE));
        break;
    }
  }

  private void setupObservers() {
    channelsViewModel.getChannels().observe(getViewLifecycleOwner(), channelsHolder -> {
      if (!ListUpdateType.NONE.equals(channelsHolder.getUpdateType())) {
        channelsAdapter.setChannels(channelsHolder.getItems());
        channelsHolder.applyChanges(channelsAdapter);
      }
    });
    channelsViewModel.getError().observe(getViewLifecycleOwner(), throwable -> AlertUtils.alert(context, throwable));
  }

  private void setupViews() {
    channelsAdapter.setListener(this);

    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
    SpaceItemDecoration itemDecoration = new SpaceItemDecoration(context.getResources().getDimensionPixelSize(R.dimen.margin_md), layoutManager.getOrientation());
    channelsView.setLayoutManager(layoutManager);
    channelsView.addItemDecoration(itemDecoration);
    channelsView.setAdapter(channelsAdapter);
    RecyclerView.ItemAnimator itemAnimator = channelsView.getItemAnimator();
    if (itemAnimator instanceof SimpleItemAnimator) {
      ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
    }
  }

  private void setupYouTubeChannelSignIn() {
    String googleAuthClientId = getString(R.string.google_oauth2_client_id);
    GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestId()
      .requestEmail()
      .requestProfile()
      .requestIdToken(googleAuthClientId)
      .requestServerAuthCode(googleAuthClientId)
      .requestScopes(new Scope(YOUTUBE_MANAGE_SCOPE))
      .build();
    youtubeChannelSignInClient = GoogleSignIn.getClient(context, signInOptions);
  }

  private void setupContent() {
    channelsViewModel.loadChannels();
    channelsViewModel.loadConnectedChannels();
  }

  private void handleYouTubeChannelSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
      GoogleSignInAccount googleSignInAccount = completedTask.getResult(ApiException.class);
      channelsViewModel.connectYouTubeChannel(googleSignInAccount.getServerAuthCode());
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }

  private class ConnectFacebookCallback implements FacebookCallback<LoginResult> {
    @Override
    public void onSuccess(LoginResult loginResult) {
      channelsViewModel.connectFacebookChannel(loginResult.getAccessToken().getToken());
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {
      AlertUtils.alert(context, error);
    }
  }
}
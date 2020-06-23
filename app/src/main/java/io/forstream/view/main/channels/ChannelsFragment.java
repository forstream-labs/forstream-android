package io.forstream.view.main.channels;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.annimon.stream.Stream;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.forstream.R;
import io.forstream.api.model.Channel;
import io.forstream.api.model.ChannelTarget;
import io.forstream.common.BaseFragment;
import io.forstream.util.AlertUtils;
import io.forstream.util.UIUtils;
import io.forstream.util.component.SpaceItemDecoration;
import io.forstream.view.main.channels.facebook.ChannelTargetDialogFragment;
import timber.log.Timber;

public class ChannelsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ChannelsAdapter.Listener, ChannelTargetDialogFragment.Listener {

  private static final int YOUTUBE_CHANNEL_SIGN_IN_REQUEST_CODE = 1;
  private static final int TWITCH_CHANNEL_REQUEST_CODE = 2;

  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.channels_view) RecyclerView channelsView;

  @Inject Context context;
  @Inject ChannelsViewModel channelsViewModel;
  @Inject ChannelsAdapter channelsAdapter;

  private ChannelTargetDialogFragment channelTargetDialogFragment;
  private CallbackManager callbackManager = CallbackManager.Factory.create();
  private Channel selectedChannel;
  private String selectedAccessToken;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_channels, container, false);
    ButterKnife.bind(this, view);

    UIUtils.defaultSwipeRefreshLayout(swipeRefreshLayout, this);
    setupObservers();
    setupViews();
    setupContent();
    return view;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case YOUTUBE_CHANNEL_SIGN_IN_REQUEST_CODE:
        handleYouTubeChannelSignInResult(data);
        break;
      case TWITCH_CHANNEL_REQUEST_CODE:
        handleTwitchChannelSignInResult(data);
        break;
    }
  }

  @Override
  public void onRefresh() {
    setupContent();
  }

  @Override
  public void onChannelClick(Channel channel) {
    selectedChannel = channel;
    switch (channel.getIdentifier()) {
      case YOUTUBE:
        onYouTubeChannelClick(channel);
        break;
      case FACEBOOK:
        onFacebookChannelClick(channel);
        break;
      case TWITCH:
        onTwitchChannelClick(channel);
        break;
    }
  }

  @Override
  public void onConnectButtonClick(ChannelTarget channelTarget) {
    channelsViewModel.connectFacebookChannel(selectedAccessToken, channelTarget.getId());
  }

  private void setupObservers() {
    channelsViewModel.getViewItems().observe(getViewLifecycleOwner(), channelsHolder -> {
      swipeRefreshLayout.setRefreshing(false);
      channelsAdapter.setViewItems(channelsHolder.getItems());
      channelsHolder.applyChanges(channelsAdapter);
    });
    channelsViewModel.getFacebookTargets().observe(getViewLifecycleOwner(), channelTargets -> {
      if (channelTargets.size() == 1) {
        channelsViewModel.connectFacebookChannel(selectedAccessToken, channelTargets.get(0).getId());
      } else {
        channelTargetDialogFragment = new ChannelTargetDialogFragment(context);
        channelTargetDialogFragment.setListener(this);
        channelTargetDialogFragment.setChannel(selectedChannel);
        channelTargetDialogFragment.setChannelTargets(channelTargets);
        channelTargetDialogFragment.show(getActivity().getSupportFragmentManager(), ChannelsFragment.class.getSimpleName());
      }
    });
    channelsViewModel.getChannelConnected().observe(getViewLifecycleOwner(), connectedChannel -> {
      if (channelTargetDialogFragment != null) {
        channelTargetDialogFragment.dismiss();
      }
    });
    channelsViewModel.getError().observe(getViewLifecycleOwner(), throwable -> {
      swipeRefreshLayout.setRefreshing(false);
      AlertUtils.alert(context, throwable);
    });
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

  private void setupContent() {
    channelsViewModel.loadViewItems();
  }

  private void onYouTubeChannelClick(Channel channel) {
    Scope scope = new Scope(channel.getRequiredScopes().get(0));
    Scope[] scopes = Stream.of(channel.getRequiredScopes()).skip(1).map(Scope::new).toArray(Scope[]::new);
    String googleAuthClientId = getString(R.string.default_web_client_id);
    GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestEmail()
      .requestIdToken(googleAuthClientId)
      .requestServerAuthCode(googleAuthClientId)
      .requestScopes(scope, scopes)
      .build();
    GoogleSignInClient youtubeChannelSignInClient = GoogleSignIn.getClient(context, signInOptions);
    youtubeChannelSignInClient.revokeAccess();
    Intent signInIntent = youtubeChannelSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, YOUTUBE_CHANNEL_SIGN_IN_REQUEST_CODE);
  }

  private void onFacebookChannelClick(Channel channel) {
    LoginManager loginManager = LoginManager.getInstance();
    loginManager.registerCallback(callbackManager, new ConnectFacebookCallback());
    loginManager.logIn(this, channel.getRequiredScopes());
  }

  private void onTwitchChannelClick(Channel channel) {
    AuthorizationServiceConfiguration.fetchFromIssuer(Uri.parse("https://id.twitch.tv/oauth2"), (serviceConfiguration, authorizationException) -> {
      if (authorizationException != null) {
        Timber.e("Error fetching oauth2 config from Twitch: %s %s", authorizationException.error, authorizationException.errorDescription);
        AlertUtils.alert(context, R.string.twitch_error_sign_in);
        return;
      }
      AuthorizationRequest.Builder authorizationRequestBuilder = new AuthorizationRequest.Builder(
        serviceConfiguration,
        getString(R.string.twitch_client_id),
        ResponseTypeValues.CODE,
        Uri.parse(getString(R.string.twitch_redirect_url))
      );
      AuthorizationRequest authorizationRequest = authorizationRequestBuilder
        .setScopes(channel.getRequiredScopes())
        .build();
      AuthorizationService authorizationService = new AuthorizationService(context);
      Intent authorizationIntent = authorizationService.getAuthorizationRequestIntent(authorizationRequest);
      startActivityForResult(authorizationIntent, TWITCH_CHANNEL_REQUEST_CODE);
    });
  }

  private void handleYouTubeChannelSignInResult(Intent data) {
    try {
      Task<GoogleSignInAccount> youtubeChannelSignInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
      GoogleSignInAccount googleSignInAccount = youtubeChannelSignInTask.getResult(RuntimeException.class);
      channelsViewModel.connectYouTubeChannel(googleSignInAccount.getServerAuthCode());
    } catch (RuntimeException e) {
      Timber.e(e, "Error signing in with YouTube");
      AlertUtils.alert(context, R.string.youtube_error_sign_in);
    }
  }

  private void handleTwitchChannelSignInResult(Intent data) {
    AuthorizationException authorizationException = AuthorizationException.fromIntent(data);
    if (authorizationException != null) {
      Timber.e("Error executing oauth2 flow with Twitch: %s %s", authorizationException.error, authorizationException.errorDescription);
      AlertUtils.alert(context, R.string.twitch_error_sign_in);
      return;
    }
    AuthorizationResponse authorizationResponse = AuthorizationResponse.fromIntent(data);
    channelsViewModel.connectTwitchChannel(authorizationResponse.authorizationCode);
  }

  private class ConnectFacebookCallback implements FacebookCallback<LoginResult> {
    @Override
    public void onSuccess(LoginResult loginResult) {
      selectedAccessToken = loginResult.getAccessToken().getToken();
      channelsViewModel.listFacebookTargets(selectedAccessToken);
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

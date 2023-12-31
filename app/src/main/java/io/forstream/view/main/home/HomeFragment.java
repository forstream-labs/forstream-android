package io.forstream.view.main.home;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.forstream.R;
import io.forstream.api.enums.ChannelIdentifier;
import io.forstream.api.model.ChannelAlert;
import io.forstream.api.model.ConnectedChannel;
import io.forstream.api.model.LiveStream;
import io.forstream.common.BaseFragment;
import io.forstream.common.Constants;
import io.forstream.common.livedata.list.ListHolder;
import io.forstream.common.livedata.list.ListUpdateType;
import io.forstream.util.AlertUtils;
import io.forstream.util.UIUtils;
import io.forstream.util.component.SpaceItemDecoration;
import io.forstream.view.livestream.LiveStreamActivity;
import io.forstream.view.main.home.connectedchannels.ConnectedChannelsAdapter;
import io.forstream.view.main.home.livestream.CreateLiveStreamDialogFragment;
import io.forstream.view.main.home.livestream.LiveStreamsAdapter;

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, CreateLiveStreamDialogFragment.Listener, LiveStreamsAdapter.Listener, ConnectedChannelsAdapter.Listener {

  private static final String CHANNEL_ALERT_ENABLE_LIVE_STREAMING = "enable_live_streaming";

  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.connected_channels_layout) View connectedChannelsLayout;
  @BindView(R.id.connected_channels_view) RecyclerView connectedChannelsView;
  @BindView(R.id.no_channels_connected) View noChannelsConnectedLayout;
  @BindView(R.id.live_streams_layout) View liveStreamsLayout;
  @BindView(R.id.live_streams_view) RecyclerView liveStreamsView;
  @BindView(R.id.no_live_streams) View noLiveStreamsLayout;
  @BindView(R.id.create_live_stream_button) FloatingActionButton createLiveStreamButton;

  @Inject Context context;
  @Inject HomeViewModel homeViewModel;
  @Inject ConnectedChannelsAdapter connectedChannelsAdapter;
  @Inject LiveStreamsAdapter liveStreamsAdapter;
  @Inject CreateLiveStreamDialogFragment createLiveStreamDialogFragment;

  private ViewPager viewPager;

  public HomeFragment(ViewPager viewPager) {
    this.viewPager = viewPager;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    ButterKnife.bind(this, view);

    UIUtils.defaultSwipeRefreshLayout(swipeRefreshLayout, this);
    setupObservers();
    setupViews();
    setupConnectedChannelsView();
    setupLiveStreamsView();
    setupContent();
    return view;
  }

  @Override
  public void onRemoveConnectedChannelClick(ConnectedChannel connectedChannel) {
    homeViewModel.disconnectChannel(connectedChannel);
  }

  @Override
  public void onChannelAlertClick(ConnectedChannel connectedChannel, ChannelAlert channelAlert) {
    if (ChannelIdentifier.YOUTUBE.equals(connectedChannel.getChannel().getIdentifier()) && CHANNEL_ALERT_ENABLE_LIVE_STREAMING.equals(channelAlert.getId())) {
      MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context)
        .setTitle(R.string.channel_alert_youtube_enable_live_streaming_dialog_title)
        .setView(R.layout.view_channel_alert_enable_youtube_live_streaming)
        .setPositiveButton(R.string.accept, (dialog, which) -> homeViewModel.checkChannelAlert(connectedChannel, channelAlert))
        .setNegativeButton(R.string.close, null);
      dialogBuilder.create().show();
    }
  }

  @Override
  public void onLiveStreamClick(LiveStream liveStream) {
    Intent intent = new Intent(context, LiveStreamActivity.class);
    intent.putExtra(Constants.LIVE_STREAM, liveStream);
    startActivity(intent);
  }

  @Override
  public void onRemoveLiveStreamClick(LiveStream liveStream) {
    homeViewModel.removeLiveStream(liveStream);
  }

  @Override
  public void onRefresh() {
    setupContent();
  }

  @Override
  public void onCreateLiveStreamButtonClick(String title, String description, List<ChannelIdentifier> channelsIdentifiers) {
    homeViewModel.createLiveStream(title, description, channelsIdentifiers);
  }

  @OnClick(R.id.connect_channels_button)
  void onConnectChannelsButtonClick() {
    // Goes to channels tab
    viewPager.setCurrentItem(1);
  }

  @OnClick({R.id.go_live_button, R.id.create_live_stream_button})
  void onCreateLiveStreamButtonClick() {
    createLiveStreamDialogFragment.show(getActivity().getSupportFragmentManager(), HomeFragment.class.getSimpleName());
  }

  private void setupObservers() {
    homeViewModel.getConnectedChannels().observe(getViewLifecycleOwner(), connectedChannelsHolder -> {
      swipeRefreshLayout.setRefreshing(false);
      updateLayouts();
      createLiveStreamDialogFragment.setConnectedChannels(connectedChannelsHolder.getItems());
      connectedChannelsAdapter.setConnectedChannels(connectedChannelsHolder.getItems());
      connectedChannelsHolder.applyChanges(connectedChannelsAdapter);
    });
    homeViewModel.getLiveStreams().observe(getViewLifecycleOwner(), liveStreamsHolder -> {
      swipeRefreshLayout.setRefreshing(false);
      updateLayouts();
      liveStreamsAdapter.setLiveStreams(liveStreamsHolder.getItems());
      liveStreamsHolder.applyChanges(liveStreamsAdapter);
    });
    homeViewModel.getCreateLiveStream().observe(getViewLifecycleOwner(), liveStream -> {
      createLiveStreamDialogFragment.dismiss();
      Intent intent = new Intent(context, LiveStreamActivity.class);
      intent.putExtra(Constants.LIVE_STREAM, liveStream);
      startActivity(intent);
    });
    homeViewModel.getError().observe(getViewLifecycleOwner(), throwable -> {
      swipeRefreshLayout.setRefreshing(false);
      AlertUtils.alert(context, throwable);
    });
  }

  private void setupViews() {
    createLiveStreamDialogFragment.setListener(this);
    createLiveStreamButton.setImageDrawable(new IconicsDrawable(context, FontAwesome.Icon.faw_video));
  }

  private void setupConnectedChannelsView() {
    connectedChannelsAdapter.setListener(this);

    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
    SpaceItemDecoration itemDecoration = new SpaceItemDecoration(context.getResources().getDimensionPixelSize(R.dimen.margin_sm), layoutManager.getOrientation());
    connectedChannelsView.setLayoutManager(layoutManager);
    connectedChannelsView.addItemDecoration(itemDecoration);
    connectedChannelsView.setAdapter(connectedChannelsAdapter);
    RecyclerView.ItemAnimator itemAnimator = connectedChannelsView.getItemAnimator();
    if (itemAnimator instanceof SimpleItemAnimator) {
      ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
    }
  }

  private void setupLiveStreamsView() {
    liveStreamsAdapter.setListener(this);

    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
    SpaceItemDecoration itemDecoration = new SpaceItemDecoration(context.getResources().getDimensionPixelSize(R.dimen.margin_md), layoutManager.getOrientation());
    liveStreamsView.setLayoutManager(layoutManager);
    liveStreamsView.addItemDecoration(itemDecoration);
    liveStreamsView.setAdapter(liveStreamsAdapter);
    RecyclerView.ItemAnimator itemAnimator = liveStreamsView.getItemAnimator();
    if (itemAnimator instanceof SimpleItemAnimator) {
      ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
    }
  }

  private void setupContent() {
    homeViewModel.loadConnectedChannels();
    homeViewModel.loadLiveStreams();
  }

  private void updateLayouts() {
    ListHolder<ConnectedChannel> connectedChannelsHolder = homeViewModel.getConnectedChannels().getValue();
    ListHolder<LiveStream> liveStreamsHolder = homeViewModel.getLiveStreams().getValue();
    if (connectedChannelsHolder.getUpdateType().equals(ListUpdateType.NONE) || liveStreamsHolder.getUpdateType().equals(ListUpdateType.NONE)) {
      return;
    }

    List<ConnectedChannel> connectedChannels = connectedChannelsHolder.getItems();
    List<LiveStream> liveStreams = liveStreamsHolder.getItems();
    connectedChannelsLayout.setVisibility(View.VISIBLE);
    noChannelsConnectedLayout.setVisibility(connectedChannels.isEmpty() ? View.VISIBLE : View.GONE);
    liveStreamsLayout.setVisibility(!liveStreams.isEmpty() || !connectedChannels.isEmpty() ? View.VISIBLE : View.GONE);
    noLiveStreamsLayout.setVisibility(liveStreams.isEmpty() ? View.VISIBLE : View.GONE);
    createLiveStreamButton.setVisibility(!connectedChannels.isEmpty() && !liveStreams.isEmpty() ? View.VISIBLE : View.GONE);
  }
}

package io.livestream.view.main.home;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.livestream.R;
import io.livestream.api.enums.ChannelIdentifier;
import io.livestream.api.model.ConnectedChannel;
import io.livestream.api.model.LiveStream;
import io.livestream.common.BaseFragment;
import io.livestream.common.Constants;
import io.livestream.common.livedata.list.ListUpdateType;
import io.livestream.util.AlertUtils;
import io.livestream.util.UIUtils;
import io.livestream.util.component.SpaceItemDecoration;
import io.livestream.view.livestream.LiveStreamActivity;

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, CreateLiveStreamDialogFragment.Listener, LiveStreamsAdapter.Listener, ConnectedChannelsAdapter.Listener {

  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.connected_channels_layout) View connectedChannelsLayout;
  @BindView(R.id.connected_channels_view) RecyclerView connectedChannelsView;
  @BindView(R.id.live_streams_layout) View liveStreamsLayout;
  @BindView(R.id.live_streams_view) RecyclerView liveStreamsView;
  @BindView(R.id.create_live_stream_button) FloatingActionButton createLiveStreamButton;

  @Inject Context context;
  @Inject HomeViewModel homeViewModel;
  @Inject ConnectedChannelsAdapter connectedChannelsAdapter;
  @Inject LiveStreamsAdapter liveStreamsAdapter;
  @Inject CreateLiveStreamDialogFragment createLiveStreamDialogFragment;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    ButterKnife.bind(this, view);

    setupObservers();
    setupSwipeRefreshLayout();
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

  @OnClick(R.id.create_live_stream_button)
  void onCreateLiveStreamButtonClick() {
    createLiveStreamDialogFragment.show(getActivity().getSupportFragmentManager(), HomeFragment.class.getSimpleName());
  }

  private void setupObservers() {
    homeViewModel.getConnectedChannels().observe(getViewLifecycleOwner(), connectedChannelsHolder -> {
      connectedChannelsLayout.setVisibility(View.VISIBLE);
      swipeRefreshLayout.setRefreshing(false);
      createLiveStreamDialogFragment.setConnectedChannels(connectedChannelsHolder.getItems());
      if (!ListUpdateType.NONE.equals(connectedChannelsHolder.getUpdateType())) {
        connectedChannelsAdapter.setConnectedChannels(connectedChannelsHolder.getItems());
        connectedChannelsHolder.applyChanges(connectedChannelsAdapter);
      }
    });
    homeViewModel.getLiveStreams().observe(getViewLifecycleOwner(), liveStreamsHolder -> {
      liveStreamsLayout.setVisibility(View.VISIBLE);
      swipeRefreshLayout.setRefreshing(false);
      if (!ListUpdateType.NONE.equals(liveStreamsHolder.getUpdateType())) {
        liveStreamsAdapter.setLiveStreams(liveStreamsHolder.getItems());
        liveStreamsHolder.applyChanges(liveStreamsAdapter);
      }
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

  private void setupSwipeRefreshLayout() {
    UIUtils.defaultSwipeRefreshLayout(swipeRefreshLayout, this);
    swipeRefreshLayout.setProgressViewOffset(false, 0, UIUtils.convertDpToPixel(context, 80));
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
}

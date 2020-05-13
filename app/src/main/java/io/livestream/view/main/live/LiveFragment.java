package io.livestream.view.main.live;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.livestream.R;
import io.livestream.api.model.ConnectedChannel;
import io.livestream.common.BaseFragment;
import io.livestream.common.livedata.list.ListUpdateType;
import io.livestream.util.AlertUtils;
import io.livestream.util.component.SpaceItemDecoration;

public class LiveFragment extends BaseFragment implements ConnectedChannelsAdapter.AdapterListener, CreateLiveStreamDialogFragment.Listener {

  @BindView(R.id.connected_channels_view) RecyclerView connectedChannelsView;
  @BindView(R.id.create_live_stream_button) FloatingActionButton createLiveStreamButton;

  @Inject Context context;
  @Inject LiveViewModel liveViewModel;
  @Inject ConnectedChannelsAdapter connectedChannelsAdapter;
  @Inject LiveStreamsAdapter liveStreamsAdapter;

  private CreateLiveStreamDialogFragment createLiveStreamDialogFragment;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_live, container, false);
    ButterKnife.bind(this, view);

    setupObservers();
    setupViews();
    setupContent();
    return view;
  }

  @Override
  public void onConnectedChannelClick(ConnectedChannel connectedChannel) {

  }

  @Override
  public void onCreateLiveStreamButtonClick(String title, String description) {
    liveViewModel.createLiveStream(title, description);
  }

  @OnClick(R.id.create_live_stream_button)
  void onCreateLiveStreamButtonClick() {
    createLiveStreamDialogFragment.show(getActivity().getSupportFragmentManager(), CreateLiveStreamDialogFragment.class.getSimpleName());
  }

  private void setupObservers() {
    liveViewModel.getConnectedChannels().observe(getViewLifecycleOwner(), connectedChannelsHolder -> {
      if (!ListUpdateType.NONE.equals(connectedChannelsHolder.getUpdateType())) {
        connectedChannelsAdapter.setConnectedChannels(connectedChannelsHolder.getItems());
        connectedChannelsHolder.applyChanges(connectedChannelsAdapter);
      }
    });
    liveViewModel.getLiveStreams().observe(getViewLifecycleOwner(), liveStreamsHolder -> {
      if (!ListUpdateType.NONE.equals(liveStreamsHolder.getUpdateType())) {
        liveStreamsAdapter.setLiveStreams(liveStreamsHolder.getItems());
        liveStreamsHolder.applyChanges(liveStreamsAdapter);
      }
    });
    liveViewModel.getCreateLiveStream().observe(getViewLifecycleOwner(), liveStream -> createLiveStreamDialogFragment.dismiss());
    liveViewModel.getError().observe(getViewLifecycleOwner(), throwable -> AlertUtils.alert(context, throwable));
  }

  private void setupViews() {
    createLiveStreamButton.setImageDrawable(new IconicsDrawable(context, FontAwesome.Icon.faw_video));

    createLiveStreamDialogFragment = new CreateLiveStreamDialogFragment();
    createLiveStreamDialogFragment.setListener(this);

    connectedChannelsAdapter.setAdapterListener(this);

    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
    SpaceItemDecoration itemDecoration = new SpaceItemDecoration(context.getResources().getDimensionPixelSize(R.dimen.margin_md), layoutManager.getOrientation());
    connectedChannelsView.setLayoutManager(layoutManager);
    connectedChannelsView.addItemDecoration(itemDecoration);
    connectedChannelsView.setAdapter(connectedChannelsAdapter);
    RecyclerView.ItemAnimator itemAnimator = connectedChannelsView.getItemAnimator();
    if (itemAnimator instanceof SimpleItemAnimator) {
      ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
    }
  }

  private void setupContent() {
    liveViewModel.loadConnectedChannels();
    liveViewModel.loadLiveStreams();
  }
}

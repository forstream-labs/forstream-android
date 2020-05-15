package io.livestream.view.main.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.livestream.R;
import io.livestream.api.enums.ChannelIdentifier;
import io.livestream.api.model.ConnectedChannel;
import io.livestream.util.component.SpaceItemDecoration;

public class CreateLiveStreamDialogFragment extends BottomSheetDialogFragment {

  @BindView(R.id.live_stream_title_layout) TextInputLayout liveStreamTitleLayout;
  @BindView(R.id.live_stream_title_input) TextInputEditText liveStreamTitleInput;
  @BindView(R.id.live_stream_description_input) TextInputEditText liveStreamDescriptionInput;
  @BindView(R.id.connected_channels_view) RecyclerView connectedChannelsView;
  @BindView(R.id.create_live_stream_button) Button createLiveStreamButton;

  private Context context;
  private Listener listener;
  private ConnectedChannelsAdapter connectedChannelsAdapter;

  @Inject
  public CreateLiveStreamDialogFragment(Context context, ConnectedChannelsAdapter connectedChannelsAdapter) {
    this.context = context;
    this.connectedChannelsAdapter = connectedChannelsAdapter;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.bottom_sheet_create_live_stream, container, false);
    ButterKnife.bind(this, view);

    setupViews();
    setupConnectedChannelsView();
    return view;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  public void setConnectedChannels(List<ConnectedChannel> connectedChannels) {
    connectedChannelsAdapter.setConnectedChannels(connectedChannels);
  }

  @OnTextChanged(R.id.live_stream_title_input)
  void onLiveStreamTitleInputChanged() {
    liveStreamTitleLayout.setError(liveStreamTitleInput.getText().toString().isEmpty() ? " " : null);
    updateCreateLiveStreamButtonState();
  }

  @OnClick(R.id.create_live_stream_button)
  void onCreateLiveStreamButtonClick() {
    if (listener != null) {
      List<ChannelIdentifier> channelsIdentifiers = new ArrayList<>();
      for (ConnectedChannel connectedChannel : connectedChannelsAdapter.getEnabledConnectedChannels()) {
        channelsIdentifiers.add(connectedChannel.getChannel().getIdentifier());
      }
      listener.onCreateLiveStreamButtonClick(liveStreamTitleInput.getText().toString(), liveStreamDescriptionInput.getText().toString(), channelsIdentifiers);
    }
  }

  private void setupViews() {
    onLiveStreamTitleInputChanged();
    updateCreateLiveStreamButtonState();
  }

  private void setupConnectedChannelsView() {
    connectedChannelsAdapter.setViewType(ConnectedChannelsAdapter.VIEW_STYLE_NONE);
    connectedChannelsAdapter.setShowEnabledSwitch(true);

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

  private void updateCreateLiveStreamButtonState() {
    createLiveStreamButton.setEnabled(!liveStreamTitleInput.getText().toString().isEmpty());
  }

  public interface Listener {

    void onCreateLiveStreamButtonClick(String title, String description, List<ChannelIdentifier> channelsIdentifiers);

  }
}

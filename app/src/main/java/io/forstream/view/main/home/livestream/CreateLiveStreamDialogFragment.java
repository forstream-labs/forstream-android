package io.forstream.view.main.home.livestream;

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

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.forstream.R;
import io.forstream.api.enums.ChannelIdentifier;
import io.forstream.api.model.ConnectedChannel;
import io.forstream.util.component.SpaceItemDecoration;
import io.forstream.view.main.home.connectedchannels.ConnectedChannelsSelectionAdapter;

public class CreateLiveStreamDialogFragment extends BottomSheetDialogFragment implements ConnectedChannelsSelectionAdapter.Listener {

  @BindView(R.id.live_stream_title_layout) TextInputLayout liveStreamTitleLayout;
  @BindView(R.id.live_stream_title_input) TextInputEditText liveStreamTitleInput;
  @BindView(R.id.live_stream_description_input) TextInputEditText liveStreamDescriptionInput;
  @BindView(R.id.connected_channels_view) RecyclerView connectedChannelsView;
  @BindView(R.id.create_live_stream_button) Button createLiveStreamButton;

  private Context context;
  private Listener listener;
  private ConnectedChannelsSelectionAdapter connectedChannelsSelectionAdapter;

  @Inject
  public CreateLiveStreamDialogFragment(Context context, ConnectedChannelsSelectionAdapter connectedChannelsSelectionAdapter) {
    this.context = context;
    this.connectedChannelsSelectionAdapter = connectedChannelsSelectionAdapter;
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
    connectedChannelsSelectionAdapter.setConnectedChannels(connectedChannels);
  }

  @Override
  public void onConnectedChannelCheckedChanged(ConnectedChannel connectedChannel, boolean checked) {
    updateCreateLiveStreamButtonState();
  }

  @OnTextChanged(R.id.live_stream_title_input)
  void onLiveStreamTitleInputChanged() {
    liveStreamTitleLayout.setError(liveStreamTitleInput.getText().toString().isEmpty() ? " " : null);
    updateCreateLiveStreamButtonState();
  }

  @OnClick(R.id.create_live_stream_button)
  void onCreateLiveStreamButtonClick() {
    if (listener != null) {
      List<ChannelIdentifier> channelsIdentifiers = connectedChannelsSelectionAdapter.getEnabledChannels();
      listener.onCreateLiveStreamButtonClick(liveStreamTitleInput.getText().toString(), liveStreamDescriptionInput.getText().toString(), channelsIdentifiers);
    }
  }

  private void setupViews() {
    onLiveStreamTitleInputChanged();
    updateCreateLiveStreamButtonState();
  }

  private void setupConnectedChannelsView() {
    connectedChannelsSelectionAdapter.setListener(this);
    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
    SpaceItemDecoration itemDecoration = new SpaceItemDecoration(context.getResources().getDimensionPixelSize(R.dimen.margin_sm), layoutManager.getOrientation());
    connectedChannelsView.setLayoutManager(layoutManager);
    connectedChannelsView.addItemDecoration(itemDecoration);
    connectedChannelsView.setAdapter(connectedChannelsSelectionAdapter);
    RecyclerView.ItemAnimator itemAnimator = connectedChannelsView.getItemAnimator();
    if (itemAnimator instanceof SimpleItemAnimator) {
      ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
    }
  }

  private void updateCreateLiveStreamButtonState() {
    boolean noChannelsEnabled = connectedChannelsSelectionAdapter.getEnabledChannels().isEmpty();
    boolean emptyTitle = liveStreamTitleInput.getText().toString().isEmpty();
    createLiveStreamButton.setEnabled(!emptyTitle && !noChannelsEnabled);
  }

  public interface Listener {

    void onCreateLiveStreamButtonClick(String title, String description, List<ChannelIdentifier> channelsIdentifiers);

  }
}

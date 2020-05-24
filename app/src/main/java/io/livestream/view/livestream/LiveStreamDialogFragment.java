package io.livestream.view.livestream;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.livestream.R;
import io.livestream.api.enums.StreamStatus;
import io.livestream.api.model.LiveStream;
import io.livestream.api.model.ProviderStream;
import io.livestream.common.Constants;
import io.livestream.util.AlertUtils;
import io.livestream.util.AppUtils;
import io.livestream.util.ImageUtils;
import io.livestream.util.UIUtils;
import io.livestream.util.component.SpaceItemDecoration;

public class LiveStreamDialogFragment extends BottomSheetDialogFragment implements ProviderStreamsAdapter.Listener {

  @BindView(R.id.live_stream_thumbnail) ImageView liveStreamThumbnailView;
  @BindView(R.id.live_stream_title) TextView liveStreamTitleView;
  @BindView(R.id.live_stream_description) TextView liveStreamDescriptionView;
  @BindView(R.id.live_stream_status) TextView liveStreamStatusView;
  @BindView(R.id.live_stream_date) TextView liveStreamDateView;
  @BindView(R.id.provider_streams_view) RecyclerView providerStreamsView;

  private Context context;
  private LiveStreamViewModel liveStreamViewModel;
  private ProviderStreamsAdapter providerStreamsAdapter;

  private LiveStream liveStream;

  @Inject
  public LiveStreamDialogFragment(Context context, LiveStreamViewModel liveStreamViewModel, ProviderStreamsAdapter providerStreamsAdapter) {
    this.context = context;
    this.liveStreamViewModel = liveStreamViewModel;
    this.providerStreamsAdapter = providerStreamsAdapter;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(STYLE_NORMAL, R.style.LiveStreamBottomSheet);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.bottom_sheet_live_stream, container, false);
    ButterKnife.bind(this, view);

    setupLiveStream();
    setupObservers();
    setupViews();
    setupContent();
    return view;
  }

  @Override
  public void show(@NonNull FragmentManager manager, @Nullable String tag) {
    super.show(manager, tag);
  }

  @Override
  public void onProviderStreamEnabledChanged(ProviderStream providerStream) {
    if (providerStream.getEnabled()) {
      liveStreamViewModel.enableLiveStreamProvider(liveStream, providerStream);
    } else {
      liveStreamViewModel.disableLiveStreamProvider(liveStream, providerStream);
    }
  }

  private void setupLiveStream() {
    liveStream = (LiveStream) getArguments().getSerializable(Constants.LIVE_STREAM);
  }

  private void setupObservers() {
    liveStreamViewModel.getEnableDisableLiveStream().observe(this, liveStream -> {
      this.liveStream = liveStream;
      providerStreamsAdapter.setProviderStreams(liveStream.getProviders());
      providerStreamsAdapter.setStateSwitchEnabled(false);
      providerStreamsAdapter.notifyDataSetChanged();
    });
    liveStreamViewModel.getError().observe(this, throwable -> AlertUtils.alert(context, throwable));
  }

  private void setupViews() {
    providerStreamsAdapter.setListener(this);

    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
    SpaceItemDecoration itemDecoration = new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.margin_sm), layoutManager.getOrientation());
    providerStreamsView.setLayoutManager(layoutManager);
    providerStreamsView.addItemDecoration(itemDecoration);
    providerStreamsView.setAdapter(providerStreamsAdapter);
    RecyclerView.ItemAnimator itemAnimator = providerStreamsView.getItemAnimator();
    if (itemAnimator instanceof SimpleItemAnimator) {
      ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
    }
  }

  private void setupContent() {
    updateContent();
    providerStreamsAdapter.setProviderStreams(liveStream.getProviders());
    providerStreamsAdapter.setStateSwitchEnabled(!liveStream.getStatus().equals(StreamStatus.COMPLETE));
    providerStreamsAdapter.notifyDataSetChanged();
  }

  private void updateContent() {
    ImageUtils.loadImage(context, liveStreamViewModel.getAuthenticatedUser(), liveStreamThumbnailView);
    liveStreamTitleView.setText(liveStream.getTitle());
    liveStreamDescriptionView.setText(liveStream.getDescription());
    liveStreamStatusView.setText(AppUtils.getStreamStatusName(context, liveStream.getStatus(), null));
    UIUtils.setColorFilter(liveStreamStatusView.getBackground(), AppUtils.getStreamStatusColor(context, liveStream.getStatus(), null));

    if (liveStream.getStartDate() != null) {
      liveStreamDateView.setVisibility(View.VISIBLE);
      if (liveStream.getEndDate() != null) {
        liveStreamDateView.setText(DateUtils.formatDateRange(context, liveStream.getStartDate().getTime(), liveStream.getEndDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_NO_YEAR));
      } else {
        liveStreamDateView.setText(DateUtils.formatDateTime(context, liveStream.getStartDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_NO_YEAR));
      }
    } else {
      liveStreamDateView.setVisibility(View.GONE);
    }
  }
}

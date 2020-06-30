package io.forstream.view.livestream;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import io.forstream.R;
import io.forstream.api.model.LiveStream;
import io.forstream.common.Constants;
import io.forstream.util.AppUtils;
import io.forstream.util.StringUtils;
import io.forstream.util.UIUtils;
import io.forstream.util.component.SpaceItemDecoration;

public class LiveStreamDialogFragment extends BottomSheetDialogFragment {

  @BindView(R.id.live_stream_title) TextView liveStreamTitleView;
  @BindView(R.id.live_stream_description) TextView liveStreamDescriptionView;
  @BindView(R.id.live_stream_status) TextView liveStreamStatusView;
  @BindView(R.id.live_stream_date) TextView liveStreamDateView;
  @BindView(R.id.provider_streams_view) RecyclerView providerStreamsView;

  private Context context;
  private ProviderStreamsAdapter providerStreamsAdapter;

  private LiveStream liveStream;

  @Inject
  public LiveStreamDialogFragment(Context context, ProviderStreamsAdapter providerStreamsAdapter) {
    this.context = context;
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
    setupViews();
    setupContent();
    return view;
  }

  @Override
  public void show(@NonNull FragmentManager manager, @Nullable String tag) {
    super.show(manager, tag);
  }

  private void setupLiveStream() {
    liveStream = (LiveStream) getArguments().getSerializable(Constants.LIVE_STREAM);
  }

  private void setupViews() {
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
    providerStreamsAdapter.notifyDataSetChanged();
  }

  private void updateContent() {
    liveStreamTitleView.setText(liveStream.getTitle());
    liveStreamDescriptionView.setText(liveStream.getDescription());
    liveStreamDescriptionView.setVisibility(!StringUtils.isEmpty(liveStream.getDescription()) ? View.VISIBLE : View.GONE);
    liveStreamStatusView.setText(AppUtils.getStreamStatusName(context, liveStream.getStreamStatus(), null));
    UIUtils.setColorFilter(liveStreamStatusView.getBackground(), AppUtils.getStreamStatusColor(context, liveStream.getStreamStatus(), null));

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

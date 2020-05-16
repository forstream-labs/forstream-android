package io.livestream.view.main.home;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.livestream.R;
import io.livestream.api.model.LiveStream;
import io.livestream.common.adapter.base.BaseViewHolder;
import io.livestream.service.AuthenticatedUser;
import io.livestream.util.AppUtils;
import io.livestream.util.ImageUtils;
import io.livestream.util.UIUtils;

public class LiveStreamsAdapter extends RecyclerView.Adapter<LiveStreamsAdapter.ViewHolder> {

  private Context context;
  private AuthenticatedUser authenticatedUser;
  private Listener listener;
  private List<LiveStream> liveStreams;

  @Inject
  public LiveStreamsAdapter(Context context, AuthenticatedUser authenticatedUser) {
    this.context = context;
    this.authenticatedUser = authenticatedUser;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  public void setLiveStreams(List<LiveStream> liveStreams) {
    this.liveStreams = liveStreams;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_live_stream, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    LiveStream liveStream = liveStreams.get(position);
    holder.bindView(liveStream);
  }

  @Override
  public int getItemCount() {
    return liveStreams != null ? liveStreams.size() : 0;
  }

  public interface Listener {

    void onLiveStreamClick(LiveStream liveStream);

  }

  class ViewHolder extends BaseViewHolder<LiveStream> {

    @BindView(R.id.live_stream_thumbnail) ImageView liveStreamThumbnail;
    @BindView(R.id.live_stream_title) TextView liveStreamTitle;
    @BindView(R.id.live_stream_description) TextView liveStreamDescription;
    @BindView(R.id.live_stream_status) TextView liveStreamStatus;
    @BindView(R.id.live_stream_date) TextView liveStreamDate;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(LiveStream liveStream) {
      ImageUtils.loadImage(context, authenticatedUser.get(), liveStreamThumbnail);
      liveStreamTitle.setText(liveStream.getTitle());
      liveStreamDescription.setText(liveStream.getDescription());

      liveStreamStatus.setText(AppUtils.getStreamStatusName(context, liveStream.getStatus()));
      int streamStatusColor = 0;
      switch (liveStream.getStatus()) {
        case READY:
          streamStatusColor = ContextCompat.getColor(context, R.color.stream_status_ready);
          break;
        case LIVE:
          streamStatusColor = ContextCompat.getColor(context, R.color.stream_status_live);
          break;
        case COMPLETE:
          streamStatusColor = ContextCompat.getColor(context, R.color.stream_status_complete);
          break;
      }
      UIUtils.setColorFilter(liveStreamStatus.getBackground(), streamStatusColor);

      if (liveStream.getStartDate() != null) {
        liveStreamDate.setVisibility(View.VISIBLE);
        if (liveStream.getEndDate() != null) {
          liveStreamDate.setText(DateUtils.formatDateRange(context, liveStream.getStartDate().getTime(), liveStream.getEndDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_NO_YEAR));
        } else {
          liveStreamDate.setText(DateUtils.formatDateTime(context, liveStream.getStartDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_NO_YEAR));
        }
      } else {
        liveStreamDate.setVisibility(View.GONE);
      }
    }

    @OnClick(R.id.live_stream_view)
    void onLiveStreamViewClick() {
      if (listener != null) {
        listener.onLiveStreamClick(liveStreams.get(getAdapterPosition()));
      }
    }
  }
}
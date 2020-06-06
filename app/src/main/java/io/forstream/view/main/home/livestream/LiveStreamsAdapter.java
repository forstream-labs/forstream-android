package io.forstream.view.main.home.livestream;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.forstream.R;
import io.forstream.api.model.LiveStream;
import io.forstream.common.adapter.base.BaseViewHolder;
import io.forstream.service.AuthenticatedUser;
import io.forstream.util.AppUtils;
import io.forstream.util.ImageUtils;
import io.forstream.util.StringUtils;
import io.forstream.util.UIUtils;

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

    void onRemoveLiveStreamClick(LiveStream liveStream);

  }

  class ViewHolder extends BaseViewHolder<LiveStream> {

    @BindView(R.id.live_stream_thumbnail) ImageView liveStreamThumbnail;
    @BindView(R.id.live_stream_title) TextView liveStreamTitleView;
    @BindView(R.id.live_stream_description) TextView liveStreamDescriptionView;
    @BindView(R.id.live_stream_status) TextView liveStreamStatusView;
    @BindView(R.id.live_stream_date) TextView liveStreamDateView;
    @BindView(R.id.live_stream_menu) View liveStreamMenuView;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(LiveStream liveStream) {
      ImageUtils.loadImage(context, authenticatedUser.get(), liveStreamThumbnail);
      liveStreamTitleView.setText(liveStream.getTitle());
      liveStreamDescriptionView.setText(liveStream.getDescription());
      liveStreamDescriptionView.setVisibility(!StringUtils.isEmpty(liveStream.getDescription()) ? View.VISIBLE : View.GONE);
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

    @OnClick(R.id.live_stream_view)
    void onLiveStreamViewClick() {
      if (listener != null) {
        listener.onLiveStreamClick(liveStreams.get(getAdapterPosition()));
      }
    }

    @OnClick(R.id.live_stream_menu)
    void onLiveStreamMenuClick() {
      LiveStream liveStream = liveStreams.get(getAdapterPosition());
      PopupMenu popup = new PopupMenu(context, liveStreamMenuView);
      popup.inflate(R.menu.menu_connected_channel);
      popup.setOnMenuItemClickListener(item -> {
        if (item.getItemId() == R.id.remove_connected_channel) {
          MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context)
            .setTitle(R.string.activity_main_dialog_remove_live_stream_message)
            .setPositiveButton(R.string.remove, (dialog, which) -> listener.onRemoveLiveStreamClick(liveStream))
            .setNegativeButton(R.string.cancel, null);
          dialogBuilder.create().show();
          return true;
        }
        return false;
      });
      popup.show();
    }
  }
}
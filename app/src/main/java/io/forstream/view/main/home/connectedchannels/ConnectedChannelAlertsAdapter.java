package io.forstream.view.main.home.connectedchannels;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.annimon.stream.Stream;
import com.mikepenz.iconics.IconicsColorRes;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSizeRes;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.forstream.R;
import io.forstream.api.model.ChannelAlert;
import io.forstream.api.model.ConnectedChannel;
import io.forstream.common.adapter.base.BaseViewHolder;
import io.forstream.util.AppUtils;

public class ConnectedChannelAlertsAdapter extends RecyclerView.Adapter<ConnectedChannelAlertsAdapter.ViewHolder> {

  private Context context;
  private Listener listener;
  private ConnectedChannel connectedChannel;
  private List<ChannelAlert> channelAlerts;

  public ConnectedChannelAlertsAdapter(Context context) {
    this.context = context;
  }

  public void setConnectedChannel(ConnectedChannel connectedChannel) {
    this.connectedChannel = connectedChannel;
    this.channelAlerts = Stream.of(connectedChannel.getAlerts()).filter(channelAlert -> {
      String description = AppUtils.getChannelAlertString(context, connectedChannel.getChannel(), channelAlert);
      return description != null && !channelAlert.isChecked();
    }).toList();
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_connected_channel_alert, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ChannelAlert channelAlert = channelAlerts.get(position);
    holder.bindView(channelAlert);
  }

  @Override
  public int getItemCount() {
    return channelAlerts.size();
  }

  public interface Listener {

    void onChannelAlertClick(ConnectedChannel connectedChannel, ChannelAlert channelAlert);

  }

  class ViewHolder extends BaseViewHolder<ChannelAlert> {

    @BindView(R.id.connected_channel_alert_icon) ImageView alertIconView;
    @BindView(R.id.connected_channel_alert_description) TextView alertDescriptionView;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(ChannelAlert channelAlert) {
      Drawable alertTypeDrawable;
      switch (channelAlert.getType()) {
        case WARNING:
          alertTypeDrawable = new IconicsDrawable(context, FontAwesome.Icon.faw_exclamation_triangle).size(new IconicsSizeRes(R.dimen.image_xsm)).color(new IconicsColorRes(R.color.warning));
          break;
        case ERROR:
          alertTypeDrawable = new IconicsDrawable(context, FontAwesome.Icon.faw_times_circle).size(new IconicsSizeRes(R.dimen.image_xsm)).color(new IconicsColorRes(R.color.error));
          break;
        default:
          alertTypeDrawable = new IconicsDrawable(context, FontAwesome.Icon.faw_info_circle).size(new IconicsSizeRes(R.dimen.image_xsm)).color(new IconicsColorRes(R.color.info));
          break;
      }
      alertIconView.setImageDrawable(alertTypeDrawable);
      String description = AppUtils.getChannelAlertString(context, connectedChannel.getChannel(), channelAlert);
      alertDescriptionView.setText(description);
    }

    @OnClick(R.id.channel_alert)
    void onChannelAlertClick() {
      ChannelAlert channelAlert = channelAlerts.get(getAdapterPosition());
      if (listener != null) {
        listener.onChannelAlertClick(connectedChannel, channelAlert);
      }
    }
  }
}

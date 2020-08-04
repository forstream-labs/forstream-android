package io.forstream.view.main.home.livestream;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.forstream.R;
import io.forstream.api.model.Channel;
import io.forstream.api.model.LiveStream;
import io.forstream.api.model.ProviderMessage;
import io.forstream.api.model.ProviderStream;
import io.forstream.common.adapter.base.BaseViewHolder;
import io.forstream.util.AppUtils;
import io.forstream.util.ImageUtils;

public class LiveStreamAlertsAdapter extends RecyclerView.Adapter<LiveStreamAlertsAdapter.ViewHolder> {

  private Context context;
  private Listener listener;
  private List<ProviderMessage> providersMessages = new ArrayList<>();
  private Map<ProviderMessage, ProviderStream> providerByProviderMessage = new HashMap<>();

  @Inject
  public LiveStreamAlertsAdapter(Context context) {
    this.context = context;
  }

  public void setLiveStream(LiveStream liveStream) {
    this.providerByProviderMessage.clear();
    this.providersMessages = new ArrayList<>();
    for (ProviderStream provider : liveStream.getProviders()) {
      List<ProviderMessage> providerMessages = Stream.of(provider.getMessages()).filter(providerMessage -> {
        String description = AppUtils.getProviderStreamMessageString(context, provider.getChannel(), providerMessage);
        if (description != null) {
          providerByProviderMessage.put(providerMessage, provider);
          return true;
        }
        return false;
      }).toList();
      this.providersMessages.addAll(providerMessages);
    }
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_provider_stream_message, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ProviderMessage providerMessage = providersMessages.get(position);
    holder.bindView(providerMessage);
  }

  @Override
  public int getItemCount() {
    return providersMessages.size();
  }

  public interface Listener {

    void onProviderMessageClick(ProviderStream providerStream, ProviderMessage providerMessage);

  }

  class ViewHolder extends BaseViewHolder<ProviderMessage> {

    @BindView(R.id.provider_message_icon) ImageView providerMessageIconView;
    @BindView(R.id.provider_message_description) TextView providerMessageDescriptionView;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(ProviderMessage providerMessage) {
      Channel channel = providerByProviderMessage.get(providerMessage).getChannel();
      ImageUtils.loadImage(context, channel, providerMessageIconView);
      String description = AppUtils.getProviderStreamMessageString(context, channel, providerMessage);
      providerMessageDescriptionView.setText(description);
    }

    @OnClick(R.id.provider_message_view)
    void onProviderMessageClick() {
      ProviderMessage providerMessage = providersMessages.get(getAdapterPosition());
      if (listener != null) {
        listener.onProviderMessageClick(providerByProviderMessage.get(providerMessage), providerMessage);
      }
    }
  }
}

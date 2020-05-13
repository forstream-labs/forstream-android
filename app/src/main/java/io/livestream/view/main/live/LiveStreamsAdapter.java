package io.livestream.view.main.live;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.livestream.R;
import io.livestream.api.model.LiveStream;
import io.livestream.common.adapter.base.BaseViewHolder;

public class LiveStreamsAdapter extends RecyclerView.Adapter<LiveStreamsAdapter.ViewHolder> {

  private Context context;
  private LiveStreamsAdapter.AdapterListener adapterListener;
  private List<LiveStream> liveStreams;

  @Inject
  public LiveStreamsAdapter(Context context) {
    this.context = context;
  }

  public void setAdapterListener(LiveStreamsAdapter.AdapterListener adapterListener) {
    this.adapterListener = adapterListener;
  }

  public void setLiveStreams(List<LiveStream> liveStreams) {
    this.liveStreams = liveStreams;
  }

  @NonNull
  @Override
  public LiveStreamsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_live_stream, parent, false);
    return new LiveStreamsAdapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull LiveStreamsAdapter.ViewHolder holder, int position) {
    LiveStream liveStream = liveStreams.get(position);
    holder.bindView(liveStream);
  }

  @Override
  public int getItemCount() {
    return liveStreams != null ? liveStreams.size() : 0;
  }

  public interface AdapterListener {

    void onLiveStreamClick(LiveStream liveStream);

  }

  class ViewHolder extends BaseViewHolder<LiveStream> {

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(LiveStream liveStream) {

    }

    @OnClick(R.id.live_stream_view)
    void onLiveStreamViewClick() {
      if (adapterListener != null) {
        adapterListener.onLiveStreamClick(liveStreams.get(getAdapterPosition()));
      }
    }
  }
}
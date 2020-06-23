package io.forstream.view.main.home;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.forstream.api.enums.ChannelIdentifier;
import io.forstream.api.model.ConnectedChannel;
import io.forstream.api.model.LiveStream;
import io.forstream.api.service.ChannelService;
import io.forstream.api.service.StreamService;
import io.forstream.api.service.UserService;
import io.forstream.common.livedata.SingleLiveData;
import io.forstream.common.livedata.list.ListHolder;
import io.forstream.common.livedata.list.ListLiveData;
import io.forstream.common.viewmodel.BaseViewModel;
import io.forstream.service.NotificationService;
import timber.log.Timber;

public class HomeViewModel extends BaseViewModel implements NotificationService.ChannelSubscriber, NotificationService.LiveStreamSubscriber {

  private static final String CONNECTED_CHANNELS_POPULATE = "channel";
  private static final String LIVE_STREAM_POPULATE = "providers->channel providers->connected_channel";

  private UserService userService;
  private ChannelService channelService;
  private StreamService streamService;
  private NotificationService notificationService;

  private ListLiveData<ConnectedChannel> connectedChannels = new ListLiveData<>();
  private ListLiveData<LiveStream> liveStreams = new ListLiveData<>();
  private SingleLiveData<LiveStream> createLiveStream = new SingleLiveData<>();

  public HomeViewModel(UserService userService, ChannelService channelService, StreamService streamService, NotificationService notificationService) {
    this.userService = userService;
    this.channelService = channelService;
    this.streamService = streamService;
    this.notificationService = notificationService;

    notificationService.subscribeChannel(this);
    notificationService.subscribeLiveStream(this);
  }

  public LiveData<ListHolder<ConnectedChannel>> getConnectedChannels() {
    return connectedChannels;
  }

  public LiveData<ListHolder<LiveStream>> getLiveStreams() {
    return liveStreams;
  }

  public LiveData<LiveStream> getCreateLiveStream() {
    return createLiveStream;
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    notificationService.unsubscribeChannel(this);
  }

  @Override
  public void onChannelConnected(ConnectedChannel connectedChannel) {
    loadConnectedChannels();
  }

  @Override
  public void onChannelDisconnected(ConnectedChannel connectedChannel) {

  }

  @Override
  public void onLiveStreamUpdated(LiveStream liveStream) {
    liveStreams.set(liveStream);
  }

  public void loadConnectedChannels() {
    userService.listMyConnectedChannels(CONNECTED_CHANNELS_POPULATE).then(connectedChannels -> {
      this.connectedChannels.postValue(connectedChannels);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error loading connected channels");
      error.postValue(reason);
      return null;
    });
  }

  public void loadLiveStreams() {
    userService.listMyLiveStreams(LIVE_STREAM_POPULATE).then(liveStreams -> {
      this.liveStreams.postValue(liveStreams);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error loading live streams");
      error.postValue(reason);
      return null;
    });
  }

  public void disconnectChannel(ConnectedChannel connectedChannel) {
    channelService.disconnectChannel(connectedChannel.getChannel()).then(liveStream -> {
      notificationService.notifyChannelDisconnected(connectedChannel);
      connectedChannels.remove(connectedChannel);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error disconnecting channel %s", connectedChannel.getChannel().getIdentifier());
      error.postValue(reason);
      return null;
    }));
  }

  public void createLiveStream(String title, String description, List<ChannelIdentifier> channelsIdentifiers) {
    streamService.createLiveStream(title, description, channelsIdentifiers).then(liveStream -> {
      liveStreams.add(0, liveStream);
      createLiveStream.postValue(liveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error creating live stream");
      error.postValue(reason);
      return null;
    }));
  }

  public void removeLiveStream(LiveStream liveStream) {
    streamService.removeLiveStream(liveStream).then(aVoid -> {
      liveStreams.remove(liveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error removing live stream %s", liveStream.getId());
      error.postValue(reason);
      return null;
    }));
  }
}

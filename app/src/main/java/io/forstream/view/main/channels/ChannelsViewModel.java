package io.forstream.view.main.channels;

import androidx.lifecycle.LiveData;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.onehilltech.promises.Promise;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.forstream.api.model.Channel;
import io.forstream.api.model.ChannelTarget;
import io.forstream.api.model.ConnectedChannel;
import io.forstream.api.service.ChannelService;
import io.forstream.api.service.UserService;
import io.forstream.common.livedata.SingleLiveData;
import io.forstream.common.livedata.list.ListHolder;
import io.forstream.common.livedata.list.ListLiveData;
import io.forstream.common.viewmodel.BaseViewModel;
import io.forstream.service.NotificationService;
import timber.log.Timber;

public class ChannelsViewModel extends BaseViewModel implements NotificationService.ChannelSubscriber {

  private UserService userService;
  private ChannelService channelService;
  private NotificationService notificationService;

  private ListLiveData<ViewItem> viewItems = new ListLiveData<>();
  private SingleLiveData<List<ChannelTarget>> facebookTargets = new SingleLiveData<>();
  private SingleLiveData<ConnectedChannel> channelConnected = new SingleLiveData<>();

  public ChannelsViewModel(UserService userService, ChannelService channelService, NotificationService notificationService) {
    this.userService = userService;
    this.channelService = channelService;
    this.notificationService = notificationService;

    notificationService.subscribeChannel(this);
  }

  public LiveData<ListHolder<ViewItem>> getViewItems() {
    return viewItems;
  }

  public LiveData<List<ChannelTarget>> getFacebookTargets() {
    return facebookTargets;
  }

  public LiveData<ConnectedChannel> getChannelConnected() {
    return channelConnected;
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    notificationService.unsubscribeChannel(this);
  }

  @Override
  public void onChannelConnected(ConnectedChannel connectedChannel) {

  }

  @Override
  public void onChannelDisconnected(ConnectedChannel connectedChannel) {
    ViewItem viewItem = new ViewItem(connectedChannel.getChannel());
    int index = viewItems.indexOf(viewItem);
    if (index >= 0) {
      viewItem = viewItems.get(index);
      viewItem.setConnected(false);
      viewItems.set(index, viewItem);
    }
  }

  @SuppressWarnings("unchecked")
  public void loadViewItems() {
    channelService.listChannels().then(channels -> Promise.all(Promise.value(channels), userService.listMyConnectedChannels())).then(value -> {
      List<Channel> channels = (List<Channel>) value.get(0);
      List<ConnectedChannel> connectedChannels = (List<ConnectedChannel>) value.get(1);
      List<ViewItem> viewItems = new ArrayList<>();
      for (Channel channel : channels) {
        ViewItem viewItem = new ViewItem(channel);
        Optional<ConnectedChannel> connectedChannelOptional = Stream.of(connectedChannels)
          .filter(currentConnectedChannel -> currentConnectedChannel.getChannel().equals(channel))
          .findFirst();
        viewItem.setConnected(connectedChannelOptional.isPresent());
        viewItems.add(viewItem);
      }
      this.viewItems.postValue(viewItems);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error loading channels");
      error.postValue(reason);
      return null;
    }));
  }

  public void connectYouTubeChannel(String authorizationCode) {
    channelService.connectYouTubeChannel(authorizationCode).then(connectedChannel -> {
      updateChannelConnected(connectedChannel);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error connecting YouTube channel");
      error.postValue(reason);
      return null;
    }));
  }

  public void connectTwitchChannel(String authorizationCode) {
    channelService.connectTwitchChannel(authorizationCode).then(connectedChannel -> {
      updateChannelConnected(connectedChannel);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error connecting Twitch channel");
      error.postValue(reason);
      return null;
    }));
  }

  public void listFacebookTargets(String accessToken) {
    channelService.listFacebookChannelTargets(accessToken).then(facebookTargets -> {
      this.facebookTargets.postValue(facebookTargets);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error listing Facebook targets");
      error.postValue(reason);
      return null;
    }));
  }

  public void connectFacebookChannel(String accessToken, String targetId) {
    channelService.connectFacebookChannel(accessToken, targetId).then(connectedChannel -> {
      updateChannelConnected(connectedChannel);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error connecting Facebook channel");
      error.postValue(reason);
      return null;
    }));
  }

  private void updateChannelConnected(ConnectedChannel connectedChannel) {
    ViewItem viewItem = new ViewItem(connectedChannel.getChannel());
    int index = viewItems.indexOf(viewItem);
    if (index >= 0) {
      viewItem = viewItems.get(index);
      viewItem.setConnected(true);
      viewItems.set(index, viewItem);
    }
    channelConnected.postValue(connectedChannel);
    notificationService.notifyChannelConnected(connectedChannel);
  }

  public static class ViewItem {

    private Channel channel;
    private boolean connected;

    public ViewItem(Channel channel) {
      this.channel = channel;
    }

    public Channel getChannel() {
      return channel;
    }

    public void setChannel(Channel channel) {
      this.channel = channel;
    }

    public boolean isConnected() {
      return connected;
    }

    public void setConnected(boolean connected) {
      this.connected = connected;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      ViewItem viewItem = (ViewItem) o;
      return channel.equals(viewItem.channel);
    }

    @Override
    public int hashCode() {
      return Objects.hash(channel);
    }
  }
}

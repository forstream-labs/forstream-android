package io.livestream.service;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import io.livestream.api.model.ConnectedChannel;
import io.livestream.api.model.LiveStream;
import io.livestream.dagger.scope.AppScope;

@AppScope
public class NotificationService {

  private Set<ChannelSubscriber> channelSubscribers = new HashSet<>();
  private Set<LiveStreamSubscriber> liveStreamSubscribers = new HashSet<>();

  @Inject
  public NotificationService() {

  }

  public void subscribeChannel(ChannelSubscriber subscriber) {
    channelSubscribers.add(subscriber);
  }

  public void unsubscribeChannel(ChannelSubscriber subscriber) {
    channelSubscribers.remove(subscriber);
  }

  public void subscribeLiveStream(LiveStreamSubscriber subscriber) {
    liveStreamSubscribers.add(subscriber);
  }

  public void unsubscribeLiveStream(LiveStreamSubscriber subscriber) {
    liveStreamSubscribers.remove(subscriber);
  }

  public void notifyChannelConnected(ConnectedChannel connectedChannel) {
    for (ChannelSubscriber subscriber : channelSubscribers) {
      subscriber.onChannelConnected(connectedChannel);
    }
  }

  public void notifyChannelDisconnected(ConnectedChannel connectedChannel) {
    for (ChannelSubscriber subscriber : channelSubscribers) {
      subscriber.onChannelDisconnected(connectedChannel);
    }
  }

  public void notifyLiveStreamUpdated(LiveStream liveStream) {
    for (LiveStreamSubscriber subscriber : liveStreamSubscribers) {
      subscriber.onLiveStreamUpdated(liveStream);
    }
  }

  public interface ChannelSubscriber {

    void onChannelConnected(ConnectedChannel connectedChannel);

    void onChannelDisconnected(ConnectedChannel connectedChannel);

  }

  public interface LiveStreamSubscriber {

    void onLiveStreamUpdated(LiveStream liveStream);

  }
}
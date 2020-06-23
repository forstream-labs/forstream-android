package io.forstream.view.livestream;

import androidx.lifecycle.LiveData;

import io.forstream.api.model.LiveStream;
import io.forstream.api.model.ProviderStream;
import io.forstream.api.model.User;
import io.forstream.api.service.StreamService;
import io.forstream.common.livedata.SingleLiveData;
import io.forstream.common.viewmodel.BaseViewModel;
import io.forstream.service.AuthenticatedUser;
import io.forstream.service.NotificationService;
import timber.log.Timber;

public class LiveStreamViewModel extends BaseViewModel {

  private static final String LIVE_STREAM_POPULATE = "providers->channel providers->connected_channel";

  private AuthenticatedUser authenticatedUser;
  private StreamService streamService;
  private NotificationService notificationService;

  private SingleLiveData<LiveStream> liveStream = new SingleLiveData<>();
  private SingleLiveData<LiveStream> startLiveStream = new SingleLiveData<>();
  private SingleLiveData<LiveStream> endLiveStream = new SingleLiveData<>();
  private SingleLiveData<LiveStream> enableDisableLiveStream = new SingleLiveData<>();

  public LiveStreamViewModel(AuthenticatedUser authenticatedUser, StreamService streamService, NotificationService notificationService) {
    this.authenticatedUser = authenticatedUser;
    this.streamService = streamService;
    this.notificationService = notificationService;
  }

  public LiveData<LiveStream> getLiveStream() {
    return liveStream;
  }

  public LiveData<LiveStream> getStartLiveStream() {
    return startLiveStream;
  }

  public LiveData<LiveStream> getEndLiveStream() {
    return endLiveStream;
  }

  public LiveData<LiveStream> getEnableDisableLiveStream() {
    return enableDisableLiveStream;
  }

  public User getAuthenticatedUser() {
    return authenticatedUser.get();
  }

  public void loadLiveStream(String id) {
    streamService.getLiveStream(id, LIVE_STREAM_POPULATE).then(liveStream -> {
      this.liveStream.postValue(liveStream);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error loading live stream");
      return null;
    });
  }

  public void startLiveStream(LiveStream liveStream) {
    streamService.startLiveStream(liveStream).then(updatedLiveStream -> {
      startLiveStream.postValue(updatedLiveStream);
      notificationService.notifyLiveStreamUpdated(updatedLiveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error starting live stream %s", liveStream.getId());
      error.postValue(reason);
      return null;
    }));
  }

  public void endLiveStream(LiveStream liveStream) {
    streamService.endLiveStream(liveStream).then(updatedLiveStream -> {
      endLiveStream.postValue(updatedLiveStream);
      notificationService.notifyLiveStreamUpdated(updatedLiveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error ending live stream %s", liveStream.getId());
      error.postValue(reason);
      return null;
    }));
  }

  public void enableLiveStreamProvider(LiveStream liveStream, ProviderStream providerStream) {
    streamService.enableLiveStreamProvider(liveStream, providerStream).then(updatedLiveStream -> {
      enableDisableLiveStream.postValue(updatedLiveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error enabling provider %s of live stream %s ", providerStream.getChannel().getIdentifier(), liveStream.getId());
      error.postValue(reason);
      return null;
    }));
  }

  public void disableLiveStreamProvider(LiveStream liveStream, ProviderStream providerStream) {
    streamService.disableLiveStreamProvider(liveStream, providerStream).then(updatedLiveStream -> {
      enableDisableLiveStream.postValue(updatedLiveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error disabling provider %s of live stream %s ", providerStream.getChannel().getIdentifier(), liveStream.getId());
      error.postValue(reason);
      return null;
    }));
  }
}
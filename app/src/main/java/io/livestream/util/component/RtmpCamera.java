package io.livestream.util.component;

import android.media.MediaCodec;
import android.view.SurfaceView;

import com.pedro.rtplibrary.base.Camera2Base;

import net.ossrs.rtmp.ConnectCheckerRtmp;
import net.ossrs.rtmp.SrsFlvMuxer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class RtmpCamera extends Camera2Base {

  private ConnectCheckerRtmp connectChecker;
  private List<SrsFlvMuxer> muxers = new ArrayList<>();

  public RtmpCamera(SurfaceView surfaceView, ConnectCheckerRtmp connectChecker) {
    super(surfaceView);
    this.connectChecker = connectChecker;
  }

  public void setupMuxers(int count) {
    for (int i = 0; i < count; i++) {
      muxers.add(new SrsFlvMuxer(connectChecker));
    }
  }

  @Override
  public void setAuthorization(String user, String password) {

  }

  @Override
  public boolean shouldRetry(String reason) {
    return !muxers.isEmpty() && muxers.get(0).shouldRetry(reason);
  }

  @Override
  public void setReTries(int reTries) {
    for (SrsFlvMuxer muxer : muxers) {
      muxer.setReTries(reTries);
    }
  }

  @Override
  protected void reConnect(long delay) {
    for (SrsFlvMuxer muxer : muxers) {
      muxer.reConnect(delay);
    }
  }

  @Override
  public void resizeCache(int newSize) throws RuntimeException {
    for (SrsFlvMuxer muxer : muxers) {
      muxer.resizeFlvTagCache(newSize);
    }
  }

  @Override
  public int getCacheSize() {
    return !muxers.isEmpty() ? muxers.get(0).getFlvTagCacheSize() : 0;
  }

  @Override
  public long getSentAudioFrames() {
    long audioFrames = 0;
    for (SrsFlvMuxer muxer : muxers) {
      audioFrames += muxer.getSentAudioFrames();
    }
    return audioFrames;
  }

  @Override
  public long getSentVideoFrames() {
    long audioFrames = 0;
    for (SrsFlvMuxer muxer : muxers) {
      audioFrames += muxer.getSentVideoFrames();
    }
    return audioFrames;
  }

  @Override
  public long getDroppedAudioFrames() {
    long audioFrames = 0;
    for (SrsFlvMuxer muxer : muxers) {
      audioFrames += muxer.getDroppedAudioFrames();
    }
    return audioFrames;
  }

  @Override
  public long getDroppedVideoFrames() {
    long audioFrames = 0;
    for (SrsFlvMuxer muxer : muxers) {
      audioFrames += muxer.getDroppedVideoFrames();
    }
    return audioFrames;
  }

  @Override
  public void resetSentAudioFrames() {
    for (SrsFlvMuxer muxer : muxers) {
      muxer.resetSentAudioFrames();
    }
  }

  @Override
  public void resetSentVideoFrames() {
    for (SrsFlvMuxer muxer : muxers) {
      muxer.resetSentVideoFrames();
    }
  }

  @Override
  public void resetDroppedAudioFrames() {
    for (SrsFlvMuxer muxer : muxers) {
      muxer.resetDroppedAudioFrames();
    }
  }

  @Override
  public void resetDroppedVideoFrames() {
    for (SrsFlvMuxer muxer : muxers) {
      muxer.resetDroppedVideoFrames();
    }
  }

  @Override
  protected void prepareAudioRtp(boolean isStereo, int sampleRate) {
    for (SrsFlvMuxer muxer : muxers) {
      muxer.setIsStereo(isStereo);
      muxer.setSampleRate(sampleRate);
    }
  }

  @Override
  protected void startStreamRtp(String url) {
    if (videoEncoder.getRotation() == 90 || videoEncoder.getRotation() == 270) {
      for (SrsFlvMuxer muxer : muxers) {
        muxer.setVideoResolution(videoEncoder.getHeight(), videoEncoder.getWidth());
      }
    } else {
      for (SrsFlvMuxer muxer : muxers) {
        muxer.setVideoResolution(videoEncoder.getWidth(), videoEncoder.getHeight());
      }
    }
    String[] urls = url.split(",");
    for (int i = 0; i < urls.length; i++) {
      SrsFlvMuxer muxer = muxers.get(i);
      muxer.start(urls[i]);
    }
  }

  @Override
  protected void stopStreamRtp() {
    for (SrsFlvMuxer muxer : muxers) {
      muxer.stop();
    }
  }

  @Override
  protected void getAacDataRtp(ByteBuffer aacBuffer, MediaCodec.BufferInfo info) {
    for (SrsFlvMuxer muxer : muxers) {
      muxer.sendAudio(aacBuffer.duplicate(), info);
    }
  }

  @Override
  protected void onSpsPpsVpsRtp(ByteBuffer sps, ByteBuffer pps, ByteBuffer vps) {
    for (SrsFlvMuxer muxer : muxers) {
      muxer.setSpsPPs(sps.duplicate(), pps);
    }
  }

  @Override
  protected void getH264DataRtp(ByteBuffer h264Buffer, MediaCodec.BufferInfo info) {
    for (SrsFlvMuxer muxer : muxers) {
      muxer.sendVideo(h264Buffer.duplicate(), info);
    }
  }
}

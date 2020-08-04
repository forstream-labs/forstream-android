package io.forstream.api.model;

import io.forstream.api.enums.ChannelAlertType;

public class ChannelAlert extends Entity {

  private ChannelAlertType type;
  private boolean checked;

  public ChannelAlertType getType() {
    return type;
  }

  public void setType(ChannelAlertType type) {
    this.type = type;
  }

  public boolean isChecked() {
    return checked;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }
}

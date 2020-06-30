package io.forstream.api.model;

import java.io.Serializable;

public class Target implements Serializable {

  private static final long serialVersionUID = 6761992786627847992L;

  private String id;
  private String name;
  private String url;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}

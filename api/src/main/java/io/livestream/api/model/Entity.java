package io.livestream.api.model;

import java.io.Serializable;
import java.util.Objects;

public class Entity implements Serializable {

  private static final long serialVersionUID = -1052720778334164804L;

  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Entity entity = (Entity) o;
    return id.equals(entity.id);
  }
}

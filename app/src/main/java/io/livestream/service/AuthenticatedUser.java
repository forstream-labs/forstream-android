package io.livestream.service;

import javax.inject.Inject;

import io.livestream.api.model.User;
import io.livestream.dagger.scope.AppScope;

@AppScope
public class AuthenticatedUser {

  private User user;

  @Inject
  public AuthenticatedUser() {

  }

  public void set(User user) {
    this.user = user;
  }

  public User get() {
    return user;
  }

  public void remove() {
    set(null);
  }
}

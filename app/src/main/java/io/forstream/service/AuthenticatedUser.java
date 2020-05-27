package io.forstream.service;

import javax.inject.Inject;

import io.forstream.api.model.User;
import io.forstream.dagger.scope.AppScope;

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

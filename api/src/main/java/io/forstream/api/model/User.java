package io.forstream.api.model;

import java.io.Serializable;
import java.util.Date;

public class User extends Entity implements Serializable {

  private static final long serialVersionUID = -66497826973366808L;

  private String firstName;
  private String lastName;
  private String email;
  private String imageUrl;
  private String googleId;
  private Date registrationDate;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getGoogleId() {
    return googleId;
  }

  public void setGoogleId(String googleId) {
    this.googleId = googleId;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }

  public String getFullName() {
    return firstName.concat(" ").concat(lastName);
  }
}

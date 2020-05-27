package io.forstream.api.exception;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ApiException extends RuntimeException {

  private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

  private Integer status;
  private String code;
  private String causeMessage;
  private String errorBody;

  private ApiException() {

  }

  private ApiException(String code, String description) {
    super(description);
    this.code = code;
  }

  public static ApiException fromJson(String json) {
    ApiException apiException;
    try {
      apiException = gson.fromJson(json, ApiException.class);
      if (apiException == null) {
        apiException = internalError();
      }
    } catch (Exception e) {
      Log.e(ApiException.class.getSimpleName(), String.format("Error converting json: %s", json));
      apiException = internalError();
    }
    apiException.errorBody = json;
    return apiException;
  }

  public static ApiException internalError() {
    return new ApiException("internal_error", "Internal error");
  }

  public Integer getStatus() {
    return status;
  }

  public String getCode() {
    return code;
  }

  public String getCauseMessage() {
    return causeMessage;
  }

  public String getErrorBody() {
    return errorBody;
  }
}

package io.forstream.api.util;

import com.onehilltech.promises.Promise;

import java.io.IOException;

import io.forstream.api.exception.ApiException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromiseUtils {

  private PromiseUtils() {

  }

  public static <T> Promise<T> build(Call<T> call) {
    return new Promise<>(settlement -> call.enqueue(new Callback<T>() {
      @Override
      public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
          settlement.resolve(response.body());
        } else {
          try {
            if (response.errorBody() != null) {
              settlement.reject(ApiException.fromJson(response.errorBody().string()));
            } else {
              settlement.reject(ApiException.internalError());
            }
          } catch (IOException e) {
            settlement.reject(ApiException.internalError());
          }
        }
      }

      @Override
      public void onFailure(Call<T> call, Throwable throwable) {
        settlement.reject(throwable);
      }
    }));
  }
}

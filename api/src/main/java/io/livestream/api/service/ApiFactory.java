package io.livestream.api.service;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import io.livestream.api.BuildConfig;
import io.livestream.api.util.JsonUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ApiFactory {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER = "Bearer";

  private ApiFactory() {

  }

  public static <T> T build(Class<T> clazz, Context context) {
    OkHttpClient httpClient = new OkHttpClient.Builder()
      .addInterceptor(new AuthorizationInterceptor(context))
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .build();
    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(BuildConfig.API_URL)
      .addConverterFactory(GsonConverterFactory.create(JsonUtils.getGson()))
      .addConverterFactory(new EnumConverterFactory())
      .client(httpClient)
      .build();
    return retrofit.create(clazz);
  }

  private static class AuthorizationInterceptor implements Interceptor {

    private Context context;

    AuthorizationInterceptor(Context context) {
      this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
      String token = TokenManager.getToken(context);
      if (token == null) {
        return chain.proceed(chain.request());
      }
      Request request = chain.request().newBuilder()
        .addHeader(AUTHORIZATION_HEADER, BEARER.concat(" ").concat(token))
        .build();
      return chain.proceed(request);
    }
  }

  private static class EnumConverterFactory extends Converter.Factory {
    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
      Converter<?, String> converter = null;
      if (type instanceof Class && ((Class<?>) type).isEnum()) {
        converter = value -> {
          try {
            Enum valueEnum = (Enum) value;
            SerializedName serializedName = valueEnum.getClass().getField(valueEnum.name()).getAnnotation(SerializedName.class);
            return serializedName != null ? serializedName.value() : null;
          } catch (NoSuchFieldException e) {
            return null;
          }
        };
      }
      return converter;
    }
  }
}

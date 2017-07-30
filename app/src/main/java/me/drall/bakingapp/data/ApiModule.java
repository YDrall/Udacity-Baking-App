/*
 *  Copyright (C) 2017 Yogesh Drall
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package me.drall.bakingapp.data;

import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import me.drall.bakingapp.config.AutoValueGsonFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    private String baseUrl;

    public ApiModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Named("BaseUrl")
    String providesBaseUrl() {
        return this.baseUrl;
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(@Named("BaseUrl") String baseUrl,
        @Named("JsonAdapterFactory") Converter.Factory factory,
        OkHttpClient client,
        @Named("rxCallAdapterFactory") CallAdapter.Factory rxCallAdapterFactory) {

        return new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(rxCallAdapterFactory)
            .addConverterFactory(factory)
            .client(client)
            .build();
    }

    @Provides
    @Named("rxCallAdapterFactory")
    CallAdapter.Factory providesRxJava2CallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Named("LoggingInterceptor")
    Interceptor providesLoggingInterceptor() {
        return new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Named("JsonAdapterFactory")
    Converter.Factory providesJsonAdapterFactory() {
        return GsonConverterFactory.create(new GsonBuilder()
            .registerTypeAdapterFactory(AutoValueGsonFactory.create())
            .create());
    }

    @Provides
    OkHttpClient provideOkHttpClient(@Named("LoggingInterceptor")Interceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build();
    }

    @Provides
    ApiService providesApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}

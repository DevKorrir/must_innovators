package com.newton.meruinnovators.di

import com.newton.auth.navigation.AuthNavigationApi
import com.newton.events.navigation.EventsNavigationApi
import com.newton.meruinnovators.BuildConfig
import com.newton.meruinnovators.navigation.NavigationSubGraphs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    @Provides
    fun provideNavigationSubGraphs(
        authNavigationApi: AuthNavigationApi,
        eventsNavigationApi: EventsNavigationApi
    ): NavigationSubGraphs {
        return NavigationSubGraphs(authNavigationApi,eventsNavigationApi)
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .callTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()


    @Provides
    @Singleton
    fun provideMeruInnovatorsApi(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
package io.github.sustainow.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.github.sustainow.BuildConfig
import io.github.sustainow.service.auth.AuthService
import io.github.sustainow.service.auth.AuthServiceSupabaseImp
import io.github.sustainow.service.calculation.CalculationService
import io.github.sustainow.service.calculation.CalculationServiceGemini
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        // TODO(PRODUCTION) Don't use the gemini api directly on client side
        return Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/")
            .addConverterFactory( Json.asConverterFactory(
                "application/json; charset=UTF8".toMediaType()))
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val originalHttpUrl = original.url

                        val url = originalHttpUrl.newBuilder()
                            .addQueryParameter("key",BuildConfig.GEMINI_API_KEY )
                            .build()

                        val requestBuilder = original.newBuilder()
                            .url(url)

                        chain.proceed(requestBuilder.build())
                    }
                    .build()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideCalculationService(retrofit: Retrofit): CalculationService {
        return retrofit.create(CalculationServiceGemini::class.java)
    }

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY,
        ) {
            install(Postgrest)
            install(Auth) {
                flowType = FlowType.PKCE
                scheme = "app"
                host = "supabase.com"
            }
            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
        return client.postgrest
    }

    @Provides
    @Singleton
    fun provideSupabaseAuth(client: SupabaseClient): Auth {
        return client.auth
    }

    @Provides
    @Singleton
    fun provideSupabaseStorage(client: SupabaseClient): Storage {
        return client.storage
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: Auth): AuthService {
        return AuthServiceSupabaseImp(auth)
    }
}

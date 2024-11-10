package io.github.sustainow.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.sustainow.service.auth.AuthService
import io.github.sustainow.service.auth.AuthServiceFirebaseImp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideAuthRepository(): AuthService {
        return AuthServiceFirebaseImp()
    }
}

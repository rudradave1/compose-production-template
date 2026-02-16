package com.rudradave.features.samplefeature.di

import com.rudradave.features.samplefeature.data.SampleFeatureRepositoryImpl
import com.rudradave.features.samplefeature.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds sample feature contracts to implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SampleFeatureModule {

    /**
     * Binds post repository implementation.
     */
    @Binds
    @Singleton
    abstract fun bindPostRepository(
        impl: SampleFeatureRepositoryImpl
    ): PostRepository
}

package com.app.host.ui.di

import com.app.commondata.repository.AppRepositoryImpl
import com.app.commondomain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * DataModule is used to inject repository or services or database classes
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {

    @Binds
    abstract fun appRepository(
        analyticsServiceImpl: AppRepositoryImpl
    ): AppRepository
}

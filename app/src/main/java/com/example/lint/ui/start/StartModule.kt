package com.example.lint.ui.start

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped


@Module
@InstallIn(ActivityComponent::class)
object StartModule {
    @Provides

    @ActivityScoped
    fun provideSharedMenuViewModel(activity: FragmentActivity): StartSharedViewModel {
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory).get(
            StartSharedViewModel::class.java
        )
    }
}
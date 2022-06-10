package com.example.link.ui.main

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.link.ui.start.StartSharedViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    @Provides
    @ActivityScoped
    fun provideContext(@ActivityContext context: Context): Context = context


    @Provides
    @ActivityScoped
    fun provideToolbarMenuViewModel(activity: FragmentActivity): MainToolbarViewModel {
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory).get(
            MainToolbarViewModel::class.java
        )
    }


    @Provides
    @ActivityScoped
    fun provideSharedMenuViewModel(activity: FragmentActivity): MainSharedViewModel {
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory).get(
            MainSharedViewModel::class.java
        )
    }
}
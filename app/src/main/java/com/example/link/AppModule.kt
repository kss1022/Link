package com.example.link

import android.content.Context
import com.example.link.data.api.FireStoreApi
import com.example.link.data.api.FireStoreApiImpl
import com.example.link.data.repository.PetRepository
import com.example.link.data.repository.PetRepositoryImpl
import com.example.link.data.repository.UserRepository
import com.example.link.data.repository.UserRepositoryImpl
import com.example.link.util.lifecycle.SingleLiveEvent
import com.example.link.util.lifecycle.SystemUIType
import com.example.link.util.resource.DefaultResourceProvider
import com.example.link.util.resource.ResourceProvider
import com.example.yourchoice.data.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module(includes = [AppModuleBinds::class])
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideIoDispatchers() = Dispatchers.IO

    @Singleton
    @Provides
    fun providePreferenceManager(@ApplicationContext context: Context) : PreferenceManager = PreferenceManager(
        context.getSharedPreferences(PreferenceManager.PREFERENCES_NAME, Context.MODE_PRIVATE)
    )

    @Singleton
    @Provides
    fun provideFirestore() : FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth = Firebase.auth


    @Singleton
    @Provides
    fun provideSystemUIType(): SingleLiveEvent<SystemUIType> {
        return SingleLiveEvent<SystemUIType>().apply {
            value = SystemUIType.NORMAL
        }
    }
}


@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun bindResourceProvider(repository: DefaultResourceProvider): ResourceProvider


    @Singleton
    @Binds
    abstract fun bindFireStoreApi(api : FireStoreApiImpl) : FireStoreApi

    @Singleton
    @Binds
    abstract fun bindUserRepository(repositoryImpl: UserRepositoryImpl) : UserRepository


    @Singleton
    @Binds
    abstract fun bindPetRepository(repositoryImpl: PetRepositoryImpl) : PetRepository
}
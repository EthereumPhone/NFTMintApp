package org.ethereumphone.nftcreator.injection

import androidx.lifecycle.viewmodel.compose.viewModel
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.ethereumphone.nftcreator.App
import org.ethereumphone.nftcreator.walletconnect.ConnectWalletViewModel
import org.koin.dsl.module
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.walletconnect.impls.FileWCSessionStore
import java.io.File

val appModule = module {

    // provide application
    single { androidApplication() as App }

    // Provide Moshi
    single { Moshi.Builder().build() }

    // Provide OkHttpClient
    single { OkHttpClient.Builder().build() }

    // provide WCSessionStore
    single { FileWCSessionStore(File(androidContext().cacheDir, "session_store.json").apply { createNewFile() }, get()) }

    // provide ConnectWalletViewModel
    viewModel { ConnectWalletViewModel(get(), get(), get()) }
}
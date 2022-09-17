package org.ethereumphone.nftcreator.injection

import androidx.compose.ui.platform.LocalContext
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.pinkroom.walletconnectkit.WalletConnectKit
import dev.pinkroom.walletconnectkit.WalletConnectKitConfig
import okhttp3.OkHttpClient
import org.ethereumphone.nftcreator.App
import org.ethereumphone.nftcreator.walletconnect.ConnectWalletViewModel
import org.koin.dsl.module
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.walletconnect.impls.FileWCSessionStore
import org.walletconnect.impls.WCSessionStore
import java.io.File
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities

val appModule = module {
    // provide application
    single { androidApplication() as App }

    // provide walletConnectKit
    single<WalletConnectKit> {
        val config = WalletConnectKitConfig(
        context = androidContext(),
        bridgeUrl = "wss://bridge.aktionariat.com:8887",
        appUrl = "https://ethereumphone.org",
        appName = "NFT Creator",
        appDescription = "Create NFTs!"
        )

        WalletConnectKit.Builder(config).build()
    }
}
package org.ethereumphone.nftcreator.walletconnect

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.komputing.khex.extensions.toHexString
import org.walletconnect.Session
import org.walletconnect.impls.MoshiPayloadAdapter
import org.walletconnect.impls.OkHttpTransport
import org.walletconnect.impls.WCSession
import org.walletconnect.impls.WCSessionStore
import java.util.*

class ConnectWalletViewModel(
    private val moshi: Moshi,
    private val client: OkHttpClient,
    private val storage: WCSessionStore
): ViewModel() {

    var userWallet = MutableStateFlow("")
        private set

    private var config: Session.Config? = null
    private var session: Session? = null
    private var bridge: BridgeServer? = null
    private var activeCallback: Session.Callback? = null

    init {
        initBridge()
    }

    private fun initBridge() {
        bridge = BridgeServer(moshi).apply {
            onStart()
        }
    }

    private fun resetSession() {
        session?.clearCallbacks()
        val key = ByteArray(32).also { Random().nextBytes(it) }.toHexString(prefix = "")
        config =
            Session.Config(UUID.randomUUID().toString(), "https://bridge.walletconnect.org", key)
        session = WCSession(
            config?.toFullyQualifiedConfig() ?: return,
            MoshiPayloadAdapter(moshi),
            storage,
            OkHttpTransport.Builder(client, moshi),
            Session.PeerMeta(name = "Example App")
        )
        session?.offer()
    }


    fun connectWallet(context: Context, callback: () -> Unit) {
        resetSession()
        activeCallback = object : Session.Callback {
            override fun onMethodCall(call: Session.MethodCall) = Unit

            override fun onStatus(status: Session.Status) {
                status.handleStatus()

                if(status == Session.Status.Approved) {
                    GlobalScope.launch(Dispatchers.Main) {
                        callback()
                    }
                }
            }
        }
        session?.addCallback(activeCallback ?: return)
        context.startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(config?.toWCUri() ?: return)
        })
    }

    fun Session.Status.handleStatus() {
        when (this) {
            Session.Status.Approved -> sessionApproved()
            Session.Status.Closed -> sessionClosed()
            Session.Status.Connected,
            Session.Status.Disconnected,
            is Session.Status.Error,
            -> Log.e("WC Session Status", "handleStatus: $this", )
        }
    }

    private fun sessionApproved() {
        val address = session?.approvedAccounts()?.firstOrNull() ?: return
        /* Provider name*/
        // val walletType = session?.peerMeta()?.name ?: ""
        userWallet.value = address
    }

    private fun sessionClosed() {

    }

    fun signMessage(context: Context, message: String, callback: (Session.MethodCall.Response) -> Unit) {


        session?.performMethodCall(


            /*Session.MethodCall.SignMessage(
                id = 1337L,
                message = userWallet.value,
                address = message
            ),
            callback = callback
            */
            Session.MethodCall.SendTransaction(
                id = 133L,
                from = "0x24EdA4f7d0c466cc60302b9b5e9275544E5ba552",
                to = "0x3a4e6ed8b0f02bfbfaa3c6506af2db939ea5798c",
                nonce = null,
                gasPrice = null,
                gasLimit = null,
                value = "0x5AF3107A4000",
                data = ""
            ),
            callback = callback

        )


        /*
        activeCallback = object : Session.Callback {
            override fun onMethodCall(call: Session.MethodCall) {
                Log.d("test onMethodCall", "Margus Ass")
            }

            override fun onStatus(status: Session.Status) {

            }
        }
        */
        //session?.addCallback(activeCallback ?: return)
        // sign message via walletConnect

        /*
        context.startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(config?.toWCUri() ?: return)
        })
        */
    }
}
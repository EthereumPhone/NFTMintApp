package org.ethereumphone.nftcreator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CreatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)

        val extras = intent.extras

        val address = extras?.getString("address")

        val addressView = findViewById<TextView>(R.id.addressView)
        addressView.text = getString(R.string.welcome_message, address)
    }
}
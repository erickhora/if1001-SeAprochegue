package com.project.seaprochegue.seaprochegue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        supportActionBar?.title = "Configurações"
    }
}

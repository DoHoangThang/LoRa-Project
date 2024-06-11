package com.example.hethonglora

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GGsheet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ggsheet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val myWeb = findViewById<WebView>(R.id.webView)
        myWeb.apply {
            loadUrl("https://docs.google.com/spreadsheets/d/1ErhyVOYc-n9dE45DrRVBfJuQr_qLCKU7TUsDWx2J4b8/edit#gid=0") //đg link dẫn đến ggsheet
            settings.javaScriptEnabled = true
        }
    }
}
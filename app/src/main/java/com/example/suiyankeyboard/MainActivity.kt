package com.example.suiyankeyboard

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val tv = TextView(this).apply {
            text = "🚀 Suiyan Keyboard Installed!\n\nGo to Settings > Languages & Input > Input Methods\n\nEnable 'Suiyan Keyboard'"
            textSize = 18f
            setPadding(20, 20, 20, 20)
        }
        setContentView(tv)
    }
}

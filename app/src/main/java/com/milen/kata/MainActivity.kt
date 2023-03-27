package com.milen.kata

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.milen.kata.experiments.CoroutinesExperimentActivity
import com.milen.kata.experiments.foregroundservice.ForegroundServiceExperimentActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_coroutines_experiments).setOnClickListener {
            startActivity(Intent(this, CoroutinesExperimentActivity::class.java))
        }

        findViewById<Button>(R.id.btn_foreground_service_experiments).setOnClickListener {
            startActivity(Intent(this, ForegroundServiceExperimentActivity::class.java))
        }
    }
}
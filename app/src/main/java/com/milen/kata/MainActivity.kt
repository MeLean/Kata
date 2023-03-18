package com.milen.kata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.milen.kata.experiments.CoroutinesExperimentActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.coroutines_experiments_btn).setOnClickListener{
            startActivity(Intent(this, CoroutinesExperimentActivity::class.java))
        }
    }
}
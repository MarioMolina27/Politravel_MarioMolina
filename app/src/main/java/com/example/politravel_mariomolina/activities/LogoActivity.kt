package com.example.politravel_mariomolina.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.politravel_mariomolina.R

class LogoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val imgLogo = findViewById<ImageView>(R.id.imgLogo)
        imgLogo.animate()
            .alpha(1f)
            .setStartDelay(1000)
            .setDuration(2000)
            .withEndAction(Runnable
            {
                imgLogo.animate()
                    .alpha(0f)
                    .setDuration(2000)
                    .withEndAction(Runnable
                    {
                        imgLogo.setVisibility(View.GONE)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    })
            })
    }
}
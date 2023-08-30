package eu.pl.snk.senseibunny.trelloclone

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private var binding :ActivitySplashBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )

        val typeFace: Typeface =  Typeface.createFromAsset(assets, "AntipastoPro_trial.ttf")
        binding!!.titleId.typeface=typeFace
    }
}
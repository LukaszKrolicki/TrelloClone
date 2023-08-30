package eu.pl.snk.senseibunny.trelloclone

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivitySplashBinding
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private var binding: ActivityStartBinding ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val typeFace: Typeface =  Typeface.createFromAsset(assets, "AntipastoPro_trial.ttf")
        binding!!.titleId.typeface=typeFace
    }
}
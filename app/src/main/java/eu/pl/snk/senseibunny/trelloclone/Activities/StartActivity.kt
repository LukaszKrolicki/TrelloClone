package eu.pl.snk.senseibunny.trelloclone.Activities

import Firebase.FireStoreClass
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private var binding: ActivityStartBinding ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val typeFace: Typeface =  Typeface.createFromAsset(assets, "AntipastoPro_trial.ttf")
        binding!!.titleId.typeface=typeFace
        binding!!.appName.typeface=typeFace

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )


        binding?.join?.setOnClickListener{
            val intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)
        }

        binding?.signUp?.setOnClickListener{
            val intent = Intent(this, SingInActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}
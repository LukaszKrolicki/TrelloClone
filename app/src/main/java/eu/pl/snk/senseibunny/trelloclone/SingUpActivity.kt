package eu.pl.snk.senseibunny.trelloclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivitySingUpBinding

class SingUpActivity : AppCompatActivity() {

    var binding: ActivitySingUpBinding ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBar2)
        getSupportActionBar()?.setTitle("Sing Up")
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolBar2?.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}
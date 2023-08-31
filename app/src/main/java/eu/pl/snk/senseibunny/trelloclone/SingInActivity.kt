package eu.pl.snk.senseibunny.trelloclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivitySingInBinding
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivitySingUpBinding

class SingInActivity : AppCompatActivity() {
    private var binding: ActivitySingInBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySingInBinding.inflate(layoutInflater)
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
package eu.pl.snk.senseibunny.trelloclone.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivitySingInBinding
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivitySingUpBinding

class SingInActivity : BaseActivity() {
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

        binding?.button?.setOnClickListener{
            login()
        }


    }
    private fun validateForm(email: String, password: String): Boolean {
        return when{
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter a email")
                false
            }

            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a password")
                false
            }
            else ->{
                true
            }
        }
    }

    private fun login(){
        val email: String = binding?.emailEditText?.text.toString()
        val password: String = binding?.passwordEditText?.text.toString()

        if(validateForm(email, password)){
            showProgressDialog()
            lateinit var auth: FirebaseAuth
            auth = FirebaseAuth.getInstance()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(
                            baseContext,
                            "Authentication success.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    } else {
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}
package eu.pl.snk.senseibunny.trelloclone.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivitySingUpBinding

class SingUpActivity : BaseActivity() {

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

        binding?.button?.setOnClickListener{
            registerUser()
        }


    }
    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter a name")
                false
            }

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

    private fun registerUser(){
        val name: String = binding?.nameEditText?.text.toString().trim{it<=' '}
        val email: String = binding?.emailEditText?.text.toString().trim{it<=' '}
        val password: String = binding?.passwordEditText?.text.toString().trim{it<=' '}

        if(validateForm(name,email,password)){
            showProgressDialog()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                hideProgressDialog()
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val registeredEmail = firebaseUser.email!!
                    Toast.makeText(this, "You have registered!!", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut() //for now we looging him off
                    finish()
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}
package eu.pl.snk.senseibunny.trelloclone.Activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import eu.pl.snk.senseibunny.trelloclone.R

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private lateinit var progressDialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun showProgressDialog(){
        progressDialog = ProgressDialog(this@BaseActivity)
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.show()
    }

    fun hideProgressDialog(){
        progressDialog.dismiss()
    }

    fun getCurrentId(): String{ //get current user id
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun doubleBackToExit(){ //if double back pressed then shut down application
        if(doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce= true

        Toast.makeText(this@BaseActivity,"Click one more time to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            doubleBackToExitPressedOnce=false
        },20000)
    }

    fun showErrorSnackBar(message:String){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message,Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(ContextCompat.getColor(this, R.color.snackbar_error))
        snackBar.show()

    }

    fun loadImageFromUrl(context: Context, imageUrl: String, imageView: CircleImageView) {
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()

        Glide.with(context)
            .load(imageUrl)
            .apply(requestOptions)
            .into(imageView)
    }
}
package eu.pl.snk.senseibunny.trelloclone.Activities

import Firebase.FireStoreClass
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.pl.snk.senseibunny.trelloclone.R
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivityProfileBinding
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivitySingInBinding
import models.User

class ProfileActivity : BaseActivity() {

    private var binding: ActivityProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        setSupportActionBar(binding?.toolBar2)
        getSupportActionBar()?.setTitle("Profile")
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolBar2?.setNavigationOnClickListener {
            onBackPressed()
        }

        FireStoreClass().signInUser(this)


    }

    fun profileDetails(user: User) {
        binding?.nameEditText?.setText(user.name)
        binding?.mobileEditText?.setText(user.mobile.toString())
        binding?.emailEditText?.setHint(user.email)
        user.image?.let {
            binding?.imageView?.let { it1 -> loadImageFromUrl(this, it, it1) }
        }
    }
}
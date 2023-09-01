package eu.pl.snk.senseibunny.trelloclone.Activities

import Firebase.FireStoreClass
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import eu.pl.snk.senseibunny.trelloclone.R
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivityMainBinding
import models.User

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener{

    var binding : ActivityMainBinding ?=null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        FireStoreClass().signInUser(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.appbar?.toolbarMainActivity)
        binding?.appbar?.toolbarMainActivity?.setNavigationIcon(R.drawable.baseline_density_medium_24)

        binding?.appbar?.toolbarMainActivity?.setNavigationOnClickListener(){
            //toogle drawer
            toogleDrawer()
        }

        binding?.navView!!.setNavigationItemSelectedListener(this)
    }

    private fun toogleDrawer(){
        if(binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true){
            binding?.drawerLayout!!.closeDrawer(GravityCompat.START) //close drawer
        }else{
            binding?.drawerLayout!!.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() { //implements closing the drawer with back button
        if(binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true){
            binding?.drawerLayout!!.closeDrawer(GravityCompat.START) //close drawer
        }else{
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_profile -> {
                Toast.makeText(this, "My profile", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, StartActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK) //idk what it does xd
                startActivity(intent)
                finish()
            }
        }
        binding?.drawerLayout!!.closeDrawer(GravityCompat.START)

        return true
    }

    fun UpdateNavigationUserDetails(user: User){
        val imageView = findViewById<CircleImageView>(R.id.user_image)
        user.image?.let { loadImageFromUrl(this, it,imageView) }
        val textView = findViewById<TextView>(R.id.tv_username)
        if(user.name!=null){
            textView.setText(user.name.toString())
        }
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
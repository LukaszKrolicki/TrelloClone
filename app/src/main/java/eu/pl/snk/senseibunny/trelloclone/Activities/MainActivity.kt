package eu.pl.snk.senseibunny.trelloclone.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import eu.pl.snk.senseibunny.trelloclone.R
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivityMainBinding
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivitySingUpBinding
import eu.pl.snk.senseibunny.trelloclone.databinding.MainConentBinding

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
}
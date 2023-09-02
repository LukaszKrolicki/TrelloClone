package eu.pl.snk.senseibunny.trelloclone.Activities

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import de.hdodenhof.circleimageview.CircleImageView
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivityBoardBinding
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivityMainBinding

class BoardActivity : BaseActivity() {

    private var binding: ActivityBoardBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBar2)
        getSupportActionBar()?.setTitle("Sing Up")
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolBar2?.setNavigationOnClickListener{
            onBackPressed()
        }

        binding?.imageView?.setOnClickListener {
            requestStoragePermission()
        }
    }

    private val GalleryLaucnher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode== RESULT_OK && result.data!=null){
                val imageBackground: CircleImageView? = binding?.imageView
                imageBackground?.setImageURI(result.data?.data) // setting background of our app
            }
        }

    private val StorageLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){
            permissions->
        permissions.entries.forEach{
            val permissionName=it.key
            val isGranted=it.value
            if(isGranted) {
                if(permissionName==Manifest.permission.READ_EXTERNAL_STORAGE){
                    Toast.makeText(this, "permission granted for Read", Toast.LENGTH_LONG).show()

                    //Getting into gallery

                    val pickInent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    GalleryLaucnher.launch(pickInent)

                }
                else{
                    Toast.makeText(this, "permission granted for Write", Toast.LENGTH_LONG).show()
                }
            }
            else{
                if(permissionName== Manifest.permission.READ_EXTERNAL_STORAGE){
                    Toast.makeText(this, "permission denied for Read", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this, "permission denied for Camera", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) && shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            showRationaleDialog(
                "App requires storage access",
                "Allow read storage to be able to do this"
            )
        } else {
            StorageLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }
    private fun showRationaleDialog(title: String, message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Cancel"){dialog, _->dialog.dismiss()}
        builder.create().show()
    }



    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}
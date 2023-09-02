package eu.pl.snk.senseibunny.trelloclone.Activities

import Firebase.FireStoreClass
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.app.Activity
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AlertDialog
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask.TaskSnapshot
import de.hdodenhof.circleimageview.CircleImageView
import eu.pl.snk.senseibunny.trelloclone.R
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivityProfileBinding
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivitySingInBinding
import models.User

class ProfileActivity : BaseActivity() {

    private var binding: ActivityProfileBinding? = null
    private var imageUri: Uri? = null
    private var profileImageUrl: String? = null

    private lateinit var  userDetails: User
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

        binding?.imageView?.setOnClickListener {
            requestStoragePermission()
        }

        binding?.updateButton?.setOnClickListener {
            if(imageUri!=null){
                uploadUserImage()
            }
            else{
                updateUserProfile()
            }
        }

    }

    fun profileDetails(user: User) {
        userDetails=user

        binding?.nameEditText?.setText(user.name)
        binding?.mobileEditText?.setText(user.mobile.toString())
        binding?.emailEditText?.setHint(user.email)


        user.image?.let {
            binding?.imageView?.let { it1 -> loadImageFromUrl(this, it, it1) }
        }
    }



    private val GalleryLaucnher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode== RESULT_OK && result.data!=null){
                val imageBackground: CircleImageView? = binding?.imageView
                imageUri = result.data?.data
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

    private fun uploadUserImage(){
        showProgressDialog()

        if(imageUri!=null){

            val sRef :StorageReference = FirebaseStorage.getInstance().reference.child("USER_IMAGE"+ System.currentTimeMillis()+ "."+getFileExtension(imageUri)) //send to the storage
            sRef.putFile(imageUri!!).addOnSuccessListener {
                it->
                Log.e("Firebase Image Url", it.metadata!!.reference!!.downloadUrl.toString())

                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri->
                    Log.e("Downloadable Image Url", uri.toString())
                    profileImageUrl=uri.toString()
                    updateUserProfile()
                }
            }.addOnFailureListener{
                exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
                hideProgressDialog()
            }
        }
    }

    private fun getFileExtension(uri: Uri?): String? { //get link type fe. png
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(uri?.let {
            contentResolver.getType(
                it
            )
        })
    }

    private fun updateUserProfile(){
        val userHashMap= HashMap<String, Any>()
        var anyChangesMade=false

        if(profileImageUrl!!.isNotEmpty()){
            userHashMap["image"]= profileImageUrl!!
            anyChangesMade=true
        }

        if(binding?.nameEditText?.toString()!=userDetails.name.toString()){
            userHashMap["name"]= binding?.nameEditText?.text.toString()
            anyChangesMade=true
        }

        if(binding?.mobileEditText?.toString()!=userDetails.mobile.toString()){
            if (binding?.mobileEditText?.text.toString() == "") {
                userHashMap["mobile"] = 0L
            } else {
                userHashMap["mobile"] = binding?.mobileEditText?.text.toString().toLong()
                anyChangesMade=true
            }
        }

        if(anyChangesMade){
            FireStoreClass().updateUserProfile(this, userHashMap)
        }
    }

    fun profileUpdateSuccess(){
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }

}
package eu.pl.snk.senseibunny.trelloclone.Activities

import Firebase.FireStoreClass
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivityBoardBinding
import eu.pl.snk.senseibunny.trelloclone.databinding.ActivityMainBinding
import models.Board

class BoardActivity : BaseActivity() {

    private var binding: ActivityBoardBinding?=null

    private lateinit var userName: String

    private var imageUri: Uri? = null
    private var imageUrl: String? = ""

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

        if(intent.hasExtra("name")){
            userName= intent.getStringExtra("name").toString()
        }

        binding?.imageView?.setOnClickListener {
            requestStoragePermission()
        }

        binding?.updateButton?.setOnClickListener{
            if(imageUri!=null){
                uploadBoardImage()
            }
            else{
                showProgressDialog()
                createBoard()
            }
        }
    }


    fun boardCreatedSuccessfully(){
        hideProgressDialog()
        finish()
    }

    private fun uploadBoardImage(){
        showProgressDialog()

        if(imageUri!=null){

            val sRef : StorageReference = FirebaseStorage.getInstance().reference.child("BOARD_IMAGE"+ System.currentTimeMillis()+ "."+getFileExtension(imageUri)) //send to the storage
            sRef.putFile(imageUri!!).addOnSuccessListener {
                    it->
                Log.e("Firebase Image Url", it.metadata!!.reference!!.downloadUrl.toString())

                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        uri->
                    Log.e("Downloadable Image Url", uri.toString())
                    imageUrl=uri.toString()
                    createBoard()
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

    private fun createBoard(){
        val boardHashMap= HashMap<String, Any>()
        var anyChangesMade=false

        if(imageUrl!!.isNotEmpty()){
            boardHashMap["image"]= imageUrl!!
            anyChangesMade=true
        }
        else{
            boardHashMap["image"]=""
        }

        if(binding?.nameEditText?.toString()!!.isNotEmpty()){
            boardHashMap["name"]= binding?.nameEditText?.text.toString()
            anyChangesMade=true
        }


        if(anyChangesMade){
            val assignedUsersArrayList: ArrayList<String> = ArrayList()
            assignedUsersArrayList.add(getCurrentId()) // adding the current user id.
            val x: Board = Board(boardHashMap["name"] as String, boardHashMap["image"] as String,
                userName.toString(),assignedUsersArrayList)
            FireStoreClass().createBoard(this, x)
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





    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}
package Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import eu.pl.snk.senseibunny.trelloclone.Activities.SingInActivity
import eu.pl.snk.senseibunny.trelloclone.Activities.SingUpActivity
import models.User

class FireStoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()


    fun signInUser(activity: SingInActivity){
        mFirestore.collection("Users")//creating collection
            .document(getCurrentUserId()).get().addOnSuccessListener {document->
                val loggedInUser = document.toObject(User::class.java)//we retrieve from collection and make a user class
                if (loggedInUser != null) {
                    activity.signInSuccess(loggedInUser)
                }
            }
    }
    //we register user also in our database
    public fun registerUser(activity:SingUpActivity, userInfo: User){
        mFirestore.collection("Users")//creating collection
            .document(getCurrentUserId()).set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }


    fun getCurrentUserId():String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""

        if(currentUser!=null){
            currentUserId=currentUser.uid
        }

        return currentUserId
    }
}
import android.content.ContentValues
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.capstone.activities.RegisterActivity
import com.android.capstone.imageclassification.ImageClassificationActivity
import com.android.capstone.models.ResultsModel
import com.android.capstone.models.User
import com.android.capstone.utils.Constants
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId()).set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Error")
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun writeDataOnFirestore(activity: ImageClassificationActivity, results: ResultsModel) {
        val ResultsBatch = mFireStore.collection(Constants.RESULTS).document()
        val user = hashMapOf(
            "title" to results.title,
            "confidence" to results.confidence,
            "id" to getCurrentUserId(),
            "date" to Timestamp(Date())
        )

        mFireStore.runBatch { batch ->
            batch.set(ResultsBatch, user)
        }
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
            }.addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
    }

    fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}
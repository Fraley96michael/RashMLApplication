package com.android.capstone.imageclassification

import FirestoreClass
import com.android.capstone.activities.LineChartActivity
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.OnLongClickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.capstone.databinding.ActivityImageClassificationBinding
import com.android.capstone.models.ResultsModel
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException

@SuppressLint("StaticFieldLeak")
val mFireStore = FirebaseFirestore.getInstance()

class ImageClassificationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityImageClassificationBinding

    private val mFireStore = FirebaseFirestore.getInstance()

    private val RESULT_LOAD_IMAGE = 123
    val IMAGE_CAPTURE_CODE = 654
    private val PERMISSION_CODE = 321
    private var image_uri: Uri? = null

    var classifier : Classifier? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageClassificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.frame?.setOnClickListener(View.OnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
        })

        binding.buttonResults.visibility = View.GONE
        binding.resultTitle.visibility = View.GONE
        binding.resultValue.visibility = View.GONE

        binding.frame?.setOnLongClickListener(OnLongClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    openCamera()
                }
            } else {
                openCamera()
            }
            true
        })

        //TODO initialize the Classifier class object. This class will load the model and using its method we will pass input to the model and get the output
        classifier = Classifier(assets,"mobilenet_v1_1.0_224.tflite","mobilenet_v1_1.0_224.txt",224)

        //TODO ask for permission of camera upon first launch of application
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            val permission = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            requestPermissions(permission, PERMISSION_CODE)
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            image_uri = data.data
            binding.innerImage!!.setImageURI(image_uri)
            doInference()
        }
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            binding.innerImage!!.setImageURI(image_uri)
            doInference()
        }
    }

    //TODO pass image to the model and shows the results on screen
    @RequiresApi(Build.VERSION_CODES.O)
    fun doInference() {
        val bitmap : Bitmap? = uriToBitmap(image_uri!!)
        val rotated : Bitmap? = rotateBitmap(bitmap!!)
        val results  = classifier?.recognizeImage(rotated!!)
        //Show Results on the screen
        binding.resultTv?.text = " "
        for(r in results!!){
           binding.resultTv?.append(r.title + "  " + r.confidence + "\n")
        }

        //Take us to the next page
        binding.buttonResults.visibility = View.VISIBLE
        binding.buttonResults.setOnClickListener {
            binding.resultTitle?.text = ""
            binding.resultValue?.text = ""

            //going to bind new values and transfer
            for(r in results!!){
                binding.resultTitle?.append(r.title)
                binding.resultValue?.append( r.confidence.toString())
                val hashMap = ResultsModel(
                   title = binding.resultTitle.text.toString(),
                    confidence = binding.resultValue.text.toString().toFloat(),
                )

                FirestoreClass().writeDataOnFirestore(this, hashMap)
                val i = Intent(this, LineChartActivity::class.java).apply {
                    putExtra("Title", binding.resultTitle?.text.toString())
                    putExtra("Confidence", binding.resultValue?.text.toString())
                }


            val intent = Intent(this@ImageClassificationActivity, LineChartActivity::class.java)
            startActivity(intent)
                startActivity(i)
        }
        }
    }

    //TODO takes URI of the image and returns bitmap
    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor =
                contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    //TODO rotate image if image captured on samsung devices
    //Most phone cameras are landscape, meaning if you take the photo in portrait, the resulting photos will be rotated 90 degrees.
    @SuppressLint("Range")
    fun rotateBitmap(input: Bitmap): Bitmap? {
        val orientationColumn =
            arrayOf(MediaStore.Images.Media.ORIENTATION)
        val cur =
            contentResolver.query(image_uri!!, orientationColumn, null, null, null)
        var orientation = -1
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]))
        }
        Log.d("tryOrientation", orientation.toString() + "")
        val rotationMatrix = Matrix()
        rotationMatrix.setRotate(orientation.toFloat())
        return Bitmap.createBitmap(
            input,
            0,
            0,
            input.width,
            input.height,
            rotationMatrix,
            true
        )
    }
}
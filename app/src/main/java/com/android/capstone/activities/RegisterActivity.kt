package com.android.capstone.activities

import FirestoreClass
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.capstone.databinding.ActivityRegisterBinding
import com.android.capstone.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private lateinit var binding : ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.already.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun userRegisteredSuccess(){
        Toast.makeText(this, "registration successful", Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance()
        finish()
    }

    fun register(view: View) {

        val email : String = binding.emailReg.text.toString().trim(){it <= ' '}
        val pass : String = binding.passReg.text.toString().trim(){it <= ' '}
        val first : String = binding.firstName.text.toString().trim(){it <= ' '}
        val last : String = binding.lastName.text.toString().trim(){it <= ' '}

        when {
            TextUtils.isEmpty(binding.emailReg.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter email.",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            TextUtils.isEmpty(binding.firstName.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter your firstname.",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            TextUtils.isEmpty(binding.lastName.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter you lastname.",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            TextUtils.isEmpty(binding.passReg.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this@RegisterActivity,
                    "Please enter password.",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            TextUtils.isEmpty(binding.confirmPass.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please confirm password.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            !TextUtils.equals(binding.passReg.text.toString(), binding.confirmPass.text.toString()) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Passwords do not match.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

            val intent = Intent(this, AboutUsActivity::class.java)

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener {
                        task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val registeredEmail = firebaseUser.email!!
                    val user = User(firebaseUser.uid,first,last,registeredEmail)
                    FirestoreClass().registerUser(this,user)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("user_id",firebaseUser.uid)
                    intent.putExtra("email_id", email)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
            }
        }
    }
}
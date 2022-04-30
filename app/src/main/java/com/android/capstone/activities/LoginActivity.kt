package com.android.capstone.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.capstone.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 0
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.already.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.forgotpassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.google.setOnClickListener {
            signIn()
        }

        binding.google2.setOnClickListener {
            signIn()
        }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("731006135829-anhbeg8mm5sn0h54flru3etve3jqfakd.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, AboutUsActivity::class.java)
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    // ...
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun login(view: View) {

        when {
            TextUtils.isEmpty(binding.emailLog.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this@LoginActivity, "Please enter email.", Toast.LENGTH_SHORT)
                    .show()
            }
            TextUtils.isEmpty(binding.passwordLog.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this@LoginActivity, "Please enter password.", Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {

                auth.signInWithEmailAndPassword(binding.emailLog.text.toString(), binding.passwordLog.text.toString())
                    .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, AboutUsActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        applicationContext,
                        exception.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
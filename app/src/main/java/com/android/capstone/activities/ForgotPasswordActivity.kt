package com.android.capstone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.capstone.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

private lateinit var forgotBinding: ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(forgotBinding.root)

        forgotBinding.submit.setOnClickListener {
            val email : String = forgotBinding.email.text.toString().trim{ it <= ' '}
            if(email.isEmpty()){
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Please enter email address.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {task ->
                        if(task.isSuccessful){
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Email sent successfully.",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()
                        } else {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}
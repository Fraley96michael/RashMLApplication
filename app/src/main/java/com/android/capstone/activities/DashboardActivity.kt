package com.android.capstone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.capstone.databinding.DashboardBinding
import com.android.capstone.databinding.DashboardBinding.*
import com.android.capstone.imageclassification.ImageClassificationActivity
import com.android.capstone.imageclassification.LiveFeedClassificationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var dashboardBinding: DashboardBinding

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardBinding = inflate(layoutInflater)
        setContentView(dashboardBinding.root)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        val email = user.email

        ("Welcome" + " " + email.toString()).also { dashboardBinding.editTextTextPersonName.text = it }

        dashboardBinding.imageRec.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ImageClassificationActivity::class.java)
            startActivity(intent)
        }
        dashboardBinding.liveFeed.setOnClickListener {
            val intent = Intent(this@DashboardActivity, LiveFeedClassificationActivity::class.java)
            startActivity(intent)
        }
        dashboardBinding.logout.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this@DashboardActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        dashboardBinding.rashInfo.setOnClickListener {
            val intent = Intent(this@DashboardActivity, RashInfoActivity::class.java)
            startActivity(intent)
        }

        dashboardBinding.progressChart.setOnClickListener{
            val intent = Intent(this@DashboardActivity, LineChartActivity::class.java)
            startActivity(intent)
        }
        dashboardBinding.chatButton.setOnClickListener{
            val intent = Intent(this@DashboardActivity, ChatActivity::class.java)
            startActivity(intent)
        }
    }
}
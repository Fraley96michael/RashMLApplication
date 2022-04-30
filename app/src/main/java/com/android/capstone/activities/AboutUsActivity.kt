package com.android.capstone.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.capstone.databinding.AboutUsBinding

private lateinit var aboutUsBinding: AboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aboutUsBinding = AboutUsBinding.inflate(layoutInflater)
        setContentView(aboutUsBinding.root)

        aboutUsBinding.buttonAboutUsContinue.setOnClickListener{
            val intent = Intent(this@AboutUsActivity, DashboardActivity:: class.java)
            startActivity(intent)
        }
}
}
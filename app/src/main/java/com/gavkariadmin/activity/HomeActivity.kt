package com.gavkariadmin.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gavkariadmin.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(),View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        imgEvent.setOnClickListener(this)
        imgNews.setOnClickListener(this)
        imgSale.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            imgSale ->{
                startActivity(Intent(this,BuySaleActivity::class.java))
            }

            imgEvent->{
                startActivity(Intent(this,EventActivity::class.java))
            }

            imgNews->{
                startActivity(Intent(this,NewsActivity::class.java))
            }
        }
    }
}

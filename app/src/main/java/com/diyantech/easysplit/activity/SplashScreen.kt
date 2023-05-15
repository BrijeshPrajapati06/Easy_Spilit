package com.diyantech.easysplit.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.diyantech.easysplit.R
import com.diyantech.easysplit.modelclass.response.LoginResponse
import com.diyantech.easysplit.utils.Session


class SplashScreen : AppCompatActivity() {
    var user: LoginResponse.Data? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.dark_green)


        if (Session.getCurrentUser() != null) {
            user = Session.getCurrentUser()!!
        }

//        Handler().postDelayed({
//            val intent = Intent(this@SplashScreen, WelcomeScreen::class.java)
//            startActivity(intent)
//            finish()
//        }, 3000)
        Handler().postDelayed(object : Runnable {
            override fun run() {

                var intent : Intent ?= null

                if (user == null){
                    intent = Intent(this@SplashScreen,WelcomeScreen::class.java)
                }else{
                    if (user?.email != null){
                        intent = Intent(this@SplashScreen,LoginActivity::class.java)
                    }else{
                        intent = Intent(this@SplashScreen,HomeActivity::class.java)

                    }
                }
                startActivity(intent)
                finish()
            }
        }, 2000)
    }
}
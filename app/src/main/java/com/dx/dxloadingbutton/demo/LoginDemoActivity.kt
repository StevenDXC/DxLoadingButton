package com.dx.dxloadingbutton.demo

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import com.dx.dxloadingbutton.DemoActivity
import com.dx.dxloadingbutton.R
import com.dx.dxloadingbutton.lib.AnimationType
import kotlinx.android.synthetic.main.activity_demo.*


class LoginDemoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        loading_btn.resetAfterFailed = true
        loading_btn.animationEndAction = {
            if (it == AnimationType.SUCCESSFUL) {
                startActivity(Intent(applicationContext, DemoActivity::class.java))
            }
        }
        loading_btn.setOnClickListener { checkLogin() }
    }

    private fun checkLogin() {

        val userName = edit_user_name.text.toString()
        if (userName.isBlank()) {
            edit_user_name!!.error = "please input user name"
            return
        }

        val password = edit_password.text.toString()
        if (password.isBlank()) {
            edit_password.error = "please input password"
            return
        }

        loading_btn!!.startLoading()
        //send login request

        //demo
        edit_user_name.isEnabled = false
        edit_password.isEnabled = false
        loading_btn.postDelayed({
            edit_user_name.isEnabled = true
            edit_password.isEnabled = true
            if ("admin" == userName && "admin" == password) {
                //login success
                loading_btn.loadingSuccessful()
                loading_btn.animationEndAction = {
                    toNextPage()
                    Unit
                }
            } else {
                loading_btn.loadingFailed()
                Toast.makeText(applicationContext, "login failed,please check username and password", Toast.LENGTH_SHORT).show()
            }
        }, 3000)
    }


    //add a demo activity transition animation,this is a demo implement
    private fun toNextPage() {

        val cx = (loading_btn.left + loading_btn.right) / 2
        val cy = (loading_btn.top + loading_btn.bottom) / 2

        val animator = ViewAnimationUtils.createCircularReveal(animate_view, cx, cy, 0f, resources.displayMetrics.heightPixels * 1.2f)
        animator.duration = 500
        animator.interpolator = AccelerateDecelerateInterpolator()
        animate_view.visibility = View.VISIBLE
        animator.start()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                loading_btn.postDelayed({
                    loading_btn.reset()
                    animate_view.visibility = View.INVISIBLE
                },200)
            }

            override fun onAnimationEnd(animation: Animator) {
                startActivity(Intent(applicationContext, SecondActivity::class.java))
                overridePendingTransition(0,0)
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })

    }
}

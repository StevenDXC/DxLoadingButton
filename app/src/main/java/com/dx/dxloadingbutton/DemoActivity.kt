package com.dx.dxloadingbutton

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.dx.dxloadingbutton.demo.LoginDemoActivity
import kotlinx.android.synthetic.main.activity_main.*


class DemoActivity : AppCompatActivity(), View.OnClickListener {


    override fun onClick(view: View) {
        when (view.id) {
            R.id.loading_btn -> {
                loading_btn.startLoading()
                loading_btn.isEnabled = false
            }
            R.id.btn_success -> loading_btn.loadingSuccessful()
            R.id.btn_failed -> loading_btn.loadingFailed()
            R.id.btn_reset -> {
                loading_btn.reset()
                loading_btn.isEnabled = true
            }
            R.id.btn_cancel -> {
                loading_btn.cancelLoading()
                loading_btn.isEnabled = true
            }
            R.id.btn_demo -> {
                loading_btn.cancelLoading()
                startActivity(Intent(this@DemoActivity, LoginDemoActivity::class.java))
            }
            R.id.btn_enable -> {
                loading_btn.isEnabled = !loading_btn.isEnabled
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loading_btn.rippleColor = 0x33ffffff
        loading_btn.typeface = Typeface.SERIF
        loading_btn.setOnClickListener(this)
        loading_btn.cornerRadius = 100f
        val shader = LinearGradient(0f, 0f, 1000f, 100f, -0x551ac6cb, -0x5500a8de, Shader.TileMode.CLAMP)
        loading_btn.backgroundShader = shader
        loading_btn.animationEndAction = {
            Toast.makeText(applicationContext, "end:$it", Toast.LENGTH_SHORT).show()
        }

        btn_success.setOnClickListener(this)
        btn_failed.setOnClickListener(this)
        btn_reset.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
        btn_demo.setOnClickListener(this)
        btn_enable.setOnClickListener(this)

    }
}

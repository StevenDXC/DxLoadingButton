package com.dx.dxloadingbutton;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dx.dxloadingbutton.demo.DemoActivity;
import com.dx.dxloadingbutton.lib.LoadingButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private LoadingButton lb;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loading_btn:
                lb.startLoading();
                lb.setEnabled(false);
                break;
            case R.id.btn_success:
                lb.loadingSuccessful();
                break;
            case R.id.btn_failed:
                lb.loadingFailed();
                break;
            case R.id.btn_reset:
                lb.reset();
                lb.setEnabled(true);
                break;
            case R.id.btn_cancel:
                lb.cancelLoading();
                lb.setEnabled(true);
                break;
            case R.id.btn_demo:
                lb.cancelLoading();
                startActivity(new Intent(MainActivity.this, DemoActivity.class));
                break;
            case R.id.btn_enable:
                lb.setEnabled(!lb.isEnabled());
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lb = findViewById(R.id.loading_btn);
        lb.setTypeface(Typeface.SERIF);
        lb.setOnClickListener(this);
        lb.setCornerRadius(32f);
        Shader shader = new LinearGradient(0f,0f,1000f,100f, Color.GREEN, Color.BLUE, Shader.TileMode.CLAMP);
        lb.setBackgroundShader(shader);

        findViewById(R.id.btn_success).setOnClickListener(this);
        findViewById(R.id.btn_failed).setOnClickListener(this);
        findViewById(R.id.btn_reset).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_demo).setOnClickListener(this);
        findViewById(R.id.btn_enable).setOnClickListener(this);

    }
}

package com.dx.dxloadingbutton;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dx.dxloadingbutton.demo.DemoActivity;
import com.dx.dxloadingbutton.lib.LoadingButton;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button sucBtn = (Button) findViewById(R.id.btn_success);
        final Button failedBtn = (Button) findViewById(R.id.btn_failed);
        final Button resetBtn = (Button) findViewById(R.id.btn_reset);
        final Button cancelBtn = (Button) findViewById(R.id.btn_cancel);
        final Button demoBtn = (Button) findViewById(R.id.btn_demo);

        final LoadingButton lb = (LoadingButton)findViewById(R.id.loading_btn);
        lb.setTypeface(Typeface.SERIF);
        lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb.startLoading();
            }
        });
        
        sucBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb.loadingSuccessful();
            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb.loadingFailed();
                Toast.makeText(getApplicationContext(),"login failed,try again",Toast.LENGTH_SHORT).show();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                lb.reset();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb.cancelLoading();
            }
        });


        demoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb.cancelLoading();
                startActivity(new Intent(MainActivity.this, DemoActivity.class));
            }
        });
    }
}

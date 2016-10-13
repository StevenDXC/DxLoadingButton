package com.dx.dxloadingbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dx.dxloadingbutton.widget.LoadingButton;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button sucBtn = (Button) findViewById(R.id.btn_success);
        final Button failedBtn = (Button) findViewById(R.id.btn_failed);
        final Button resetBtn = (Button) findViewById(R.id.btn_reset);

        final LoadingButton lb = (LoadingButton)findViewById(R.id.loading_btn);
        lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb.startLoading();
                sucBtn.setEnabled(true);
                failedBtn.setEnabled(true);
            }
        });

        sucBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb.loadingSuccessful();
                sucBtn.setEnabled(false);
                failedBtn.setEnabled(false);
                resetBtn.setEnabled(true);
            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb.loadingFiled();
                Toast.makeText(getApplicationContext(),"login failed,try again",Toast.LENGTH_SHORT).show();
                sucBtn.setEnabled(false);
                failedBtn.setEnabled(false);
                resetBtn.setEnabled(!lb.isResetAfterFailed());
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                lb.reset();
                sucBtn.setEnabled(false);
                failedBtn.setEnabled(false);
                resetBtn.setEnabled(false);
            }
        });
    }
}

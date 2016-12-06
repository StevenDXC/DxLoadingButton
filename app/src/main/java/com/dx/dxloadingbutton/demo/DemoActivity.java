package com.dx.dxloadingbutton.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dx.dxloadingbutton.MainActivity;
import com.dx.dxloadingbutton.R;
import com.dx.dxloadingbutton.lib.LoadingButton;


public class DemoActivity extends AppCompatActivity {


    private EditText mEditUserName,mEditPassword;
    private LoadingButton mLoadingBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mLoadingBtn = (LoadingButton)findViewById(R.id.loading_btn);
        //when login failed, reset view to button with animation
        mLoadingBtn.setResetAfterFailed(true);
        mLoadingBtn.setAnimationEndListener(new LoadingButton.AnimationEndListener() {
            @Override
            public void onAnimationEnd(LoadingButton.AnimationType animationType) {
                if(animationType == LoadingButton.AnimationType.SUCCESSFUL){
                    startActivity(new Intent(DemoActivity.this, MainActivity.class));
                }
            }
        });

        mEditUserName = (EditText) findViewById(R.id.edit_user_name);
        mEditPassword = (EditText) findViewById(R.id.edit_password);


        mLoadingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });
    }

    private void checkLogin(){

        final String userName = mEditUserName.getText().toString();
        if(userName.length() == 0){
            mEditUserName.setError("please input user name");
            return;
        }

        final String password = mEditPassword.getText().toString();
        if(password.length() == 0){
            mEditPassword.setError("please input password");
            return;
        }

        mLoadingBtn.startLoading();
        //send login request

        //demo
        mEditUserName.setEnabled(false);
        mEditPassword.setEnabled(false);
        mLoadingBtn.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEditUserName.setEnabled(true);
                mEditPassword.setEnabled(true);
               if("admin".endsWith(userName) && "admin".equals(password)){
                   //login success
                   mLoadingBtn.loadingSuccessful();
               }else{
                   mLoadingBtn.loadingFailed();
                   Toast.makeText(getApplicationContext(),"login failad,please check username and password",Toast.LENGTH_SHORT).show();
               }
            }
        },3000);
    }
}

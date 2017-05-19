package com.dx.dxloadingbutton.demo;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.dx.dxloadingbutton.MainActivity;
import com.dx.dxloadingbutton.R;
import com.dx.dxloadingbutton.lib.LoadingButton;


public class DemoActivity extends AppCompatActivity {


    private EditText mEditUserName,mEditPassword;
    private LoadingButton mLoadingBtn;

    private View animateView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mLoadingBtn = (LoadingButton)findViewById(R.id.loading_btn);
        //while login failed, reset view to button with animation
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

        animateView = findViewById(R.id.animate_view);
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
                   mLoadingBtn.setAnimationEndListener(new LoadingButton.AnimationEndListener() {
                       @Override
                       public void onAnimationEnd(LoadingButton.AnimationType animationType) {
                           toNextPage();
                       }
                   });
               }else{
                   mLoadingBtn.loadingFailed();
                   Toast.makeText(getApplicationContext(),"login failed,please check username and password",Toast.LENGTH_SHORT).show();
               }
            }
        },3000);
    }


    //add a demo activity transition animation,this is a demo implement
    private void toNextPage(){

        int cx = (mLoadingBtn.getLeft() + mLoadingBtn.getRight()) / 2;
        int cy = (mLoadingBtn.getTop() + mLoadingBtn.getBottom()) / 2;

        Animator animator = ViewAnimationUtils.createCircularReveal(animateView,cx,cy,0,getResources().getDisplayMetrics().heightPixels * 1.2f);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateInterpolator());
        animateView.setVisibility(View.VISIBLE);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(DemoActivity.this,SecondActivity.class));
                mLoadingBtn.reset();
                animateView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}

package com.dx.dxloadingbutton.lib;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;



public class LoadingButton extends View {

    public enum AnimationType {
        SUCCESSFUL,FAILED
    }

    private static final int STATE_BUTTON = 0;
    private static final int STATE_ANIMATION_STEP1 = 1;
    private static final int STATE_ANIMATION_STEP2 = 2;
    private static final int STATE_ANIMATION_LOADING = 3;
    private static final int STATE_STOP_LOADING = 4;
    private static final int STATE_ANIMATION_SUCCESS = 5;
    private static final int STATE_ANIMATION_FAILED = 6;

    private AnimationEndListener mAnimationEndListener;

    private int mColorPrimary;
    private int mDisabledBgColor;
    private int mTextColor;
    private int mDisabledTextColor;
    private int mRippleColor;
    private float mRippleAlpha;
    private boolean resetAfterFailed; //when loading data failed, reset view to normal state
    private String mText;

    private float mPadding;

    private Paint mPaint,ripplePaint;
    private Paint mStrokePaint;
    private Paint mTextPaint;
    private Paint mPathEffectPaint;
    private Paint mPathEffectPaint2;

    private Path mPath;
    private int mScaleWidth;
    private int mScaleHeight;
    private int mDegree;
    private int mAngle;
    private int mEndAngle;
    private int mCurrentState;
    private float mDensity;
    private float mButtonCorner;
    private int mRadius;
    private int width;
    private int height;
    private Matrix mMatrix = new Matrix();
    private float mTextWidth,mTextHeight;

    private Path mSuccessPath;
    private float mSuccessPathLength;
    private float[] mSuccessPathIntervals;

    private Path mFailedPath;
    private Path mFailedPath2;
    private float mFailedPathLength;
    private float[] mFailedPathIntervals;

    private float mTouchX,mTouchY;
    private float mRippleRadius;

    private RectF mButtonRectF,mArcRectF;

    private AnimatorSet mLoadingAnimatorSet;

    public LoadingButton(Context context) {
        super(context);
        init(context,null);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context,attrs);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);

        init(context,attrs);
    }


    private void init(Context context,AttributeSet attrs){

        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0);
            mColorPrimary = ta.getInt(R.styleable.LoadingButton_lb_btnColor,Color.BLUE);
            mDisabledBgColor = ta.getColor(R.styleable.LoadingButton_lb_btnDisabledColor,Color.LTGRAY);
            mDisabledTextColor = ta.getColor(R.styleable.LoadingButton_lb_disabledTextColor,Color.DKGRAY);
            String text = ta.getString(R.styleable.LoadingButton_lb_btnText);
            mText = text == null ? "" : text;
            mTextColor = ta.getColor(R.styleable.LoadingButton_lb_textColor,Color.WHITE);
            resetAfterFailed = ta.getBoolean(R.styleable.LoadingButton_lb_resetAfterFailed,true);
            mRippleColor = ta.getColor(R.styleable.LoadingButton_lb_btnRippleColor,Color.BLACK);
            mRippleAlpha = ta.getFloat(R.styleable.LoadingButton_lb_btnRippleAlpha,0.3f);
            ta.recycle();
        }

        mCurrentState = STATE_BUTTON;
        mScaleWidth = 0;
        mScaleHeight = 0;
        mDegree = 0;
        mAngle = 0;
        mDensity = getResources().getDisplayMetrics().density;
        mButtonCorner = 2 * mDensity;
        mPadding = 6 * mDensity;

        mPaint = new Paint();
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColorPrimary);
        mPaint.setStyle(Paint.Style.FILL);
        setShadowDepth1();

        ripplePaint = new Paint();
        ripplePaint.setAntiAlias(true);
        ripplePaint.setColor(mRippleColor);
        ripplePaint.setAlpha((int)(mRippleAlpha*255));
        ripplePaint.setStyle(Paint.Style.FILL);

        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setColor(mColorPrimary);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(2*mDensity);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(16*mDensity);
        mTextPaint.setFakeBoldText(true);
        mTextWidth = mTextPaint.measureText(mText);
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(mText,0,mText.length(),bounds);
        mTextHeight = bounds.height();

        mPathEffectPaint = new Paint();
        mPathEffectPaint.setAntiAlias(true);
        mPathEffectPaint.setColor(mColorPrimary);
        mPathEffectPaint.setStyle(Paint.Style.STROKE);
        mPathEffectPaint.setStrokeWidth(2*mDensity);

        mPathEffectPaint2 = new Paint();
        mPathEffectPaint2.setAntiAlias(true);
        mPathEffectPaint2.setColor(mColorPrimary);
        mPathEffectPaint2.setStyle(Paint.Style.STROKE);
        mPathEffectPaint2.setStrokeWidth(2*mDensity);

        mPath = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension((int)(88*mDensity),widthMeasureSpec);
        int height = measureDimension((int)(56*mDensity),heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        mRadius = (int)(height - mPadding*2)/2;

        if(mButtonRectF == null){
            mButtonRectF = new RectF();
            mButtonRectF.top = mPadding;
            mButtonRectF.bottom = height - mPadding;
            mArcRectF = new RectF(width/2-mRadius,mPadding,width/2+mRadius,height - mPadding);
        }

    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled()){
            return true;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mTouchX = event.getX();
                mTouchY = event.getY();
                playRippleAnimation(true);
                break;
            case MotionEvent.ACTION_UP:
                playRippleAnimation(false);
                break;
        }

        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(mCurrentState == STATE_BUTTON){
           checkEnabled();
        }
    }

    private void checkEnabled() {
        if(isEnabled()){
            mPaint.setColor(mColorPrimary);
            mTextPaint.setColor(mTextColor);
        }else{
            mPaint.setColor(mDisabledBgColor);
            mTextPaint.setColor(mDisabledTextColor);
        }
        invalidate();
    }

    private int measureDimension(int defaultSize, int measureSpec) {

        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        }
        else {
            result = defaultSize;
        }
        return result;
    }

    /**
     * start loading,play animation
     */
    public void startLoading(){

        if(mCurrentState == STATE_ANIMATION_FAILED && !resetAfterFailed){
            scaleFailedPath();
            return;
        }

        if(mCurrentState == STATE_BUTTON){
            mCurrentState = STATE_ANIMATION_STEP1;
            removeShadow();
            playStartAnimation(false);
        }

    }

    /**
     * loading data successful
     */
    public void loadingSuccessful(){
        if(mLoadingAnimatorSet != null && mLoadingAnimatorSet.isStarted()){
            mLoadingAnimatorSet.end();
            mCurrentState = STATE_STOP_LOADING;
            playSuccessAnimation();
        }
    }

    /**
     * loading data failed
     */
    public void loadingFailed(){
        if(mLoadingAnimatorSet != null && mLoadingAnimatorSet.isStarted()){
            mLoadingAnimatorSet.end();
            mCurrentState = STATE_STOP_LOADING;
            playFailedAnimation();
        }
    }

    public void cancelLoading(){
        if(mCurrentState != STATE_ANIMATION_LOADING){
            return;
        }
        cancel();
    }

    /**
     * reset view to Button with animation
     */
    public void reset(){

        if(mCurrentState == STATE_ANIMATION_SUCCESS){
            scaleSuccessPath();
        }

        if(mCurrentState ==  STATE_ANIMATION_FAILED){
            scaleFailedPath();
        }
    }

    public void setTypeface(Typeface typeface){
        if(typeface != null){
            mTextPaint.setTypeface(typeface);
            invalidate();
        }
    }

    public void setText(String text){
        if(TextUtils.isEmpty(text)){
            return;
        }
        this.mText = text;
        mTextWidth = mTextPaint.measureText(mText);
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(mText,0,mText.length(),bounds);
        mTextHeight = bounds.height();
        invalidate();
    }

    public void setTextSize(int size){
        mTextPaint.setTextSize(size*mDensity);
        mTextWidth = mTextPaint.measureText(mText);
        invalidate();
    }

    public void setTextColor(int color){
        mTextPaint.setColor(color);
        invalidate();
    }

    public void setResetAfterFailed(boolean resetAfterFailed){
        this.resetAfterFailed = resetAfterFailed;
    }

    public boolean isResetAfterFailed(){
        return resetAfterFailed;
    }

    public int getCurrentState(){
        return mCurrentState;
    }

    public void setAnimationEndListener(AnimationEndListener animationEndListener){
        this.mAnimationEndListener = animationEndListener;
    }

    private void createSuccessPath(){

        if(mSuccessPath != null){
            mSuccessPath.reset();
        }else{
            mSuccessPath = new Path();
        }

        float mLineWith = 2*mDensity;

        float left = width/2 - mRadius + mRadius/3 + mLineWith;
        float top = mPadding + mRadius/2 + mLineWith;
        float right = width/2 + mRadius - mLineWith - mRadius/3;
        float bottom = (mLineWith + mRadius) * 1.5f + mPadding/2;
        float xPoint = width/2 - mRadius/6;

        mSuccessPath = new Path();
        mSuccessPath.moveTo(left, mPadding+mRadius + mLineWith);
        mSuccessPath.lineTo(xPoint,bottom);
        mSuccessPath.lineTo(right,top);

        PathMeasure measure = new PathMeasure(mSuccessPath, false);
        mSuccessPathLength = measure.getLength();
        mSuccessPathIntervals = new float[]{mSuccessPathLength, mSuccessPathLength};
    }

    private void createFailedPath(){

        if(mFailedPath != null){
            mFailedPath.reset();
            mFailedPath2.reset();
        }else{
            mFailedPath = new Path();
            mFailedPath2 = new Path();
        }

        float left = width/2 - mRadius + mRadius/2;
        float top = mRadius/2 + mPadding;

        mFailedPath.moveTo(left,top);
        mFailedPath.lineTo(left+mRadius,top+mRadius);

        mFailedPath2.moveTo(width/2 + mRadius/2,top);
        mFailedPath2.lineTo(width/2 - mRadius + mRadius/2,top+mRadius);

        PathMeasure measure = new PathMeasure(mFailedPath, false);
        mFailedPathLength = measure.getLength();
        mFailedPathIntervals = new float[]{mFailedPathLength, mFailedPathLength};

        PathEffect PathEffect = new DashPathEffect(mFailedPathIntervals, mFailedPathLength);
        mPathEffectPaint2.setPathEffect(PathEffect);
    }


    private void setShadowDepth1(){
        mPaint.setShadowLayer(1*mDensity,0,1*mDensity,0x1F000000);
    }
    private void setShadowDepth2(){
        mPaint.setShadowLayer(2*mDensity,0,2*mDensity,0x1F000000);
    }

    private void removeShadow(){
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColorPrimary);
        mPaint.setStyle(Paint.Style.FILL);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (mCurrentState) {
            case STATE_BUTTON:
            case STATE_ANIMATION_STEP1:
                float cornerRadius = (mRadius-mButtonCorner)*(mScaleWidth/(float)(width/2-height/2))+mButtonCorner;
                mButtonRectF.left = mScaleWidth;
                mButtonRectF.right = width - mScaleWidth;
                canvas.drawRoundRect(mButtonRectF,cornerRadius,cornerRadius,mPaint);
                if(mCurrentState == STATE_BUTTON){
                    canvas.drawText(mText,(width-mTextWidth)/2,(height-mTextHeight)/2+mPadding*2,mTextPaint);
                    if(mTouchX > 0 || mTouchY > 0){
                        canvas.clipRect(0,mPadding,width,height-mPadding);
                        canvas.drawCircle(mTouchX,mTouchY,mRippleRadius,ripplePaint);
                    }
                }
                break;
            case STATE_ANIMATION_STEP2:
                canvas.drawCircle(width/2,height/2,mRadius-mScaleHeight,mPaint);
                canvas.drawCircle(width/2,height/2,mRadius-mDensity,mStrokePaint);
                break;
            case STATE_ANIMATION_LOADING:
                mPath.reset();
                mPath.addArc(mArcRectF,270+mAngle/2,360-mAngle);
                if(mAngle != 0){
                    mMatrix.setRotate(mDegree,width/2,height/2);
                    mPath.transform(mMatrix);
                    mDegree += 10;
                }
                canvas.drawPath(mPath,mStrokePaint);
                break;
            case STATE_STOP_LOADING:
                mPath.reset();
                mPath.addArc(mArcRectF,270+mAngle/2,mEndAngle);
                if(mEndAngle != 360){
                    mMatrix.setRotate(mDegree,width/2,height/2);
                    mPath.transform(mMatrix);
                    mDegree += 10;
                }
                canvas.drawPath(mPath,mStrokePaint);
                break;
            case STATE_ANIMATION_SUCCESS:
                canvas.drawPath(mSuccessPath,mPathEffectPaint);
                canvas.drawCircle(width/2,height/2,mRadius-mDensity,mStrokePaint);
                break;
            case STATE_ANIMATION_FAILED:
                canvas.drawPath(mFailedPath,mPathEffectPaint);
                canvas.drawPath(mFailedPath2,mPathEffectPaint2);
                canvas.drawCircle(width/2,height/2,mRadius-mDensity,mStrokePaint);
                break;
        }
    }

    private void playStartAnimation(final boolean isReverse){

        ValueAnimator animator = ValueAnimator.ofInt(isReverse ? width/2-height/2 :0,isReverse ? 0 :width/2-height/2);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mScaleWidth = (Integer) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(400);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setStartDelay(100);
        animator.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mCurrentState = isReverse ? STATE_BUTTON : STATE_ANIMATION_STEP2;
                if(mCurrentState == STATE_BUTTON){
                    setShadowDepth1();
                    invalidate();
                }
            }
        });

        ValueAnimator animator2 = ValueAnimator.ofInt(isReverse ? mRadius : 0,isReverse? 0 : mRadius);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mScaleHeight = (Integer) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator2.setDuration(240);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mCurrentState = isReverse ? STATE_ANIMATION_STEP1 : STATE_ANIMATION_LOADING;
                if(!isReverse){
                    checkEnabled();
                }
            }
        });

        ValueAnimator loadingAnimator = ValueAnimator.ofInt(30,300);
        loadingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAngle = (Integer) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        loadingAnimator.setDuration(1000);
        loadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnimator.setRepeatMode(ValueAnimator.REVERSE);
        loadingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        if(mLoadingAnimatorSet != null) {
            mLoadingAnimatorSet.cancel();
        }
        mLoadingAnimatorSet = new AnimatorSet();
        if(isReverse){
            mLoadingAnimatorSet.playSequentially(animator2,animator);
            checkEnabled();
        }else{
            mLoadingAnimatorSet.playSequentially(animator,animator2,loadingAnimator);
        }
        mLoadingAnimatorSet.start();
    }

    private void playSuccessAnimation(){

        createSuccessPath();

        ValueAnimator animator = ValueAnimator.ofInt(360-mAngle,360);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mEndAngle = (Integer) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(240);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mCurrentState = STATE_ANIMATION_SUCCESS;
            }
        });

        ValueAnimator successAnimator = ValueAnimator.ofFloat(0.0f,1.0f);
        successAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                PathEffect PathEffect = new DashPathEffect(mSuccessPathIntervals, mSuccessPathLength - mSuccessPathLength*value);
                mPathEffectPaint.setPathEffect(PathEffect);
                invalidate();
            }
        });
        successAnimator.setDuration(500);
        successAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animator,successAnimator);
        set.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                if(mAnimationEndListener != null){
                    mAnimationEndListener.onAnimationEnd(AnimationType.SUCCESSFUL);
                }
            }
        });
        set.start();
    }

    private void playFailedAnimation(){

        createFailedPath();

        ValueAnimator animator = ValueAnimator.ofInt(360-mAngle,360);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mEndAngle = (Integer) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(240);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mCurrentState = STATE_ANIMATION_FAILED;
            }
        });

        ValueAnimator failedAnimator = ValueAnimator.ofFloat(0.0f,1.0f);
        failedAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                PathEffect PathEffect = new DashPathEffect(mFailedPathIntervals, mFailedPathLength - mFailedPathLength*value);
                mPathEffectPaint.setPathEffect(PathEffect);
                invalidate();
            }
        });
        failedAnimator.setDuration(300);
        failedAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        ValueAnimator failedAnimator2 = ValueAnimator.ofFloat(0.0f,1.0f);
        failedAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                PathEffect PathEffect = new DashPathEffect(mFailedPathIntervals, mFailedPathLength - mFailedPathLength*value);
                mPathEffectPaint2.setPathEffect(PathEffect);
                invalidate();
            }
        });
        failedAnimator2.setDuration(300);
        failedAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animator,failedAnimator,failedAnimator2);
        set.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                if(resetAfterFailed){
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scaleFailedPath();
                        }
                    },1000);
                    return;
                }

                if(mAnimationEndListener != null){
                    mAnimationEndListener.onAnimationEnd(AnimationType.FAILED);
                }
            }
        });
        set.start();
    }

    private void cancel(){
        mCurrentState = STATE_STOP_LOADING;
        ValueAnimator animator = ValueAnimator.ofInt(360-mAngle,360);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mEndAngle = (Integer) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(240);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mCurrentState = STATE_ANIMATION_STEP2;
                playStartAnimation(true);
            }
        });
        animator.start();
    }

    private void scaleSuccessPath(){
        final Matrix scaleMatrix = new Matrix();
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(1.0f,0.0f);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                scaleMatrix.setScale(value,value,width/2,height/2);
                mSuccessPath.transform(scaleMatrix);
                invalidate();
            }
        });
        scaleAnimator.setDuration(300);
        scaleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimator.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mCurrentState = STATE_ANIMATION_STEP2;
                playStartAnimation(true);
            }
        });
        scaleAnimator.start();
    }

    private void scaleFailedPath(){
        final Matrix scaleMatrix = new Matrix();
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(1.0f,0.0f);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                scaleMatrix.setScale(value,value,width/2,height/2);
                mFailedPath.transform(scaleMatrix);
                mFailedPath2.transform(scaleMatrix);
                invalidate();
            }
        });
        scaleAnimator.setDuration(300);
        scaleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimator.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mCurrentState = STATE_ANIMATION_STEP2;
                playStartAnimation(true);
            }
        });
        scaleAnimator.start();
    }

    private void playRippleAnimation(final boolean isTouchDown){
        setShadowDepth2();
        ValueAnimator rippleAnimator = ValueAnimator.ofFloat(isTouchDown ? 0 : width/2, isTouchDown ? width/2 : width);
        rippleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mRippleRadius = (Float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        rippleAnimator.setDuration(240);
        rippleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        if(!isTouchDown){
            rippleAnimator.addListener(new AnimatorEndListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    performClick();
                    mTouchX = 0;
                    mTouchY = 0;
                    mRippleRadius = 0;
                    invalidate();
                }
            });
        }
        rippleAnimator.start();
    }

    private abstract class AnimatorEndListener implements Animator.AnimatorListener{

        @Override
        public void onAnimationStart(Animator animator){}

        @Override
        public void onAnimationCancel(Animator animator){}

        @Override
        public void onAnimationRepeat(Animator animator){}
    }


    public interface AnimationEndListener{
        void onAnimationEnd(AnimationType animationType);
    }

}

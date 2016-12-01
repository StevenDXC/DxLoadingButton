# DxLoadingButton

android button to loading view with animation,and load successful/failed animation

Demo:
---
   
![image](https://github.com/StevenDXC/DxLoadingButton/blob/master/image/loadingButton.gif)

Usage:
---

layout:

```xml
<com.dx.dxloadingbutton.widget.LoadingButton
        android:id="@+id/loading_btn"
        android:layout_gravity="center"
        android:layout_width="228dp"
        android:layout_height="wrap_content"
        app:resetAfterFailed="true"
        app:rippleColor="#000000"
        app:text="@string/button_text"
        />
```
code:

```java
LoadingButton lb = (LoadingButton)findViewById(R.id.loading_btn);
lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb.startLoading(); //start loading 
            }
});
```
show successful animation:

```java
 lb.loadingSuccessful();
```
show failed animation:

```java
 lb.loadingFailed();
```
cancel loading:

```java
 lb.cancelLoading();
```

reset:

```java
 lb.reset();
```

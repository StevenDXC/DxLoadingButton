# DxLoadingButton

android button to loading view with animation,and load successful/failed animation


[![](https://jitpack.io/v/StevenDXC/DxLoadingButton.svg)](https://jitpack.io/#StevenDXC/DxLoadingButton)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3a594b0963ba4d3b9fab95b6f429032d)](https://www.codacy.com/app/StevenDXC/DxLoadingButton?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=StevenDXC/DxLoadingButton&amp;utm_campaign=Badge_Grade)

Demo:
---
   
![image](https://github.com/StevenDXC/DxLoadingButton/blob/master/image/loadingButton.gif)

with activity transition animation demo:

![image](https://github.com/StevenDXC/DxLoadingButton/blob/master/image/loadingButton2.gif)

Usage:
---

layout:

```xml
<com.dx.dxloadingbutton.lib.LoadingButton
     android:id="@+id/loading_btn"
     android:layout_gravity="center"
     android:layout_width="228dp"
     android:layout_height="wrap_content"
     app:lb_resetAfterFailed="true"
     app:lb_btnRippleColor="#000000"
     app:lb_btnDisabledColor="#cccccc"
     app:lb_disabledTextColor="#999999"
     app:lb_cornerRadius="32"
     app:lb_rippleEnable="true"					  
     app:lb_btnText="@string/button_text"
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

enable:

```java
 lb.setEnable(true/false); 
```
* notice: lb_btnDisabledColor & lb_disabledTextColor only display while LoadingButton is normal button state, LoadingButton is playing animation or other state will display normal color

   
![image](https://github.com/StevenDXC/DxLoadingButton/blob/master/image/loadingButton_shader.jpg)

backgroundShader:

```java
lb.setBackgroundShader(new LinearGradient(0f,0f,1000f,100f, 0xAAE53935, 0xAAFF5722, Shader.TileMode.CLAMP));
```
cornerRadius:

```java
lb.setCornerRadius(32f)
```


dependency
---
Add it in your root build.gradle at the end of repositories:

```java
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
```
add dependency：

```java
dependencies {
    compile 'com.github.StevenDXC:DxLoadingButton:2.2'
}
```

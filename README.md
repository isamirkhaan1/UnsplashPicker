## UnsplashPicker
A picker library for popular images of [Unsplash](https://www.unsplash.com)

## Setup
### Dependency
  - Add JitPack to your project level build.gradle
  - Add the following dependency to app level build.gradle
  ```groovy
  dependencies {
    implementation 'com.github.samirk433:UnsplashPicker:1.0.0'
}
  ```
### Permissions
Following 1 & only 1 permission is needed for this library. I cannot explain _exactly_ why this permission is needed because **THIS IS BEYOND SCIENCE**

    <uses-permission android:name="android.permission.INTERNET" />

## Usage
### Add Unsplash Secret Key
* Go to [Unsplash Developers](https://unsplash.com/developers)
* Register as a developer and create an app
* Copy the **secret key** to string resources file `strings.xml` as follow
    ```xml
     <string name="unsplash_api_key">API KEY HERE</string>
   ```
* Open `Manifest.xml` file and add following meta-data inside _<application>_
   ```xml
     <meta-data
        android:name="unsplash_api_key" 
        android:value="@string/unsplash_api_key" />
   ```
### Essentials
Display UnsplashPicker dialog and register a callback on image selection

```java
 UnsplashPicker.show(context, new OnPhotoSelection() {
                    @Override
                    public void onPhotoSelect(String url) {
                        //use this URI to download image
                    }
                });
```

## Contributions
Contributions are welcome. This is my 1st OS library and I want to encourage other novices to start contributing. Therefore I have left some features/bugs that you may be interested to work on. 
* Replace `Gridview` with `Recyclerview (GridLayout)`
* Add endless scrolling
* [BUG] [okhttp](https://square.github.io/okhttp/) doesn't work with [Unsplash](www.unsplash.com) Server on  Android API 19
* And most importantly, improve README

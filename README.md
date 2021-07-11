# android-image-saver

## Installation

- Add the following repositories to your project/build.gradle file.

```groovy
repositories {
   maven { url 'https://jitpack.io' }
}
```

- Add the following dependency to your project/app/build.gradle file.

```groovy
dependencies {
    implementation 'com.github.prongbang:android-image-saver:1.0.0'
}
```

## Usage

- Add in AndroidManifest.xml

```xml
<application
    android:requestLegacyExternalStorage="true"/>
```

- Save bitmap to file

```kotlin
ImageSaver.to(this)
    .directory("android-image-saver")
    .filename("test")
    .extension(ImageExtension.PNG)
    .listener(object : ImageSaver.Listener {
        override fun onSuccess(file: File) { }
        override fun onFailure(exception: Exception) { }
    })
    .save(bitmap, 80)
```

- Save drawable to file

```kotlin
ImageSaver.to(this)
    .directory("android-image-saver")
    .filename("test")
    .extension(ImageExtension.PNG)
    .listener(object : ImageSaver.Listener {
        override fun onSuccess(file: File) { }
        override fun onFailure(exception: Exception) { }
    })
    .save(ContextCompat.getDrawable(this, R.mipmap.ic_launcher), 80)
```

- Save base64 to file

```kotlin
ImageSaver.to(this)
    .directory("android-image-saver")
    .filename("test")
    .extension(ImageExtension.PNG)
    .listener(object : ImageSaver.Listener {
        override fun onSuccess(file: File) { }
        override fun onFailure(exception: Exception) { }
    })
    .save(base64, 80)
```
# Caffe OpenCV Android App
This is an example of an Android app that uses OpenCV DNN module to load a Caffe model and predict an image.

## Prerequisites

### OpenCV
- Download OpenCV source code from here: https://github.com/Itseez/opencv
- Download OpenCV external modules from here: https://github.com/Itseez/opencv_contrib

### Android Studio
- Download & install Android Studio:  http://developer.android.com/sdk/index.html
- Download & install Android SDK:  http://developer.android.com/sdk/index.html (It should be installed with the Android Studio)

##Requirements

###Install NDK Bundle
- Open Android Studio and navigate Tools->Android->SDK Manager. Choose "SDK Tools" then tick "Android NDK". Finally "Apply" and close. You may need to restart. 


###Install Android NDK
- Download and install "Android NDK". Follow instruction here: http://developer.android.com/ndk/downloads/index.html 

**IMPORTANT: MAKE SURE THAT YOUR "Android NDK" IS ON YOUR `$PATH` SO THAT YOU CAN USE IT FROM ANYWHERE**



###Build OpenCV with extra modules for Android
Unfortunately the Deep Neural Network (DNN) module for OpenCV is not part of the main OpenCV distribution yet.

It resides in the extra modules. So we have to build OpenCV along extra modules. For this we need to build them together.

Here is how:

Open terminal and do:

``
$ cd <opencv_build_directory>
$ cmake -DOPENCV_EXTRA_MODULES_PATH=<opencv_contrib>/modules <opencv_source_directory>
$ make -j5
``


 

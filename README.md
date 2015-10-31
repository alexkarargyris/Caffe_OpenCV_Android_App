# Caffe OpenCV Android App
This is an example of an Android app that uses OpenCV DNN module to load a Caffe model and predict an image. It is basically the OpenCV tutorial for DNN: http://docs.opencv.org/master/d5/de7/tutorial_dnn_googlenet.html#gsc.tab=0

##A. Prerequisites

### OpenCV
- Download OpenCV source code from here: https://github.com/Itseez/opencv
- Download OpenCV external modules from here: https://github.com/Itseez/opencv_contrib

### Android Studio
- Download & install Android Studio:  http://developer.android.com/sdk/index.html 

This will install Android SDK.

##Requirements

###1. Install NDK Bundle
- Open Android Studio and navigate Tools->Android->SDK Manager. Choose "SDK Tools" then tick "Android NDK". Finally "Apply" and close. You may need to restart. 
![Alt text](https://dl.dropboxusercontent.com/u/7591304/OpenCV%20Tutorial/NDK.png?raw=true "Optional Title")


###2. Install Android NDK
- Download and install "Android NDK". Follow instruction here: http://developer.android.com/ndk/downloads/index.html 

**IMPORTANT: MAKE SURE THAT YOUR "Android NDK" IS ON YOUR `$PATH` SO THAT YOU CAN USE IT FROM ANYWHERE 
(e.g. bash_profile on Mac: https://github.com/9miao/CrossApp/wiki/Android-Development-Environment-Configuration-in-Mac-OS-X#2bash_profile-file-configuration)**



###3. Build Android OpenCV SDK with extra modules for Android
Unfortunately the Deep Neural Network (DNN) module for OpenCV is not part of the main OpenCV distribution yet.

It resides in the extra modules. So we have to build OpenCV along with extra modules. For this we need to build them together.

Here is how:

- Open your file explorer (e.g. Finder on Mac) and navigate to ``/Users/alexandroskarargyris/Downloads/opencv/platforms/scripts``. Replace with your own path.
- Open ``cmake_android_arm.sh`` with a text editor (i.e. Textedit on Mac)
- Edit it to look like this:


<pre>
#!/bin/sh
cd `dirname $0`/..

mkdir -p build_android_arm
cd build_android_arm

cmake -DOPENCV_EXTRA_MODULES_PATH=/Users/alexandroskarargyris/Downloads/opencv_contrib/modules -DCMAKE_BUILD_WITH_INSTALL_RPATH=ON -DCMAKE_TOOLCHAIN_FILE=../android/android.toolchain.cmake $@ ../..
</pre>

where ``-DOPENCV_EXTRA_MODULES_PATH=`` points to where the OpenCV external modules reside. So replace it with your own path. Save and exit your text editor.

- Open terminal:

<pre>
$ cd /Users/alexandroskarargyris/Downloads/opencv/platforms/
$ sh ./scripts/cmake_android_arm.sh
</pre>

- After making is finished then run: 

<pre>
$ cd build_android_arm
$ make install
</pre>

This process will install the Android OpenCV SDK under ``/Users/alexandroskarargyris/Downloads/opencv/platforms/build_android_arm/install`` folder. That concludes the most important part: building the Android OpenCV SDK with extra modules (e.g. DNN) from source code. 


##B. App Development

For this part I used the wonderful instructions from https://github.com/quanhua92/NDK_OpenCV_AndroidStudio

- Open Android Studio

- Start a New Project (e.g. MyApplication)

- In the project structure navigate to app->src->main->java->myapplication and add a new class (e.g. NativeClass) with the following method:

<pre>
 public class NativeClass {
    public native static String getStringFromNative();
 }
</pre>

![Alt text](https://dl.dropboxusercontent.com/u/7591304/OpenCV%20Tutorial/class.png?raw=true "Optional Title")


- Open a terminal, change to app/src/main and run the following command:

``javah -d jni -classpath ../../build/intermediates/classes/debug/ com.example.alexandroskarargyris.myapplication.NativeClass``


This command basically creates the headr file for native C/C++ to allow access to C/C++ OpenCV calls.

- Back to Android Studio you will find a new folder ``jni`` as well as the the header:  ``com_example_alexandroskarargyris_myapplication_NativeClass.h``. Open it and you will see the following method:
<pre>
JNIEXPORT jstring JNICALL Java_com_example_alexandroskarargyris_myapplication_NativeClass_getStringFromNative
        (JNIEnv * env, jobject obj);
</pre>

![Alt text](https://dl.dropboxusercontent.com/u/7591304/OpenCV%20Tutorial/native_header.png?raw=true "Optional Title")

This is the C/C++ method that ``getStringFromNative()`` in ``NativeClass`` is going to call. So let's create a .cpp to have our C++ code for this method.



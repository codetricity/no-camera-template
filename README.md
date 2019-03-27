# No-Camera Sample App

Sample app for Dream and Build contest participants without a camera.
The app reduces RICOH THETA image size from 10.7MB to 0.12MB for
transmission over unstable networks.  The original file is preserved
in the camera. The idea is to set up a timelapse to take 1,000 pictures,
one image every 5 minutes and transmit the small files automatically.
A person can look at the small image and decide if they want the larger
image.

The main branch of this app was developed on an Android Studio AVD and
uses RICOH THETA images that need to be loaded into the AVD for testing
with adb. 

The [with-camera](https://github.com/codetricity/no-camera-sample/tree/with-camera) 
branch was developed with a RICOH THETA V. This branch allows the
photographer to take a picture with the shutter button on the camera.
The shutter button also triggers the automatic processing of the image.

## Simulating Picture Taking



## Loading RICOH THETA Sample Images Manually

RICOH THETA V sample images are available here:
 * https://community.theta360.guide/t/sample-media-and-development-resources/3754

If you do not have a camera, load the sample image into /sdcard/DCIM/100RICOH with
 
    adb push /sdcard/DCIM/100RICOH/R0010123.JPG

Log into your AVD with:

    adb shell
    cd /sdcard/DCIM/100RICOH
    
Check to see that the image is installed in the proper directory.

![photo file](images/image_file.png)



## App Permissions
In Android Virtual Device, select Setting -> Apps to
enable storage permissions.

![app settings](images/app-setting.png)

Select app _No Camera Demo_

![no camera](images/no_camera.png)

Select permissions.

![permissions](images/permissions.png)

Enable _Storage_ permissions.

![storage permission](images/storage_permission.png)


## Test Image Processing

The RICOH THETA V has no screen. Your processing will be 
triggered by the shutter button or a button on the side
of the camera. To simulate the experience of pressing a camera
button, you can set up a button in your AVD.

![process button](images/process_button.png)

You will see processed image file in your AVD.

![processed file](images/processed_file.png)

Download the file to your local computer with `adb pull`.

![adb pull](images/adb_pull.png)

You can now view and analyze the file on your local computer.

![view sample image](images/image_view.png)

exiftool is useful for inspecting metadata.

![exiftool](images/exiftool.png)

## Caution About Metadata

The metadata is stripped out of this example. In particular, the
ProjectionType is not set to equirectangular.  You can add the metadata
in with exiftool for testing.

![projectiontype](images/projection_type.png)

Once you have the ProjectionType set to equirectangular, the
image will be viewable in 360 apps such as Facebook. Note that the
image resolution is intentionally low to reduce file size
for transmission over unstable cellular networks in remote areas.

![facebook](images/facebook.png)

## Finishing RICOH THETA Plug-in

If you're working on a submission for the [2019 Dream and Build 
Contest](https://community.theta360.guide/t/dream-and-build-developers-contest-now-taking-entries/4205?u=codetricity)
and do not have a RICOH THETA camera,
Oppkey may be able to help you add the functionality needed to
complete your plug-in on actual RICOH THETA hardware.
If you need help, please post in the forum at the link above.



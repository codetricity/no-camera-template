package guide.theta360.nocamera;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Button takePictureButton;
    ImageView thetaImageView;
    // on the RICOH THETA V, there is no function button. People often use the
    // wifi button on the side of the camera to process images or change settings
    Button processButton;
    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
    String basepath = extStorageDirectory + "/DCIM/100RICOH/";

    String picturePath;
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    int imageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePictureButton = findViewById(R.id.takePictueButtonId);
        processButton = findViewById(R.id.processButtonId);
        thetaImageView = findViewById(R.id.thetaImageId);
        thetaImageView.setImageResource(R.drawable.theta);

        File thetaMediaDir = new File(basepath);
        if (!thetaMediaDir.exists()) {
            thetaMediaDir.mkdirs();
        }

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picturePath = takeThetaPicture();
                Toast.makeText(MainActivity.this, "Picture saved to THETA SD card " +
                        picturePath, Toast.LENGTH_LONG).show();
                Log.d("THETADEBUG", "received image path " + picturePath);

                /**
                 * Call your image processing or file transfer method here or
                 * trigger it with a button press.
                 * If you want to process your image when the picture is taken,
                 * uncomment the line below.
                 */
                 // processImage(picturePath);


            }
        });

        processButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This section is only if you want to trigger your image
             * processing or file transfer when a button is pressed
             * on the camera.  If you start the image process when the
             * picture is taken, you can delete the entire method.
             * @param v
             */
            @Override
            public void onClick(View v) {
                processImage(picturePath);
            }
        });
    }

    private void processImage(String thetaPicturePath) {
        /**
         * Put your code here to process the image.
         * The RICOH THETA saves the images to /sdcard/DCIM/100RICOH
         * An example path:
         *  /storage/emulated/0/DCIM/100RICOH/R0010123.JPG
         *
         * Once you process your image, you should save it to the
         * same location with a different file name.
         *
         */

        Log.d("THETADEBUG", "Processing image " + thetaPicturePath);

        /**
         * Sample processing code below
         */

        File myExternalFile = new File(basepath + "PROCESSED_IMAGE.PNG");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = getBitmap(thetaPicturePath);

        // bitmap.compress should be put on different thread
        executor.submit(() -> {
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
            try {
                FileOutputStream fos = new FileOutputStream(myExternalFile);
                fos.write(byteArrayOutputStream.toByteArray());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public String takeThetaPicture() {

        InputStream in = null;
        OutputStream out = null;
        String thetaImagePath = null;
        AssetManager assetManager = getResources().getAssets();

        String[] thetaImageFiles = null;

        try {
            thetaImageFiles =  assetManager.list("100RICOH");
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            if (imageNumber >= thetaImageFiles.length) {
                imageNumber = 0;
                Log.d("THETADEBUG", "Set Image Number to Zero");
            }

            // copy file
            in = assetManager.open("100RICOH/" + thetaImageFiles[imageNumber]);
            out = new FileOutputStream(basepath  + thetaImageFiles[imageNumber]);
            copyFile(in, out);

            in.close();
            in = null;
            out.flush();
            out.close();
            out= null;
            Log.d("THETADEBUG", "copied file " + thetaImageFiles[imageNumber]);

            InputStream inputStream = assetManager.open("100RICOH/" + thetaImageFiles[imageNumber]);
            Drawable d = Drawable.createFromStream(inputStream, null);
            thetaImageView.setImageDrawable(d);
            inputStream.close();
            inputStream = null;
            thetaImagePath = basepath + thetaImageFiles[imageNumber];

            // increment image number last
            imageNumber = imageNumber + 1;

        } catch (IOException e) {
            e.printStackTrace();
        }




        return thetaImagePath;


    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    private Bitmap getBitmap(String photoPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Log.d("THETADEBUG", photoPath);
        Bitmap imgTheta = BitmapFactory.decodeFile(photoPath, options);
        ByteBuffer byteBufferTheta = ByteBuffer.allocate(imgTheta.getByteCount());
        imgTheta.copyPixelsToBuffer(byteBufferTheta);
        Bitmap bmpTheta = Bitmap.createScaledBitmap(imgTheta, 400, 200, true);
        return bmpTheta;
    }
}

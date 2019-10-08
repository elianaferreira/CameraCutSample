package py.eferreira.camerasampleapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.camerakit.CameraKitView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class CameraActivity extends AppCompatActivity {

    public static final String CAPTURED_IMAGE = "CAPTURED_IMAGE";
    public static final String FILE_NAME = "photo.jpg";

    CameraKitView cameraKitView;
    Button btnCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraKitView = findViewById(R.id.camera);
        btnCapture = findViewById(R.id.btn_capture);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] capturedImage) {
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
//                        Bitmap croppedBitmap = cropBitmapCenter(bitmap, bitmap.getWidth(), cameraKitView.getHeight());
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                        byte[] byteArray = stream.toByteArray();

                        saveFile(capturedImage);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private Bitmap cropBitmapCenter(Bitmap bitmap, int cropWidth, int cropHeight){
        int bitmapWidth     = bitmap.getWidth();
        int bitmapheight    = bitmap.getHeight();

        // make sure crop isn't larger than bitmap size
        cropWidth   = (cropWidth > bitmapWidth) ? bitmapWidth : cropWidth;
        cropHeight  = (cropHeight > bitmapheight) ? bitmapheight : cropHeight;

        int newX = bitmapWidth/2  - cropWidth/2;
        int newY = bitmapheight/2 - cropHeight/2;

        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap,newX, newY, cropWidth, cropHeight);
        return croppedBitmap;
    }


    private void saveFile(byte[] capturedImage) {
        File savedPhoto = new File(getFilesDir(), FILE_NAME);
        try {
            FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
            outputStream.write(capturedImage);
            outputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

}

package py.eferreira.camerasampleapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.log.LoggersKt;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.result.BitmapPhoto;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.result.WhenDoneListener;
import io.fotoapparat.selector.FlashSelectorsKt;
import io.fotoapparat.selector.FocusModeSelectorsKt;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.selector.SelectorsKt;
import io.fotoapparat.view.CameraView;


public class CameraActivity extends AppCompatActivity {

    public static final String CAPTURED_IMAGE = "CAPTURED_IMAGE";
    public static final String FILE_NAME = "photo.jpg";

    CameraView cameraView;
    Button btnCapture;

    Fotoapparat fotoapparat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraView = findViewById(R.id.camera_view);
        btnCapture = findViewById(R.id.btn_capture);



        fotoapparat =  Fotoapparat
                .with(this)
                .into(cameraView)           // view which will draw the camera preview
                .previewScaleType(ScaleType.CenterCrop)  // we want the preview to fill the view
                .photoResolution(ResolutionSelectorsKt.highestResolution())   // we want to have the biggest photo possible
                .lensPosition(LensPositionSelectorsKt.back())       // we want back camera
                .focusMode(SelectorsKt.firstAvailable(  // (optional) use the first focus mode which is supported by device
                        FocusModeSelectorsKt. continuousFocusPicture(),
                        FocusModeSelectorsKt.autoFocus(),        // in case if continuous focus is not available on device, auto focus will be used
                        FocusModeSelectorsKt.fixed()             // if even auto focus is not available - fixed focus mode will be used
                ))
                .flash(SelectorsKt.firstAvailable(      // (optional) similar to how it is done for focus mode, this time for flash
                        FlashSelectorsKt.autoRedEye(),
                        FlashSelectorsKt.autoFlash(),
                        FlashSelectorsKt.torch()
                ))
//                .frameProcessor(myFrameProcessor)   // (optional) receives each frame from preview stream
                .logger(LoggersKt.loggers(            // (optional) we want to log camera events in 2 places at once
                        LoggersKt.logcat(),           // ... in logcat
                        LoggersKt.fileLogger(this)    // ... and to file
                ))
                .build();

        //TODO manejar el permiso
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
//                    @Override
//                    public void onImage(CameraKitView cameraKitView, byte[] capturedImage) {
////                        Bitmap bitmap = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
////                        Bitmap croppedBitmap = cropBitmapCenter(bitmap, bitmap.getWidth(), cameraKitView.getHeight());
////                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
////                        croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
////                        byte[] byteArray = stream.toByteArray();
//
//                        saveFile(capturedImage);
//                        setResult(RESULT_OK);
//                        finish();
//                    }
//                });
                PhotoResult photoResult = fotoapparat.takePicture();
                File savedPhoto = new File(getFilesDir(), FILE_NAME);
                photoResult.saveToFile(savedPhoto);
                setResult(RESULT_OK);
                        finish();


//                photoResult.toBitmap().whenDone(new WhenDoneListener<BitmapPhoto>() {
//                    @Override
//                    public void whenDone(BitmapPhoto bitmapPhoto) {
//
//                    }
//                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fotoapparat.start();
    }


    @Override
    protected void onStop() {
        super.onStop();
        fotoapparat.stop();
    }




//    private Bitmap cropBitmapCenter(Bitmap bitmap, int cropWidth, int cropHeight){
//        int bitmapWidth     = bitmap.getWidth();
//        int bitmapheight    = bitmap.getHeight();
//
//        // make sure crop isn't larger than bitmap size
//        cropWidth   = (cropWidth > bitmapWidth) ? bitmapWidth : cropWidth;
//        cropHeight  = (cropHeight > bitmapheight) ? bitmapheight : cropHeight;
//
//        int newX = bitmapWidth/2  - cropWidth/2;
//        int newY = bitmapheight/2 - cropHeight/2;
//
//        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap,newX, newY, cropWidth, cropHeight);
//        return croppedBitmap;
//    }
//
//
//    private void saveFile(byte[] capturedImage) {
//        File savedPhoto = new File(getFilesDir(), FILE_NAME);
//        try {
//            FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
//            outputStream.write(capturedImage);
//            outputStream.close();
//        } catch (java.io.IOException e) {
//            e.printStackTrace();
//        }
//    }

}

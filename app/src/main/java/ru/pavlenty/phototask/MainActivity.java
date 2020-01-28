package ru.pavlenty.phototask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn_capture;
    private ImageView imageView;
    private TextView txt_help;
    private String imageSavePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // todo проверка наличии камеры
        btn_capture = findViewById(R.id.capture);
        imageView = findViewById(R.id.imageView);
        txt_help = findViewById(R.id.textView);
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CameraUtils.checkPermissions(getApplicationContext())) {
                    // фотографируем
                    captureImage();
                } else {
                    // даем права
                    requestCameraPermission();
                }
            }
        });
    }
    private void requestCameraPermission() {
        Log.e("EEEEEEEEE","123");
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(
                new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Toast.makeText(getApplicationContext(),"All granted",Toast.LENGTH_LONG).show();
                        } else {
                            // todo Toast - нет прав
                            Toast.makeText(getApplicationContext(),"Access denied",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }
        ).check();
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = CameraUtils.getOutputMediaFile();

        if(file != null) {
            imageSavePath = file.getAbsolutePath();
        }
        // передаем ссылку на файл
        Uri fileuri = CameraUtils.getOutputMediaUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
        startActivityForResult(intent,1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                previewImage();
            }
        }
    }

    private void previewImage()  {
        txt_help.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        Bitmap bitmap = BitmapFactory.decodeFile(imageSavePath);
        imageView.setImageBitmap(bitmap);

    }
}

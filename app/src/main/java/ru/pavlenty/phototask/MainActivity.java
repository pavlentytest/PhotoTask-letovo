package ru.pavlenty.phototask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;



import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private Button btn_capture, btn_send;
    private ImageView imageView;
    private TextView txt_help;
    private String imageSavePath;
    private Bitmap bitmap;
    private Uri fileuri;

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
        btn_send = findViewById(R.id.send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // uploadFile();
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
        fileuri = CameraUtils.getOutputMediaUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
        startActivityForResult(intent,1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                CameraUtils.refreshGallery(getApplicationContext(),imageSavePath);
                previewImage();
            }
        }
    }

    private void previewImage()  {
        txt_help.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        bitmap = BitmapFactory.decodeFile(imageSavePath);
        imageView.setImageBitmap(bitmap);

    }



}

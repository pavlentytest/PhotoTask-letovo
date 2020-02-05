package ru.pavlenty.phototask;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

public class CameraUtils {

    public static void refreshGallery(Context context, String filePath) {
        MediaScannerConnection.scanFile(context,
                new String[]{ filePath },
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {

                    }
                });
    }

    public static boolean checkPermissions(Context c) {
        return ActivityCompat.checkSelfPermission(c, Manifest.permission.CAMERA )
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(c,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static File getOutputMediaFile() {
        File mediadir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"PhotoTask");
        if(!mediadir.exists()) {
            if(!mediadir.mkdirs()) {
                Log.e("RRRRRR","Error creating dirs!");
                return null;
            }
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
        File mf;
        mf = new File(mediadir.getPath() + "/" + "Image_"+timestamp +".jpg");
        return mf;
    }

    public static Uri getOutputMediaUri(Context c, File f) {
        return FileProvider.getUriForFile(c,c.getPackageName()+".provider",f);
    }

}

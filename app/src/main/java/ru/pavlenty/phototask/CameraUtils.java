package ru.pavlenty.phototask;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
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

    public static boolean checkPermissions(Context c) {
        return ActivityCompat.checkSelfPermission(c, Manifest.permission.CAMERA )
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(c,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }


}

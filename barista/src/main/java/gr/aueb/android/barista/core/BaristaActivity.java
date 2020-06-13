package gr.aueb.android.barista.core;

import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.lang.reflect.Method;

import gr.aueb.android.barista.BuildConfig;

public class BaristaActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                2);
    }

    @Override
    protected void onPause() {
        if(BuildConfig.DEBUG)
        {
            try {
                Class<?> emmaRTClass = Class.forName("com.vladium.emma.rt.RT");
                Method dumpCoverageMethod = emmaRTClass.getMethod("dumpCoverageData", File.class, boolean.class, boolean.class);
                dumpCoverageMethod.invoke(null, new File("sdcard/coverage.exec"), true, false);
            } catch (Exception e) {}
        }
        super.onPause();
    }
}
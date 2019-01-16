package gr.aueb.android.barista.core;


import android.app.Instrumentation;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnitRunner;
import android.util.Log;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Random;

import gr.aueb.android.barista.BuildConfig;
import gr.aueb.android.barista.core.annotations.SaySomething;
import gr.aueb.android.barista.core.annotations.ScreenSize;
import gr.aueb.android.barista.core.http_client.BaristaHttpClient;
import timber.log.Timber;

import static android.util.Log.INFO;


public class BaristaRunListener extends RunListener {


    public static final int UID = new Random().nextInt(1000);

    // empty constructor
    public BaristaRunListener(){
        Timber.plant(new Timber.DebugTree());
    }

    /**
     *   Called when an atomic test is about to be started.
     *   todo think about smart reseting device properties after execution
     *         for example if only size attributes are affected , reset only screen size after execution
     *         Maybe cache the tests
     * @param description
     */
    public void testStarted(Description description){
        TestRunnerMonitor.testStarted();
        Timber.d("Starting test: "+description.getClassName()+":"+description.getMethodName());
        // logDescription(description);
        BaristaHttpClient httpClient = BaristaHttpClient.getInstance();

        Annotation screenAnnotaion = description.getAnnotation(ScreenSize.class);
        if(screenAnnotaion != null ) {

            String height = ((ScreenSize) screenAnnotaion).height();
            String width = ((ScreenSize) screenAnnotaion).width();
            Timber.d("Resizing screen to: "+width+"x"+height);
            httpClient.resizeScreen(width,height);

        }
        else{
            Timber.d("No Barista annotations provided");
        }
    }

    /**
     * Called before any tests have been run.
     * @param description
     */
    public void testRunStarted(Description description){
        TestRunnerMonitor.testRunStarted();

    }

    private void logDescription(Description description){
        Timber.d("Message from custom RunListener: %s",description.getDisplayName());
        Timber.d("Method Running: %s", description.getMethodName());
        Timber.d("Class Name: %s",description.getClassName());
        Timber.d("ANNOTATIONS FOUND");
    }

    public void testRunFinished(Result result){
        TestRunnerMonitor.testRunFinished();
        BaristaHttpClient httpClient = BaristaHttpClient.getInstance();
        httpClient.killServer();

    }

    public void testFinished(Description description) {
        TestRunnerMonitor.testFinished();
        Timber.d("Test "+description.getClassName()+":"+description.getMethodName()+" finished. Reseting Device");
        BaristaHttpClient httpClient = BaristaHttpClient.getInstance();
        httpClient.resetScreen();
    }


}

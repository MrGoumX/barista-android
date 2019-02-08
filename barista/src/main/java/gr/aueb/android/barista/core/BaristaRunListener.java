package gr.aueb.android.barista.core;


import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import java.util.List;

import gr.aueb.android.barista.core.annotations.BaristaAnotationParser;
import gr.aueb.android.barista.core.http_client.BaristaClient;
import gr.aueb.android.barista.core.http_client.DefaultBaristaRetrofitClient;
import gr.aueb.android.barista.core.model.CommandDTO;
import gr.aueb.android.barista.core.utilities.BaristaConfigurationReader;
import gr.aueb.android.barista.core.utilities.DefaultBaristaConfigurationReader;
import retrofit2.converter.jackson.JacksonConverterFactory;
import timber.log.Timber;

public class BaristaRunListener extends RunListener {


    private static final String BASE_URL = "http://10.0.2.2";
    private BaristaClient httpClient;
    private String sessionToken = null;
    public BaristaRunListener(){
        Timber.plant(new Timber.DebugTree());
        sessionToken = DefaultBaristaConfigurationReader.getEmulatorSessionToken();
        //fixme find port number with DefaultBaristaConfigurationReader method.
        // If use DefaultBaristaConfigurationReader No Instrumentation registry found error will occur.
        httpClient = new DefaultBaristaRetrofitClient(BASE_URL,
                8070,
                JacksonConverterFactory.create());
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

        List<CommandDTO> currentCommands = BaristaAnotationParser.getParsedCommands(description,sessionToken);
        if(currentCommands.size() == 1){
            httpClient.executeCommand(currentCommands.get(0));
        }
        else{
            Timber.d("Total commands to execute: "+currentCommands.size());
            httpClient.executeAllCommands(currentCommands);
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
        httpClient.killServer();
    }

    public void testFinished(Description description) {
        TestRunnerMonitor.testFinished();
        Timber.d("Test "+description.getClassName()+":"+description.getMethodName()+" finished. Reseting Device");
        //DefaultBaristaRetrofitClient httpClient = DefaultBaristaRetrofitClient.getHttpClient(BASE_URL);
        //todo reset device
        //httpClient.resetScreen();
    }


}


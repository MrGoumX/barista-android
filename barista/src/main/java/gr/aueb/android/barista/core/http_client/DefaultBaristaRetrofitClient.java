package gr.aueb.android.barista.core.http_client;


import java.io.IOException;
import java.util.List;
import gr.aueb.android.barista.BuildConfig;
import gr.aueb.android.barista.core.model.CommandDTO;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import timber.log.Timber;


public class DefaultBaristaRetrofitClient extends BaristaRetrofitClient{

    private final int DEFAULT_PORT = 8040;
    private Retrofit retrofit;

    /**
     * Construct an HTTP client in order to perform REST CALLS to the Barista Server
     *
     */
    public DefaultBaristaRetrofitClient(String BaseURL, int port, Converter.Factory converter){
       super(BaseURL,port,converter);


    }

    //todo if server has stoped normally by  the gradle plugin. timeout exception will occur
    public void killServer(){

        StatusService service = getRequestClient().create(StatusService.class);
        Call<String> callSync = service.killServer();
        Timber.d("REST CALL URI: %s",callSync.request().url().toString());

        try {
            Response<String> response = callSync.execute();
            // debug only
            //printResponse(response);
            String returnedMessage = response.body();

        } catch (IOException e) {
            Timber.d("Exception Oqured. "+e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void executeCommand(CommandDTO cmd) {

        Timber.d("Calling: "+cmd.toString()+" for "+cmd.getSessionToken());
        BaristaPluginService service = getRequestClient().create(BaristaPluginService.class);
        Call<ResponseBody> callSync = service.executeCommand(cmd);
        Timber.d("Sending request: "+callSync.request().toString());

        callSync.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Timber.d("Sucessfull Call: "+response.code()+" - "+response.message()+" - "+response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Timber.d("Failed Call: "+t.getMessage());
            }

        });
    }

    @Override
    public void executeAllCommands(List<CommandDTO> commands) {

        Timber.d("Calling list of commands");
        BaristaPluginService service = getRequestClient().create(BaristaPluginService.class);
        Call<ResponseBody> callSync = service.executeCommand(commands);
        Timber.d("Sending request: "+callSync.request().toString());

        callSync.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Timber.d("Sucessfull Call: "+response.code()+" - "+response.message()+" - "+response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Timber.d("Failed Call: "+t.getMessage());
            }

        });

    }

    /**
     *
     * @return
     */
    private Retrofit getRequestClient(){
        if(retrofit == null){
            this.create();
        }

        return retrofit;
    }

    /**
     *  Initializes the retrofit client instance to be used for the REST calls.
     *  By default it uses the Jackson Converter factory.
     */
    private void create(){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor(logging);
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(URI)
                .addConverterFactory( JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }


//    public SizeDto getActuallSize(){
//
//        try {
//            StatusService service = getRequestClient().create(StatusService.class);
//            Call<SizeDto> callSync = service.getActualSize(this.token);
//            Response<SizeDto> resultSize = callSync.execute();
//            System.out.println(resultSize.body().getHeight());
//            return resultSize.body();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}

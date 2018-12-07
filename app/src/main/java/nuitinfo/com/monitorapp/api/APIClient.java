package nuitinfo.com.monitorapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    //Build Retrofit HTTP Client
    public static Retrofit getClient() {

        retrofit = new Retrofit.Builder()

                .baseUrl("http://51.75.94.42/")
                //Gson added to Retrofit
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        return retrofit;
    }
}

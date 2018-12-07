package nuitinfo.com.monitorapp.api;

import nuitinfo.com.monitorapp.model.Note;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {
        @FormUrlEncoded
        @POST("cognitif")
        Call<String> getMentalState(@Field("timestamp") int timestamp,
                           @Field("duration") int duration,
                           @Field("nbTouch") int nbTouch ,
                           @Field("nbEchec") int nbEchec);




}

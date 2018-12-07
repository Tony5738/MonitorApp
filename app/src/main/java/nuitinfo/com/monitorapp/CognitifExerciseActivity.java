package nuitinfo.com.monitorapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.sql.Timestamp;

import nuitinfo.com.monitorapp.api.APIClient;
import nuitinfo.com.monitorapp.api.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CognitifExerciseActivity extends AppCompatActivity {

    private Button startButton;
    private TextView textView;
    private Boolean inTime = false;
    private ViewGroup layout;
    private int nbTouch = 0;
    private int duration = 30000;
    private int countdownInterval = 1000;
    private APIInterface apiInterface;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cognitif_exercise);

        //RETROFIT INTERFACE INSTANTIATION
        apiInterface = APIClient.getClient().create(APIInterface.class);

        layout = findViewById(R.id.layout);
        textView = findViewById(R.id.countdown_txt_view);
        startButton = findViewById(R.id.start_button);

        //Game launcher
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                layout.removeView(startButton);
                textView.setVisibility(View.VISIBLE);

                createRectangle();
                new CountDownTimer(duration, countdownInterval) {

                    public void onTick(long millisUntilFinished) {
                        textView.setText("Secondes restantes: " + millisUntilFinished / 1000);
                        inTime = true;

                    }

                    public void onFinish() {
                        textView.setText("Terminé");
                        inTime = false;
                        getMentalState((int)new Timestamp(System.currentTimeMillis()).getTime(), duration, nbTouch, 0);
                    }
                }.start();


            }
        });
    }


    public void createRectangle()
    {

        int width = 100;
        int height = 100;

        int parentHeight = layout.getHeight();
        int parentWidth = layout.getWidth();
        double randomTop = Math.random();
        double randomLeft = Math.random();
        ImageView imageView = new ImageView(CognitifExerciseActivity.this);
        imageView.setBackgroundColor(Color.BLACK);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(v);
                if(inTime){


                    createRectangle();
                    nbTouch++;

                }
            }
        });


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.leftMargin = (int)(randomLeft*(parentWidth-width));
        params.topMargin = (int)(randomTop*(parentHeight-height));
        layout.addView(imageView, params);


    }




    //SEND REQUEST TO WEBSERVICE TO KNOW MENTAL STATE
    public void getMentalState(int timestamp, int duration, int nbTouch, int nbEchec)
    {
        Call call = apiInterface.getMentalState(timestamp, duration, nbTouch, nbEchec);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                Log.d("HTTP_CODE",response.code()+"");

                if(response.code() == 200)
                {

                    //Get the response
                    String mentalState = (String)response.body();
                    float mentalStateInt = Float.valueOf(mentalState);
                    String message = null;
                    if(mentalStateInt <5)
                    {
                        message = "Nous envoyons des secours !!!";
                    }
                    else if(mentalStateInt <10 )
                    {
                        message = "Soyez prudent, votre état se dégrade.";
                    }
                    else
                    {
                        message = "Ok, tout va bien, bonne mission.";
                    }

                    Log.e("mentalState", String.valueOf(mentalState));
                    Toast.makeText(CognitifExerciseActivity.this, message, Toast.LENGTH_LONG).show();



                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();

                if (t instanceof Exception)
                {
                    t.printStackTrace();
                    Toast.makeText(CognitifExerciseActivity.this,"Une erreur est survenue",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

}

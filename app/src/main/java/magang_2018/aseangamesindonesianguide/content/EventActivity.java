package magang_2018.aseangamesindonesianguide.content;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.adapter.EventAdapter;
import magang_2018.aseangamesindonesianguide.api.RequestEventInterface;
import magang_2018.aseangamesindonesianguide.model.Event;
import magang_2018.aseangamesindonesianguide.response.JSONResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Event> data;
    private EventAdapter adapter;
    public static final String TAG = "CalendarActivity";
    private CalendarView mCalendarView;
    private TextView tvEventName,tvDateSchedule,tvEventDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar toolbar = findViewById(R.id.toolbar_event_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViews();
    }

    private void initViews(){

        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                String date = year + "0" + (month + 1) + "" + day;
                Log.d(TAG, "onSelectedDayChange: yyyy/mm/dd: " + date);
                loadJSON(date);



            }
        });


    }
    private void loadJSON(String date){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://13.250.3.168/agig/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestEventInterface request = retrofit.create(RequestEventInterface.class);
       // RequestEventInterface request2 = retrofit.create(RequestEventInterface.class);

        //Call<JSONResponse> call = request.getJSON();
        Call<JSONResponse> call = request.getJSON2(date);

        call.enqueue(new Callback<JSONResponse>() {

            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                data = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
                adapter = new EventAdapter(data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });

//        call2.enqueue(new Callback<JSONResponse>() {
//
//            @Override
//            public void onResponse(Call<JSONResponse> call2, retrofit2.Response<JSONResponse> response) {
//
//                JSONResponse jsonResponse = response.body();
//                data = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
//                adapter = new EventAdapter(data);
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onFailure(Call<JSONResponse> call, Throwable t) {
//                Log.d("Error",t.getMessage());
//            }
//        });
    }
}

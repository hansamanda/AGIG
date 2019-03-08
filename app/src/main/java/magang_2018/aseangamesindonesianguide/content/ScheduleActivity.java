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
import magang_2018.aseangamesindonesianguide.adapter.ScheduleAdapter;
import magang_2018.aseangamesindonesianguide.api.RequestEventInterface;
import magang_2018.aseangamesindonesianguide.api.RequestScheduleInterface;
import magang_2018.aseangamesindonesianguide.model.Event;
import magang_2018.aseangamesindonesianguide.model.Schedule;
import magang_2018.aseangamesindonesianguide.response2.JSONResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Schedule> data;
    private ScheduleAdapter adapter;
    public static final String TAG = "CalendarActivity";
    private CalendarView mCalendarView;
    private TextView tvSport, Date, tvNameSchedule, ScheduleDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar toolbar = findViewById(R.id.toolbar_schedule_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViews();
    }

    private void initViews() {

        recyclerView = (RecyclerView) findViewById(R.id.rvSchedule);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @SuppressLint("ResourceType")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                String date = year + "0" + (month + 1) + "" + day;
                Log.d(TAG, "onSelectedDayChange: mm/dd/yyyy: " + date);
                loadJSON(date);
            }
        });


    }

    private void loadJSON(String date) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://13.250.3.168/agig/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestScheduleInterface request = retrofit.create(RequestScheduleInterface.class);
       // RequestScheduleInterface request2 = retrofit.create(RequestScheduleInterface.class);

        Call<JSONResponse> call = request.getJSON(date);

        call.enqueue(new Callback<JSONResponse>() {

            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                data = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
                adapter = new ScheduleAdapter(data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
}

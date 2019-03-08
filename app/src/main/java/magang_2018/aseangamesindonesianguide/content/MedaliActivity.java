package magang_2018.aseangamesindonesianguide.content;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.adapter.MedaliAdapter;
import magang_2018.aseangamesindonesianguide.api.RequestInterface;
import magang_2018.aseangamesindonesianguide.model.Medali;
import magang_2018.aseangamesindonesianguide.network.JSONResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MedaliActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Medali> data;
    private MedaliAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medali);

        Toolbar toolbar = findViewById(R.id.toolbar_medali_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Medali");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViews();
    }
    private void initViews(){
        recyclerView = findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJSON();
    }
    private void loadJSON(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://13.250.3.168/agig/")
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSON();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                data = new ArrayList<>( Arrays.asList(jsonResponse.getData()));
                adapter = new MedaliAdapter(data, MedaliActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }
}

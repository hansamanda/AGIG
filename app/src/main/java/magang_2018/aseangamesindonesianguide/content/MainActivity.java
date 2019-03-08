package magang_2018.aseangamesindonesianguide.content;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.adapter.MedaliAdapter;
import magang_2018.aseangamesindonesianguide.adapter.NewsAdapter;
import magang_2018.aseangamesindonesianguide.adapter.SlidingImage_Adapter;
import magang_2018.aseangamesindonesianguide.api.RequestInterface;
import magang_2018.aseangamesindonesianguide.auth.LoginActivity;
import magang_2018.aseangamesindonesianguide.customfonts.TextView_Roboto_Regular;
import magang_2018.aseangamesindonesianguide.model.ImageModel;
import magang_2018.aseangamesindonesianguide.model.Medali;
import magang_2018.aseangamesindonesianguide.model.News;
import magang_2018.aseangamesindonesianguide.network.JSONResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NewsAdapter.OnItemClickListener{

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_TITLE = "titlePost";
    public static final String EXTRA_TIME = "timePost";
    public static final String EXTRA_DETAIL ="detail";
    public static final String EXTRA_SOURCE ="source";

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView_Roboto_Regular moreDetail, moreMedal;

    private FirebaseAuth auth;
    private String currentUserId;

    private RecyclerView list_news;
    private NewsAdapter newsAdapter;
    private ArrayList<News> listNews;
    private RequestQueue requestQueue;

    private RecyclerView recyclerView;
    private ArrayList<Medali> data;
    private MedaliAdapter adapter;

    String url = "http://13.250.3.168/agig/Api/News";
    String urlMedal = "http://13.250.3.168/agig/";

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;

    private int[] myImageList = new int[]{R.drawable.image1, R.drawable.image2,
            R.drawable.image3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            currentUserId = auth.getCurrentUser().toString();
        }

        Toolbar toolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        drawerLayout = findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navigation_view);

        moreDetail = findViewById(R.id.more_news);
        moreMedal = findViewById(R.id.more_medal);
        list_news = findViewById(R.id.list_view_news);
        list_news.setHasFixedSize(true);
        list_news.setLayoutManager(new LinearLayoutManager(this));

        listNews = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);


        parseJSON();

        if (auth.getCurrentUser() != null){
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
        }else {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                userMenuSelector(item);
                return false;
            }
        });

        moreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToNewsActivity();
            }
        });
        moreMedal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToMedaliActivity();
            }
        });

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        init();
        initViews();

    }

    private void initViews() {
        recyclerView = findViewById(R.id.list_view_medal);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJSON();
    }

    private void loadJSON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlMedal)
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSON5();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                data = new ArrayList<>( Arrays.asList(jsonResponse.getData()));
                adapter = new MedaliAdapter(data, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    private void init() {
        mPager = findViewById(R.id.vpSlider);
        mPager.setAdapter(new SlidingImage_Adapter(MainActivity.this,imageModelArrayList));

        CirclePageIndicator indicator = findViewById(R.id.sliderDots);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private ArrayList<ImageModel> populateList() {
        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }


    private void parseJSON() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int i = 0 ; i < jsonArray.length(); i++){
                                JSONObject hit = jsonArray.getJSONObject(i);


                                    String title = hit.getString("title");
                                    String time = hit.getString("time");
                                    String imageUrl = hit.getString("image");
                                    String body = hit.getString("body");
                                    String source = hit.getString("source");

                                    listNews.add(new News(title, time, imageUrl, body, source));

                            }

                            newsAdapter = new NewsAdapter(MainActivity.this, listNews);
                            list_news.setAdapter(newsAdapter);
                            newsAdapter.setOnItemClickListener(MainActivity.this);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = error.getMessage();
                Log.i("Error : ", message);
            }
        });

        requestQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void userMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
                break;

            case R.id.nav_news:
                sendUserToNewsActivity();
                break;

            case R.id.nav_medali:
                sendUserToMedaliActivity();
                break;

            case R.id.nav_event:
                sendUserToEventActivity();
                break;

            case R.id.nav_athletes:
                sendUserToAtlitActivity();
                break;

            case R.id.nav_sport:
                sendUserToCaborActivity();
                break;

            case R.id.nav_destination:
                sendUserToDestinationActivity();
                break;

            case R.id.nav_schedule:
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_forum:
                if (auth.getCurrentUser() != null){
                    sendUserToForumActivity();
                }else {
                    Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_chat:
                if (auth.getCurrentUser() != null){
                    sendUserToMessageActivity();
                }else {
                    Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_login:
                sendUserToLoginActivity();
                break;

            case R.id.nav_logout:
                auth.signOut();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
                break;
        }
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToForumActivity() {
        Intent createThreadIntent = new Intent(MainActivity.this, ForumActivity.class);
        startActivity(createThreadIntent);
    }

    private void sendUserToNewsActivity() {
        Intent newsIntent = new Intent(MainActivity.this, NewsActivity.class);
        startActivity(newsIntent);
    }

    private void sendUserToCaborActivity() {
        Intent newsIntent = new Intent(MainActivity.this, CaborActivity.class);
        startActivity(newsIntent);
    }

    private void sendUserToDestinationActivity() {
        Intent newsIntent = new Intent(MainActivity.this, DestinationActivity.class);
        startActivity(newsIntent);
    }

    private void sendUserToAtlitActivity() {
        Intent newsIntent = new Intent(MainActivity.this, AtlitActivity.class);
        startActivity(newsIntent);
    }

    private void sendUserToMedaliActivity() {
        Intent medaliIntent = new Intent(MainActivity.this, MedaliActivity.class);
        startActivity(medaliIntent);
    }

    private void sendUserToEventActivity() {
        Intent event = new Intent(MainActivity.this, EventActivity.class);
        startActivity( event );
    }

    private void sendUserToMessageActivity() {
        Intent createThreadIntent = new Intent(MainActivity.this, MessageActivity.class);
        startActivity(createThreadIntent);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        News clickedItem = listNews.get(position);

        detailIntent.putExtra(EXTRA_URL, clickedItem.getImage());
        detailIntent.putExtra(EXTRA_TITLE, clickedItem.getTitle());
        detailIntent.putExtra(EXTRA_TIME, clickedItem.getTime());
        detailIntent.putExtra(EXTRA_DETAIL, clickedItem.getBody());
        detailIntent.putExtra(EXTRA_SOURCE, clickedItem.getSource());

        startActivity(detailIntent);
    }
}

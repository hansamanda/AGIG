package magang_2018.aseangamesindonesianguide.content;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.adapter.News1Adapter;
import magang_2018.aseangamesindonesianguide.adapter.NewsAdapter;
import magang_2018.aseangamesindonesianguide.model.News;

public class NewsActivity extends AppCompatActivity implements NewsAdapter.OnItemClickListener, News1Adapter.OnItemClickListener {
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_TITLE = "titlePost";
    public static final String EXTRA_TIME = "timePost";
    public static final String EXTRA_DETAIL ="detail";
    public static final String EXTRA_SOURCE ="source";

    private RecyclerView list_news;
    private RecyclerView list_news_bottom;
    private NewsAdapter newsAdapter;
    private News1Adapter newsAdapter1;
    private ArrayList<News> listNews;
    private ArrayList<News> listNews1;
    private RequestQueue requestQueue;
    private RequestQueue requestQueue1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_news );

        Toolbar toolbar = findViewById(R.id.toolbar_news_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("News");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        list_news = findViewById(R.id.recycler_view);
        list_news.setHasFixedSize(true);
        list_news.setLayoutManager(new LinearLayoutManager(this));

        list_news_bottom = findViewById(R.id.recycler_view_bottom);
        list_news_bottom.setHasFixedSize(true);
        list_news_bottom.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        listNews = new ArrayList<>();

        listNews1 = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);

        requestQueue1 = Volley.newRequestQueue(this);

        parseJSON();

        parseJSON1();
    }

    private void parseJSON() {
        String url = "http://13.250.3.168/agig/Api/News";

        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET, url, null,
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

                            newsAdapter = new NewsAdapter(NewsActivity.this, listNews);
                            list_news.setAdapter(newsAdapter);
                            newsAdapter.setOnItemClickListener(NewsActivity.this);

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

    private void parseJSON1() {
        String url = "http://13.250.3.168/agig/Api/News/AlsoRead";

        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET, url, null,
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

                                listNews1.add(new News(title, time, imageUrl, body, source));

                            }

                            newsAdapter1 = new News1Adapter(NewsActivity.this, listNews1);
                            list_news_bottom.setAdapter(newsAdapter1);
                            newsAdapter1.setOnItemClickListener(NewsActivity.this);

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

        requestQueue1.add(request);
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

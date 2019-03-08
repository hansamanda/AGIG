package magang_2018.aseangamesindonesianguide.content;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.adapter.WisataAdapter;
import magang_2018.aseangamesindonesianguide.model.Wisata;


public class WisataActivity extends AppCompatActivity implements WisataAdapter.OnItemClickListener{
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_NAME= "Name";
    public static final String EXTRA_LAT="Lat";
    public static final String EXTRA_LNG="Lng";
    public static final String EXTRA_ADDRESS="Address";
    public static final String EXTRA_DESCRIPTION="Description";
    public static final String EXTRA_TAGS="Tags";
    public static final String EXTRA_CONTENT="Content";
    public static final String EXTRA_CATEGORY="Category";
    public static final String EXTRA_PHONE="Phone";
    public static final String EXTRA_WEB="Web";
    public static final String EXTRA_SHOWN="Shown";


    private RecyclerView mRecyclerView;
    private WisataAdapter mExampleAdapter;
    private ArrayList<Wisata> mExampleList;
    private com.android.volley.RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_wisata);



//        Toolbar toolbar = findViewById(R.id.toolbar_wisata_activity);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Destination");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mExampleList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();
    }
    private void parseJSON() {
        String url = "http://13.250.3.168/agig/Api/Places";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);


                                String Name = hit.getString("name");
                                String imageUrl = hit.getString("image");
                                String Lat = hit.getString("lat");
                                String Lng = hit.getString("lng");
                                String Address = hit.getString("address");
                                String Description= hit.getString("description");
                                String Tags= hit.getString("tags");
                                String Content= hit.getString("content");
                                String Category= hit.getString("category");
                                String Phone=hit.getString("phone");
                                String Web=hit.getString("web");
                                String Shown=hit.getString("shown");


                                mExampleList.add(new Wisata(imageUrl, Name, Lat, Lng, Address, Description, Tags, Content, Category, Phone, Web, Shown));
                            }

                            mExampleAdapter = new WisataAdapter(WisataActivity.this, mExampleList);
                            mRecyclerView.setAdapter(mExampleAdapter);
                            mExampleAdapter.setOnItemClickListener(WisataActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, WisatasActivity.class);
        Wisata clickedItem = mExampleList.get(position);

        detailIntent.putExtra(EXTRA_URL, clickedItem.getmImageUrl());
        detailIntent.putExtra(EXTRA_NAME, clickedItem.getmName());
        detailIntent.putExtra(EXTRA_LAT, clickedItem.getmLat());
        detailIntent.putExtra(EXTRA_LNG, clickedItem.getmLng());
        detailIntent.putExtra(EXTRA_ADDRESS, clickedItem.getmAddress());
        detailIntent.putExtra(EXTRA_DESCRIPTION, clickedItem.getmDescription());
        detailIntent.putExtra(EXTRA_TAGS, clickedItem.getmTags());
        detailIntent.putExtra(EXTRA_CONTENT, clickedItem.getmCategory());
        detailIntent.putExtra(EXTRA_CATEGORY, clickedItem.getmCategory());
        detailIntent.putExtra(EXTRA_PHONE, clickedItem.getmPhone());
        detailIntent.putExtra(EXTRA_WEB, clickedItem.getmWeb());
        detailIntent.putExtra(EXTRA_SHOWN, clickedItem.getmShown());

        startActivity(detailIntent);
    }
}

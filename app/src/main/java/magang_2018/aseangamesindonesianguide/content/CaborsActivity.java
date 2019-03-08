package magang_2018.aseangamesindonesianguide.content;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import magang_2018.aseangamesindonesianguide.R;

import static magang_2018.aseangamesindonesianguide.content.CaborActivity.EXTRA_CREATOR;
import static magang_2018.aseangamesindonesianguide.content.CaborActivity.EXTRA_LIKES;
import static magang_2018.aseangamesindonesianguide.content.CaborActivity.EXTRA_URL;
public class CaborsActivity extends AppCompatActivity {

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_cabors);
                Toolbar toolbar = findViewById(R.id.toolbar_cabors_activity);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle("Sport Details");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);

                Intent intent = getIntent();
                String imageUrl = intent.getStringExtra(EXTRA_URL);
                String creatorName = intent.getStringExtra(EXTRA_CREATOR);
                String likeCount = intent.getStringExtra(EXTRA_LIKES);

                ImageView imageView = findViewById(R.id.image_view_detail);
                TextView textViewCreator = findViewById(R.id.text_view_creator_detail);
                TextView textViewLikes = findViewById(R.id.text_view_like_detail);

                Picasso.with(this).load(imageUrl).fit().centerInside().into(imageView);
                textViewCreator.setText("Venue:  "+ creatorName);
                textViewLikes.setText("Description: " +"\n"+"\n"+ likeCount);
            }
        }


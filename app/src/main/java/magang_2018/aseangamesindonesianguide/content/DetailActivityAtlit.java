package magang_2018.aseangamesindonesianguide.content;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import magang_2018.aseangamesindonesianguide.R;

import static magang_2018.aseangamesindonesianguide.content.AtlitActivity.EXTRA_CREATOR;
import static magang_2018.aseangamesindonesianguide.content.AtlitActivity.EXTRA_LIKES;
import static magang_2018.aseangamesindonesianguide.content.AtlitActivity.EXTRA_URL;
import static magang_2018.aseangamesindonesianguide.content.AtlitActivity.EXTRA_DESC;

public class DetailActivityAtlit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_atlit);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String creatorName = intent.getStringExtra(EXTRA_CREATOR);
        String likeCount = intent.getStringExtra(EXTRA_LIKES);
        String desAtlit = intent.getStringExtra(EXTRA_DESC);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewCreator = findViewById(R.id.text_view_creator_detail);
        TextView textViewLikes = findViewById(R.id.text_view_like_detail);
        TextView textViewDescription = findViewById(R.id.text_view_description);

        Picasso.with(this).load(imageUrl).fit().centerInside().into(imageView);
        textViewCreator.setText(creatorName);
        textViewLikes.setText("Negara: " + likeCount);
        textViewDescription.setText("Sports:"+desAtlit);

    }
}

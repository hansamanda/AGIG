package magang_2018.aseangamesindonesianguide.content;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import magang_2018.aseangamesindonesianguide.R;

import static magang_2018.aseangamesindonesianguide.content.WisataActivity.EXTRA_ADDRESS;
import static magang_2018.aseangamesindonesianguide.content.WisataActivity.EXTRA_DESCRIPTION;
import static magang_2018.aseangamesindonesianguide.content.WisataActivity.EXTRA_LAT;
import static magang_2018.aseangamesindonesianguide.content.WisataActivity.EXTRA_LNG;
import static magang_2018.aseangamesindonesianguide.content.WisataActivity.EXTRA_NAME;
import static magang_2018.aseangamesindonesianguide.content.WisataActivity.EXTRA_PHONE;
import static magang_2018.aseangamesindonesianguide.content.WisataActivity.EXTRA_TAGS;
import static magang_2018.aseangamesindonesianguide.content.WisataActivity.EXTRA_URL;
import static magang_2018.aseangamesindonesianguide.content.WisataActivity.EXTRA_WEB;


public class WisatasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisatas);

        Button btn = (Button) findViewById(R.id.btnPeta);



//        Toolbar toolbar = findViewById(R.id.toolbar_wisatas_activity);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Destination Details");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String Name = intent.getStringExtra(EXTRA_NAME);
        String Address = intent.getStringExtra(EXTRA_ADDRESS);
        String Description = intent.getStringExtra(EXTRA_DESCRIPTION);
        String Web = intent.getStringExtra(EXTRA_WEB);
        String Phone = intent.getStringExtra(EXTRA_PHONE);
        String Tags = intent.getStringExtra(EXTRA_TAGS);
        final  String Lat = intent.getStringExtra(EXTRA_LAT);
        final String Lng = intent.getStringExtra(EXTRA_LNG);


        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Lat+ "," + Lng+ "");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        ImageView imageView = findViewById( R.id.image_view_detail);
        TextView textViewName = findViewById(R.id.text_view_detail_name);
        TextView textViewAddress = findViewById(R.id.text_view_detail_address);
        TextView textViewDescription = findViewById(R.id.text_view_detail_description);
        TextView textViewWeb = findViewById(R.id.text_view_detail_web);
        TextView textViewPhone = findViewById(R.id.text_view_detail_phone);
        TextView textViewTags = findViewById(R.id.text_view_detail_tags);



        Picasso.with(this).load(imageUrl).fit().centerInside().into(imageView);
        textViewName.setText(Name);
        textViewAddress.setText("Alamat: " +Address);
        textViewDescription.setText("Deskripi:"+"\n"+Description);
        textViewWeb.setText("Website: "+Web);
        textViewPhone.setText("Phone: "+Phone);
        textViewTags.setText("Tag:"+"\n"+Tags);

    }
}

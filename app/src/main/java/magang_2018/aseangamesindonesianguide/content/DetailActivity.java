package magang_2018.aseangamesindonesianguide.content;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import magang_2018.aseangamesindonesianguide.R;

import static magang_2018.aseangamesindonesianguide.content.MainActivity.EXTRA_DETAIL;
import static magang_2018.aseangamesindonesianguide.content.MainActivity.EXTRA_SOURCE;
import static magang_2018.aseangamesindonesianguide.content.MainActivity.EXTRA_TIME;
import static magang_2018.aseangamesindonesianguide.content.MainActivity.EXTRA_TITLE;
import static magang_2018.aseangamesindonesianguide.content.MainActivity.EXTRA_URL;

public class DetailActivity extends AppCompatActivity {

    //Membuat Variable ShareAction Provider
    private android.support.v7.widget.ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail );

        Toolbar toolbar = findViewById(R.id.toolbar_detail_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("News Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String title = intent.getStringExtra(EXTRA_TITLE);
        String time = intent.getStringExtra(EXTRA_TIME);
        String body = intent.getStringExtra(EXTRA_DETAIL);
        String source = intent.getStringExtra(EXTRA_SOURCE);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewCreator = findViewById(R.id.text_view_creator_detail);
        TextView textViewLikes = findViewById(R.id.text_view_detail);
        TextView textViewTanggal = findViewById(R.id.text_view_tanggal);
        TextView textViewSource = findViewById(R.id.text_view_like_source);

        Glide.with(this)
                .load(imageUrl)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        textViewCreator.setText(title);
        textViewLikes.setText(body);
        textViewTanggal.setText(time);
        textViewSource.setText(source);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Menginisialisasi MenuBar yang akan ditampilkan pada ActionBar/Toolbar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_bar, menu);

        //MenuItem yang akan dijadikan ShareActionProvider
        MenuItem item = menu.findItem(R.id.share);

        //Ambil dan Simpan ShareActionProvider
        shareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShare();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                setShare();
            break;
        }
        return true;
    }

    private void setShare(){
        ApplicationInfo appInfo = getApplicationContext().getApplicationInfo();
//        String apkPath = appInfo.sourceDir;
        Intent Share = new Intent();
        Share.setAction(Intent.ACTION_SEND);
        Share.setType("text/plain");
        Share.putExtra(Intent.EXTRA_TEXT, "Text To Share");
//        Share.setType("application/vnd.android.package-archive");
//        Share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkPath)));
        shareActionProvider.setShareIntent(Share);
    }
}

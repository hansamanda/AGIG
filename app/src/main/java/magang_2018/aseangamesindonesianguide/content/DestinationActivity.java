package magang_2018.aseangamesindonesianguide.content;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import magang_2018.aseangamesindonesianguide.R;

public class DestinationActivity extends AppCompatActivity {

    String list_nama[] = {
            "Art",
            "Cinema",
            "Embassy",
            "Kreasi",
            "Kuliner",
            "Publik",
            "Tradisional"
    };

    // daftar gambar
    int list_gambar[] = {
            R.drawable.art,
            R.drawable.cinema,
            R.drawable.embassys,
            R.drawable.kreasi,
            R.drawable.kuliner,
            R.drawable.publik,
            R.drawable.tradisional
    };


    ListView LvDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);


        LvDestination = (ListView) findViewById(R.id.LvDestination);
        // membuat sebuah adapter yang berfungsi untuk menampung data sementara sebelum di tampilkan ke dalam list view
        AdapterDestination adapter = new AdapterDestination(this, list_nama, list_gambar);
        //menampilkan / memasukan adapter ke dalam ListView
        LvDestination.setAdapter(adapter);
        //memberikan event ketika listview diklik
        LvDestination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // inten ke detail.java dengan mengirimkan parameter yang berisi nama dan gambar
                Intent a = new Intent(getApplicationContext(), WisataActivity.class);
                //kirimkan parameter
                a.putExtra("Nama", list_nama[position]);
                a.putExtra("Gambar", list_gambar[position]);

                //kirimkan ke detail.java
                startActivity(a);
            }
        });


    }

    // class di dalam class
    private class AdapterDestination extends ArrayAdapter {
        String list_nama[];
        int list_gambar[];
        Activity activity;

        //konstruktor
        public AdapterDestination(DestinationActivity mainActivity, String[] list_nama, int[] list_gambar) {
            super(mainActivity, R.layout.item_destination, list_nama);
            this.list_gambar = list_gambar;
            activity = mainActivity;
            this.list_nama = list_nama;

        }


        //menthode yang digunakan untuk memanggil layout list_buah dan mengenalkan widgetnya
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // panggil layout list_buah
            LayoutInflater inflater = (LayoutInflater) activity.getLayoutInflater();
            View v = inflater.inflate(R.layout.item_destination, null);
            // kenalkan widget yang ada pada list buah
            ImageView gambar;
            TextView nama;

            //casting widget id
            gambar = (ImageView) v.findViewById(R.id.IvGambarDestination);
            nama = (TextView) v.findViewById(R.id.TxtNamaDestination);

            // set data kedalam image
            gambar.setImageResource(list_gambar[position]);
            nama.setText(list_nama[position]);

            return v;
        }
    }
}




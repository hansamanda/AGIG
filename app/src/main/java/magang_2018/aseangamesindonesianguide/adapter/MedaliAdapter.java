package magang_2018.aseangamesindonesianguide.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.customfonts.TextView_Roboto_Regular;
import magang_2018.aseangamesindonesianguide.model.Medali;

/**
 * Created by Hans on 8/18/2018.
 */

public class MedaliAdapter extends RecyclerView.Adapter<MedaliAdapter.ViewHolder> {
    private ArrayList<Medali> data;
    private Context context;

    public MedaliAdapter(ArrayList<Medali> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public MedaliAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.item_medali, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MedaliAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(data.get(i).getCountry());
        viewHolder.tv_gold.setText(data.get(i).getGold());
        viewHolder.tv_silver.setText(data.get(i).getSilver());
        viewHolder.tv_bronze.setText( data.get( i ).getBronze());
        viewHolder.tv_total.setText(data.get(i).getTotal());

        Glide.with(context)
                .load(data.get(i).getImg())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.image_country);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView_Roboto_Regular tv_name, tv_gold, tv_silver, tv_bronze, tv_total;
        private ImageView image_country;

        public ViewHolder(View view) {
            super(view);

            tv_name = view.findViewById(R.id.text_view_name_country);
            tv_gold = view.findViewById(R.id.text_gold);
            tv_silver = view.findViewById(R.id.text_silver);
            tv_bronze = view.findViewById(R.id.text_bronze);
            tv_total = view.findViewById(R.id.text_total);

            image_country = view.findViewById(R.id.image_country);

        }
    }

}
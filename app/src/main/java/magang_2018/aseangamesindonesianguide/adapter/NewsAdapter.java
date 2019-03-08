package magang_2018.aseangamesindonesianguide.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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
import magang_2018.aseangamesindonesianguide.model.News;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<News> listNews;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public NewsAdapter(Context mContext, ArrayList<News> listNews) {
        this.mContext = mContext;
        this.listNews = listNews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = listNews.get(position);

        holder.text_title.setText(news.getTitle());
        holder.text_date.setText(news.getTime());

        String image = news.getImage();

        if (image.equals("null") || image.equals(null)){
            Glide.with(mContext)
                    .load(R.drawable.logo3)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
        } else{
            Glide.with(mContext)
                    .load(image)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
        }



    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView_Roboto_Regular text_title, text_date;

        private ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);

            text_title = itemView.findViewById(R.id.text_view_title);
            text_date = itemView.findViewById(R.id.text_view_date);
            imageView = itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

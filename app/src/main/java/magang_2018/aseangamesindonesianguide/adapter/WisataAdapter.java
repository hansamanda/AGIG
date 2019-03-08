package magang_2018.aseangamesindonesianguide.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.model.Wisata;

/**
 * Created by Anggiat on 8/20/2018.
 */

public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<Wisata> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public WisataAdapter(Context context, ArrayList<Wisata> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate( R.layout.item_wisata, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Wisata currentItem = mExampleList.get(position);


        String imageUrl = currentItem.getmImageUrl();
        String Name = currentItem.getmName();
//        String Lat = currentItem.getmLat();
//        String Lng = currentItem.getmLng();
//        String Address = currentItem.getmAddress();
//        String Description = currentItem.getmDescription();
//        String Tags = currentItem.getmTags();
//        String Content = currentItem.getmContent();
//        String Category = currentItem.getmCategory();
//        String Phone = currentItem.getmPhone();
//        String Web = currentItem.getmWeb();
//        String Shown = currentItem.getmShown();



        holder.mTextViewName.setText(Name);
//        holder.mTextViewLat.setText(Lat);
//        holder.mTextViewLng.setText(Lng);
//        holder.mTextViewAddress.setText(Address);
//        holder.mTextViewDescription.setText(Description);
//        holder.mTextViewTags .setText(Tags);
//        holder.mTextViewContent .setText(Content);
//        holder.mTextViewCategory .setText(Category);
//        holder.mTextViewPhone .setText(Phone);
//        holder.mTextViewWeb .setText(Web);
//        holder.mTextViewShown .setText(Shown);
        Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewName;
        public TextView mTextViewLat;
        public TextView mTextViewLng;
        public TextView mTextViewDescription;
        public TextView mTextViewAddress;
        public TextView mTextViewTags;
        public TextView mTextViewContent;
        public TextView mTextViewPhone;
        public TextView mTextViewCategory;
        public TextView mTextViewShown;
        public TextView mTextViewWeb;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewName = itemView.findViewById(R.id.text_view_name);
//            mTextViewLat= itemView.findViewById(R.id.text_view_lat);
//            mTextViewLng= itemView.findViewById(R.id.text_view_lng);
//            mTextViewDescription= itemView.findViewById(R.id.text_view_description);
//            mTextViewAddress= itemView.findViewById(R.id.text_view_address);
//            mTextViewTags= itemView.findViewById(R.id.text_view_tags);
//            mTextViewContent= itemView.findViewById(R.id.text_view_content);
//            mTextViewPhone= itemView.findViewById(R.id.text_view_phone);
//            mTextViewCategory= itemView.findViewById(R.id.text_view_category);
//            mTextViewShown= itemView.findViewById(R.id.text_view_shown);
//            mTextViewWeb= itemView.findViewById(R.id.text_view_web);


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



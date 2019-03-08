package magang_2018.aseangamesindonesianguide.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.content.QuestionForumActivity;
import magang_2018.aseangamesindonesianguide.customfonts.TextView_Roboto_Regular;
import magang_2018.aseangamesindonesianguide.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHelper>{

    private List<Category> categories;

    private Context context;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHelper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        return new ViewHelper(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHelper holder, final int position) {
        holder.text_category.setText(categories.get(position).getName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent askIntent = new Intent(context, QuestionForumActivity.class);
                askIntent.putExtra("key_category", categories.get(position).getKey());
                context.startActivity(askIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHelper extends RecyclerView.ViewHolder {

        private TextView_Roboto_Regular text_category;
        private View view;

        public ViewHelper(View itemView) {
            super(itemView);
            view = itemView;
            text_category = itemView.findViewById(R.id.text_view_category);
        }
    }
}

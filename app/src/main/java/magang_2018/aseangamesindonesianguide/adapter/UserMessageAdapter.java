package magang_2018.aseangamesindonesianguide.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.content.ChatActivity;
import magang_2018.aseangamesindonesianguide.customfonts.TextView_Roboto_Regular;
import magang_2018.aseangamesindonesianguide.model.UserMessage;

public class UserMessageAdapter extends RecyclerView.Adapter<UserMessageAdapter.ViewHolder> {

    private List<UserMessage> userMessages;

    private Context context;

    public UserMessageAdapter(Context context) {
        this.context = context;
    }

    public void setUserMessages(List<UserMessage> userMessages) {
        this.userMessages = userMessages;
    }

    private DatabaseReference userRef;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String userId = userMessages.get(position).getUser_key();

        userRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();

                    holder.text_name.setText(name);

                    Glide.with(context)
                            .load(image)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.image_user);


                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence option[] = new CharSequence[]{
                                    "Send Message"
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Select Option");

                            builder.setItems(option, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0){
                                        Intent chatIntent = new Intent(context, ChatActivity.class);
                                        chatIntent.putExtra("key_receiver", userId);
                                        context.startActivity(chatIntent);
                                    }
                                }
                            });

                            builder.show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String message = databaseError.getMessage();
                Log.i("Error: ", message);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView image_user;
        private TextView_Roboto_Regular text_name;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;

            image_user = itemView.findViewById(R.id.image_user);
            text_name = itemView.findViewById(R.id.text_view_name);
        }
    }
}

package magang_2018.aseangamesindonesianguide.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.customfonts.TextView_Roboto_Regular;
import magang_2018.aseangamesindonesianguide.model.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private List<Message> messages;

    private Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String senderId = messages.get(position).getFrom();
        String currentUserId = auth.getCurrentUser().getUid();

        if (senderId.equals(currentUserId)){
            holder.linearLayoutSender.setVisibility(View.INVISIBLE);
            holder.linearLayoutReceiver.setVisibility(View.VISIBLE);

            holder.receiverMessage.setText(messages.get(position).getMessage());

        }else {

            holder.linearLayoutSender.setVisibility(View.VISIBLE);
            holder.linearLayoutReceiver.setVisibility(View.INVISIBLE);
            holder.senderMessage.setText(messages.get(position).getMessage());

            userRef.child(senderId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        Glide.with(context)
                                .load(image)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.senderImage);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    String message = databaseError.getMessage();
                    Log.i("Error: ", message);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayoutSender, linearLayoutReceiver;
        private CircleImageView senderImage;
        private TextView_Roboto_Regular senderMessage, receiverMessage;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayoutSender = itemView.findViewById(R.id.layout_sender);
            linearLayoutReceiver = itemView.findViewById(R.id.layout_receiver);
            senderImage = itemView.findViewById(R.id.image_user);
            senderMessage = itemView.findViewById(R.id.message_from_sender);
            receiverMessage = itemView.findViewById(R.id.message_from_receiver);

        }
    }
}

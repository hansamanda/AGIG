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
import com.google.firebase.auth.FirebaseAuth;
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
import magang_2018.aseangamesindonesianguide.model.Answer;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHelper> {

    private List<Answer> answers;

    private Context context;

    public AnswerAdapter(Context context) {
        this.context = context;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    private DatabaseReference userRef;
    private FirebaseAuth auth;

    @NonNull
    @Override
    public ViewHelper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        auth = FirebaseAuth.getInstance();
        return new ViewHelper(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHelper holder, int position) {
        final String currentUserId = answers.get(position).getUser_key();
        final String userLogin = auth.getCurrentUser().getUid();

        holder.answer_text.setText(answers.get(position).getAnswer());
        holder.time_text.setText(answers.get(position).getTime());

        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String id = dataSnapshot.getKey();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();

                    holder.name_text.setText("by: @"+(name));
                    Glide.with(context)
                            .load(image)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imageUser);


                    if (!id.equals(userLogin)){
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
                                            chatIntent.putExtra("key_receiver", currentUserId);
                                            context.startActivity(chatIntent);
                                        }
                                    }
                                });

                                builder.show();
                            }
                        });
                    }
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
        return answers.size();
    }

    public class ViewHelper extends RecyclerView.ViewHolder {

        private CircleImageView imageUser;
        private TextView_Roboto_Regular name_text, answer_text, time_text;
        private View view;

        public ViewHelper(View itemView) {
            super(itemView);

            view = itemView;

            imageUser = itemView.findViewById(R.id.image_user);
            name_text = itemView.findViewById(R.id.text_view_name);
            answer_text = itemView.findViewById(R.id.text_view_answer);
            time_text = itemView.findViewById(R.id.text_view_time);
        }
    }
}

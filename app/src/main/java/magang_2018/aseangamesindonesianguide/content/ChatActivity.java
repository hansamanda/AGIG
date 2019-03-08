package magang_2018.aseangamesindonesianguide.content;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.adapter.MessageAdapter;
import magang_2018.aseangamesindonesianguide.auth.AccountActivity;
import magang_2018.aseangamesindonesianguide.auth.LoginActivity;
import magang_2018.aseangamesindonesianguide.customfonts.EditText_Roboto_Regular;
import magang_2018.aseangamesindonesianguide.customfonts.TextView_Roboto_Regular;
import magang_2018.aseangamesindonesianguide.model.Message;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView list_message;
    private EditText_Roboto_Regular text_message;
    private ImageButton action_add_message;
    private ProgressDialog progressDialog;

    private List<Message> messages;

    private TextView_Roboto_Regular receiverName;
    private CircleImageView receiverProfileImage;

    private FirebaseAuth auth;

    private DatabaseReference userReceiverRef, checkCurrentUser, rootRef, messageRef, user_message_key;

    private String currentUserId, receiverUserId, senderUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            sendUserToLoginActivity();
        }else{
            currentUserId = auth.getCurrentUser().getUid();
            checkCurrentUser = FirebaseDatabase.getInstance().getReference().child("Users");
            checkUserExistence();
        }

        receiverUserId = getIntent().getExtras().get("key_receiver").toString();

        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar_chat_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = layoutInflater.inflate(R.layout.custom_actionbar,null);
        actionBar.setCustomView(action_bar_view);

        senderUserId = auth.getCurrentUser().getUid();
        messages = new ArrayList<>();
        userReceiverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(receiverUserId);
        rootRef = FirebaseDatabase.getInstance().getReference();
        messageRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(senderUserId).child(receiverUserId);

        list_message = findViewById(R.id.list_item_chat);
        text_message = findViewById(R.id.edit_text_new_chat);
        action_add_message = findViewById(R.id.action_send_new_chat);
        progressDialog = new ProgressDialog(this);

        receiverName = action_bar_view.findViewById(R.id.custom_profile_name);
        receiverProfileImage = action_bar_view.findViewById(R.id.custom_profile_image);

        loadActionBar();

        action_add_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    messages.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Message message = ds.getValue(Message.class);
                        messages.add(message);
                    }

                    list_message.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                    MessageAdapter messageAdapter = new MessageAdapter(ChatActivity.this);
                    messageAdapter.setMessages(messages);
                    list_message.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                    list_message.setAdapter(messageAdapter);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                String message = databaseError.getMessage();
                Log.i("Error: ", message);
            }
        });

    }

    private void sendMessage() {
        String textMessage = text_message.getText().toString();

        if (TextUtils.isEmpty(textMessage)){
            Toast.makeText(this, "Please type a message first...", Toast.LENGTH_SHORT).show();
        }else{
            String message_sender_ref = "Messages/" + senderUserId + "/" + receiverUserId;
            String message_receiver_ref = "Messages/" + receiverUserId + "/" + senderUserId;

            user_message_key = rootRef.child("Messages").child(senderUserId).child(receiverUserId).push();

            String message_push_id = user_message_key.getKey();

            Calendar calendarDate = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd MMMM yyyy HH:mm");
            String dateTime = currentDate.format(calendarDate.getTime());

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", textMessage);
            messageTextBody.put("date", dateTime);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", senderUserId);

            Map messageTextDetails = new HashMap();
            messageTextDetails.put(message_sender_ref + "/" + message_push_id, messageTextBody);
            messageTextDetails.put(message_receiver_ref + "/" + message_push_id, messageTextBody);

            progressDialog.show();

            rootRef.updateChildren(messageTextDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChatActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                        text_message.setText("");
                        progressDialog.dismiss();
                    }else {
                        progressDialog.dismiss();
                        String message = task.getException().getMessage();
                        Log.i("Error: ", message);
                    }
                }
            });
        }
    }

    private void loadActionBar() {
        userReceiverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();

                    receiverName.setText(name);
                    Glide.with(ChatActivity.this)
                            .load(image)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String message = databaseError.getMessage();
                Log.i("Error: ", message);
            }
        });
    }

    private void checkUserExistence() {

        checkCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(currentUserId)){
                    Toast.makeText(ChatActivity.this, "Please insert your information....", Toast.LENGTH_SHORT).show();
                    sendUserToAccountActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(ChatActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToAccountActivity() {
        Intent loginIntent = new Intent(ChatActivity.this, AccountActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}

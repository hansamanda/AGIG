package magang_2018.aseangamesindonesianguide.content;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.adapter.UserMessageAdapter;
import magang_2018.aseangamesindonesianguide.auth.AccountActivity;
import magang_2018.aseangamesindonesianguide.auth.LoginActivity;
import magang_2018.aseangamesindonesianguide.model.Message;
import magang_2018.aseangamesindonesianguide.model.UserMessage;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView list_user;
    private ProgressDialog progressDialog;

    private List<UserMessage> userMessages;

    private FirebaseAuth auth;
    private DatabaseReference userMessage, checkCurrentUser;

    private String currentUserId;

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

        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar_message_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Message");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);
        list_user = findViewById(R.id.list_item_message);
        userMessages = new ArrayList<>();
        userMessage = FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserId);

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();
        userMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    userMessages.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        UserMessage userMessage = new UserMessage(key);
                        userMessages.add(userMessage);
                    }

                    list_user.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                    UserMessageAdapter userMessageAdapter = new UserMessageAdapter(MessageActivity.this);
                    userMessageAdapter.setUserMessages(userMessages);
                    list_user.setAdapter(userMessageAdapter);

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

    private void checkUserExistence() {

        checkCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(currentUserId)){
                    Toast.makeText(MessageActivity.this, "Please insert your information....", Toast.LENGTH_SHORT).show();
                    sendUserToAccountActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MessageActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToAccountActivity() {
        Intent loginIntent = new Intent(MessageActivity.this, AccountActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}

package magang_2018.aseangamesindonesianguide.content;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.adapter.AnswerAdapter;
import magang_2018.aseangamesindonesianguide.auth.AccountActivity;
import magang_2018.aseangamesindonesianguide.auth.LoginActivity;
import magang_2018.aseangamesindonesianguide.customfonts.EditText_Roboto_Regular;
import magang_2018.aseangamesindonesianguide.customfonts.TextView_Roboto_Regular;
import magang_2018.aseangamesindonesianguide.model.Answer;

public class AnswerForumActivity extends AppCompatActivity {

    private RecyclerView list_answer;
    private EditText_Roboto_Regular text_answer;
    private TextView_Roboto_Regular text_question, text_time, text_name;
    private CircleImageView image_user;
    private ImageButton action_add_answer;
    private ProgressDialog progressDialog;

    private FirebaseAuth auth;

    private DatabaseReference answerRef, userRef, questionRef, checkUser;
    private Query queryAnswer;

    private String currentCategoryId, currentQuestionId, currentSenderId, currentUserId;

    private List<Answer> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            sendUserToLoginActivity();
        }else{
            currentUserId = auth.getCurrentUser().getUid();
            checkUser = FirebaseDatabase.getInstance().getReference().child("Users");
            checkUserExistence();
        }

        currentCategoryId = getIntent().getExtras().get("key_category").toString();
        currentQuestionId = getIntent().getExtras().get("key_question").toString();
        currentSenderId = getIntent().getExtras().get("key_user").toString();

        setContentView(R.layout.activity_answer_forum);
        Toolbar toolbar = findViewById(R.id.toolbar_answer_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Answer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressDialog = new ProgressDialog(this);

        questionRef = FirebaseDatabase.getInstance().getReference().child("Forums").child("Categories")
                .child(currentCategoryId).child("Questions").child(currentQuestionId);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentSenderId);

        answerRef = FirebaseDatabase.getInstance().getReference().child("Forums").child("Categories")
                .child(currentCategoryId).child("Questions").child(currentQuestionId).child("Answers");

        queryAnswer = answerRef.orderByChild("time");

        answers = new ArrayList<>();

        list_answer = findViewById(R.id.list_item_answer);
        text_answer = findViewById(R.id.edit_text_new_answer);
        text_question = findViewById(R.id.text_view_question);
        text_time = findViewById(R.id.text_view_time);
        text_name = findViewById(R.id.text_view_name);
        image_user = findViewById(R.id.image_user);
        action_add_answer = findViewById(R.id.action_send_answer);

        action_add_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationInput();
            }
        });

    }

    private void validationInput() {
        String input_answer = text_answer.getText().toString();

        if (TextUtils.isEmpty(input_answer)){
            Toast.makeText(this, "......", Toast.LENGTH_SHORT).show();
        }else{

            String randomKey = UUID.randomUUID().toString();

            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd MMMM yyyy HH:mm");
            String dateTime = currentDate.format(calendar.getTime());

            HashMap answerMap = new HashMap();
            answerMap.put("answer_key", randomKey);
            answerMap.put("user_key", currentUserId);
            answerMap.put("answer", input_answer);
            answerMap.put("time", dateTime);

            progressDialog.show();

            answerRef.child(randomKey).updateChildren(answerMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Log.i("Message: ", "Success");
                                text_answer.setText("");
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

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String question = dataSnapshot.child("question").getValue().toString();
                    String time = dataSnapshot.child("time").getValue().toString();

                    text_question.setText(question);
                    text_time.setText(time);

                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                String name = dataSnapshot.child("name").getValue().toString();
                                String image = dataSnapshot.child("image").getValue().toString();

                                text_name.setText("by: @"+(name));
                                Glide.with(AnswerForumActivity.this)
                                        .load(image)
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(image_user);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                            String message = databaseError.getMessage();
                            Log.i("Error: ", message);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                String message = databaseError.getMessage();
                Log.i("Error: ", message);
            }
        });

        queryAnswer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    answers.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Answer answer = ds.getValue(Answer.class);
                        answers.add(answer);
                    }

                    list_answer.setLayoutManager(new LinearLayoutManager(AnswerForumActivity.this));
                    AnswerAdapter answerAdapter = new AnswerAdapter(AnswerForumActivity.this);
                    answerAdapter.setAnswers(answers);
                    list_answer.smoothScrollToPosition(answerAdapter.getItemCount()-1);
                    list_answer.setAdapter(answerAdapter);

                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String message = databaseError.getMessage();
                Log.i("Error: ", message);
                progressDialog.dismiss();
            }
        });
    }

    private void checkUserExistence() {

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(currentUserId)){
                    Toast.makeText(AnswerForumActivity.this, "Please insert your information....", Toast.LENGTH_SHORT).show();
                    sendUserToAccountActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(AnswerForumActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToAccountActivity() {
        Intent loginIntent = new Intent(AnswerForumActivity.this, AccountActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}

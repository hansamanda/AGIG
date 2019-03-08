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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.adapter.QuestionAdapter;
import magang_2018.aseangamesindonesianguide.auth.AccountActivity;
import magang_2018.aseangamesindonesianguide.auth.LoginActivity;
import magang_2018.aseangamesindonesianguide.customfonts.EditText_Roboto_Regular;
import magang_2018.aseangamesindonesianguide.model.Question;

public class QuestionForumActivity extends AppCompatActivity {

    private RecyclerView list_question;
    private EditText_Roboto_Regular text_question;
    private ImageButton action_add_question;
    private MaterialSearchView searchView;
    private ProgressDialog progressDialog;

    private List<Question> questions;

    private FirebaseAuth auth;

    private DatabaseReference questionRef, userRef;
    private Query queryQuestion, querySearch;

    private String currentUserId, currentCategoryId;
    private long countQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            sendUserToLoginActivity();
        }else{
            currentUserId = auth.getCurrentUser().getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("Users");
            checkUserExistence();
        }
        currentCategoryId = getIntent().getExtras().get("key_category").toString();
        setContentView(R.layout.activity_question_forum);
        Toolbar toolbar = findViewById(R.id.toolbar_question_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressDialog = new ProgressDialog(this);

        questionRef = FirebaseDatabase.getInstance().getReference().child("Forums").child("Categories")
                .child(currentCategoryId).child("Questions");
        queryQuestion = questionRef.orderByChild("counter");

        questions = new ArrayList<>();

        list_question = findViewById(R.id.list_item_question);
        searchView = findViewById(R.id.search_view_question);
        text_question = findViewById(R.id.edit_text_new_ask_question);
        action_add_question = findViewById(R.id.action_send_question);

        action_add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationInput();
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                queryQuestion.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            questions.clear();

                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                Question question = ds.getValue(Question.class);
                                questions.add(question);
                            }

                            list_question.setLayoutManager(new LinearLayoutManager(QuestionForumActivity.this));
                            QuestionAdapter questionAdapter = new QuestionAdapter(QuestionForumActivity.this);
                            questionAdapter.setQuestions(questions);
                            list_question.setAdapter(questionAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        String message = databaseError.getMessage();
                        Log.i("Error: ", message);
                    }
                });
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText != null && !newText.isEmpty()){
                    querySearch = questionRef.orderByChild("question").startAt(newText).endAt(newText + "\uf8ff");
                    querySearch.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                questions.clear();

                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    Question question = ds.getValue(Question.class);
                                    questions.add(question);
                                }

                                list_question.setLayoutManager(new LinearLayoutManager(QuestionForumActivity.this));
                                QuestionAdapter questionAdapter = new QuestionAdapter(QuestionForumActivity.this);
                                questionAdapter.setQuestions(questions);
                                list_question.setAdapter(questionAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            String message = databaseError.getMessage();
                            Log.i("Error: ", message);
                        }
                    });
                }else{
                    queryQuestion.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                questions.clear();

                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    Question question = ds.getValue(Question.class);
                                    questions.add(question);
                                }

                                list_question.setLayoutManager(new LinearLayoutManager(QuestionForumActivity.this));
                                QuestionAdapter questionAdapter = new QuestionAdapter(QuestionForumActivity.this);
                                questionAdapter.setQuestions(questions);
                                list_question.setAdapter(questionAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            String message = databaseError.getMessage();
                            Log.i("Error: ", message);
                        }
                    });
                }

                return true;
            }
        });
    }

    private void validationInput() {
        String input_question = text_question.getText().toString();

        if (TextUtils.isEmpty(input_question)){
            Toast.makeText(this, ".....", Toast.LENGTH_SHORT).show();
        }else{

            questionRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        countQuestion = dataSnapshot.getChildrenCount();
                    }else {
                        countQuestion = 0;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            String randomKey = UUID.randomUUID().toString();

            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd MMMM yyyy HH:mm");
            String dateTime = currentDate.format(calendar.getTime());

            HashMap questionMap = new HashMap();
            questionMap.put("question_key", randomKey);
            questionMap.put("user_key", currentUserId);
            questionMap.put("category_key", currentCategoryId);
            questionMap.put("question", input_question);
            questionMap.put("time", dateTime);
            questionMap.put("counter", countQuestion);

            progressDialog.show();

            questionRef.child(randomKey).updateChildren(questionMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Log.i("Message: ", "Success");
                        text_question.setText("");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();
        queryQuestion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    questions.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Question question = ds.getValue(Question.class);
                        questions.add(question);
                    }

                    list_question.setLayoutManager(new LinearLayoutManager(QuestionForumActivity.this));
                    QuestionAdapter questionAdapter = new QuestionAdapter(QuestionForumActivity.this);
                    questionAdapter.setQuestions(questions);
                    list_question.setAdapter(questionAdapter);
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

        final String current_user_id = auth.getCurrentUser().getUid();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (!dataSnapshot.hasChild(current_user_id)){
                        Toast.makeText(QuestionForumActivity.this, "Please insert your information....", Toast.LENGTH_SHORT).show();
                        sendUserToAccountActivity();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(QuestionForumActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToAccountActivity() {
        Intent loginIntent = new Intent(QuestionForumActivity.this, AccountActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}

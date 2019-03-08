package magang_2018.aseangamesindonesianguide.content;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.adapter.CategoryAdapter;
import magang_2018.aseangamesindonesianguide.adapter.QuestionAdapter;
import magang_2018.aseangamesindonesianguide.auth.LoginActivity;
import magang_2018.aseangamesindonesianguide.customfonts.EditText_Roboto_Regular;
import magang_2018.aseangamesindonesianguide.model.Category;
import magang_2018.aseangamesindonesianguide.model.Question;

public class ForumActivity extends AppCompatActivity {

    private RecyclerView list_category;
//    private EditText_Roboto_Regular text_category;
//    private ImageButton action_add_category;
    private MaterialSearchView searchView;
    private ProgressDialog progressDialog;

    private FirebaseAuth auth;

    private DatabaseReference forumRef;
    private Query queryForum, querySearch;

    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            sendUserToLoginActivity();
        }
        setContentView(R.layout.activity_forum);

        Toolbar toolbar = findViewById(R.id.toolbar_forum_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forum Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressDialog = new ProgressDialog(this);

        forumRef = FirebaseDatabase.getInstance().getReference().child("Forums").child("Categories");
        queryForum = forumRef.orderByChild("name");

        list_category = findViewById(R.id.list_item_category);
        searchView = findViewById(R.id.search_view);
//        text_category = findViewById(R.id.edit_text_new_category);
//        action_add_category = findViewById(R.id.action_send_new_category);

        categories = new ArrayList<>();

//        action_add_category.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                validationInput();
//            }
//        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                queryForum.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            categories.clear();

                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                Category category = ds.getValue(Category.class);
                                categories.add(category);
                            }

                            list_category.setLayoutManager(new LinearLayoutManager(ForumActivity.this));
                            CategoryAdapter categoryAdapter = new CategoryAdapter(ForumActivity.this);
                            categoryAdapter.setCategories(categories);
                            list_category.setAdapter(categoryAdapter);

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
                    querySearch = forumRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff");
                    querySearch.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                categories.clear();

                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    Category category = ds.getValue(Category.class);
                                    categories.add(category);
                                }

                                list_category.setLayoutManager(new LinearLayoutManager(ForumActivity.this));
                                CategoryAdapter categoryAdapter = new CategoryAdapter(ForumActivity.this);
                                categoryAdapter.setCategories(categories);
                                list_category.setAdapter(categoryAdapter);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            String message = databaseError.getMessage();
                            Log.i("Error: ", message);
                        }
                    });
                }else{
                    queryForum.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                categories.clear();

                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    Category category = ds.getValue(Category.class);
                                    categories.add(category);
                                }

                                list_category.setLayoutManager(new LinearLayoutManager(ForumActivity.this));
                                CategoryAdapter categoryAdapter = new CategoryAdapter(ForumActivity.this);
                                categoryAdapter.setCategories(categories);
                                list_category.setAdapter(categoryAdapter);

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

//    private void validationInput() {
//        String category_input = text_category.getText().toString();
//
//        if (TextUtils.isEmpty(category_input)){
//            Toast.makeText(this, ".....", Toast.LENGTH_SHORT).show();
//        }else {
//
//            String randomKey = UUID.randomUUID().toString();
//
//            HashMap categoryMap = new HashMap();
//            categoryMap.put("key", randomKey);
//            categoryMap.put("name", category_input);
//
//            forumRef.child(randomKey).updateChildren(categoryMap)
//                    .addOnCompleteListener(new OnCompleteListener() {
//                        @Override
//                        public void onComplete(@NonNull Task task) {
//                            if (task.isSuccessful()){
//                                Log.i("Message: ", "Success");
//                                text_category.setText("");
//                            }else {
//                                String message = task.getException().getMessage();
//                                Log.i("Error: ", message);
//                            }
//                        }
//                    });
//        }
//    }

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
        queryForum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    categories.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Category category = ds.getValue(Category.class);
                        categories.add(category);
                    }

                    list_category.setLayoutManager(new LinearLayoutManager(ForumActivity.this));
                    CategoryAdapter categoryAdapter = new CategoryAdapter(ForumActivity.this);
                    categoryAdapter.setCategories(categories);
                    list_category.setAdapter(categoryAdapter);
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

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(ForumActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

}
